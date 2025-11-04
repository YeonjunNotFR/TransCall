# TransCall – 실시간 번역 영상 통화 앱 (Android)

## 개요
**TransCall**은 언어 장벽을 없애는 것을 목표로 하는 영상 통화 앱입니다. 사용자는 방을 개설하거나 참여해 최대 **다자간**으로 영상·음성 통화를 할 수 있으며, 음성은 **Whisper‑AI**를 통한 **음성→자막** 변환 후 서버에서 번역되어 실시간으로 자막 형태로 전달됩니다. 이 앱은 Jetpack Compose 기반으로 설계되었으며, 오프라인‑우선 구조와 WebRTC를 활용한 **저지연** 미디어 스트리밍을 특징으로 합니다.

## 주요 기능
- **방 생성 및 참여**: 홈 화면에서 방을 생성하거나 코드로 참여할 수 있습니다. 사용자는 남은 이용 시간과 멤버십 정보가 표시된 프로필 카드와 최근 통화 이력을 볼 수 있으며, 대기 중인 방이 있으면 바로 입장 가능합니다.
- **실시간 영상/음성 통화**: WebRTC 기반의 **통화 서비스**를 제공하며, 카메라 전환, 마이크/카메라 켜기·끄기, 음소거·출력 설정 등의 제어가 가능합니다. `WebRtcSessionManager` 인터페이스는 세션 초기화, 시작, 카메라/마이크 제어, 오디오 장치 변경 등 통화 제어를 정의합니다.
- **실시간 번역 자막**: 음성 스트림은 앱 내에서 PCM 데이터로 추출된 후 서버의 Whisper 서비스로 전송되어 STT 결과를 얻습니다. 서버에서 번역된 자막을 받아 UI에 오버레이 형태로 표시합니다. 최근 대화는 Paging을 통해 리스트로 볼 수 있으며, 대화 업데이트는 FCM으로 트리거되어 동기화됩니다.
- **통화 기록 및 오프라인 동기화**: 모든 통화는 로컬 데이터베이스에 저장되며, 오프라인 환경에서도 조회할 수 있습니다. 서버와의 동기화는 커서 기반 페이지네이션과 타임스탬프(`updatedAt`)를 이용해 증분으로 이뤄집니다. `ConversationRepository`는 페이징 흐름, 최근 대화 스트림, 동기화용 API를 제공합니다. RemoteMediator는 백그라운드에서 서버 데이터를 가져와 로컬 데이터베이스에 저장합니다.
- **유저 인증 및 멤버십**: Google OAuth 인증을 이용해 로그인하며 서버가 발급하는 JWT를 사용합니다. 앱은 남은 통화 가능 시간과 멤버십 플랜을 표시하고, 제한 시간이 부족할 경우 업그레이드를 안내합니다.
- **푸시 알림 및 백그라운드 서비스**: Firebase Cloud Messaging을 통해 서버에서 통화 초대, 대화 업데이트 등을 받습니다. WorkManager를 사용해 앱이 백그라운드 상태에서도 일정 시간마다 대화 동기화를 수행합니다.
- **권한 및 퍼미션 처리**: 카메라와 마이크 권한, Android 13 이상의 알림 권한을 동적으로 요청하며, 퍼미션이 거부되면 사용자에게 rationale 메시지를 보여줍니다.

## 아키텍처 개요
TransCall은 **클린 아키텍처**와 **멀티 모듈** 구조를 적용해 유지 보수성과 확장성을 높였습니다. 프로젝트의 `settings.gradle.kts`에서 정의된 주요 모듈은 다음과 같습니다:

- **앱 모듈 (`:app`)**: 실제 Android 애플리케이션. Compose 테마, Hilt DI, Firebase, WorkManager 등이 설정되어 있으며, feature·domain·data·core 모듈을 종속성으로 포함합니다.
- **feature 모듈**: UI 및 화면 로직을 담당하며 각 기능별로 분리되어 있습니다. `home`, `call`, `room`, `history`, `auth`, `splash` 등이 있고, 각 모듈에는 `api` 모듈이 있어 다른 모듈과의 경계를 정의합니다. 예를 들어, `feature/home` 모듈의 `HomeScreen`은 사용자 프로필 카드, 현재 방, 통화 기록을 표시하고 방 생성·참여·기록 열람을 처리합니다.
- **domain 모듈**: 비즈니스 로직과 엔티티를 정의합니다. 예: `domain/conversation`은 대화의 페이징·최근 대화 스트림·동기화를 위한 인터페이스를 제공하고, `domain/user`, `domain/room` 등 각 영역별 도메인을 분리합니다.
- **data 모듈**: 리포지토리 구현과 로컬·원격 데이터 소스를 포함합니다. `ConversationRepositoryImpl`은 Paging3 `RemoteMediator`를 사용해 서버와 로컬 DB를 동기화합니다. `data/user`는 서버 API를 호출하여 사용자 정보를 가져오고, `data/room`은 방 생성·참여 API를 래핑합니다.
- **core 모듈**: 애플리케이션 전반에서 사용하는 공통 기능을 제공합니다. `core/network`는 Ktor/OkHttp 기반 네트워크 클라이언트와 JWT 토큰 자동 갱신 로직을 갖추고, `core/database`는 Room 데이터베이스와 DAO, `core/webRtc`는 WebRTC 세션을 구성하는 `WebRtcSessionManagerImpl` 구현과 peer connection factory를 제공합니다. `core/design`에는 디자인 시스템(Typography, Colors)과 UI 컴포넌트가 정의되고, `core/notification`은 FCM 처리, `core/permission`은 런타임 권한 요청 컴포넌트를 제공하며, `core/route`는 Compose Navigation을 추상화합니다.

모듈 간 의존성은 **단방향**을 유지해 의존성 역전을 따른다. 앱 모듈은 feature를, feature는 domain을, domain은 data를, data는 core를 의존합니다. Hilt와 KSP를 활용해 DI를 설정해 모듈 간 결합을 낮췄습니다.

## 실시간 통화 및 번역 파이프라인
- **WebRTC 통화**: `WebRtcSessionManagerImpl`은 publish/subscriber peer connection을 생성하고 signaling 메시지를 전송하여 Janus SFU에 연결합니다. 카메라와 오디오 트랙을 추가하고, 원격 피드에 대한 ICE candidate 및 SDP 교환을 처리합니다. 통화 중 카메라 전환·마이크/스피커 설정 변경 등이 가능하며, `CallServiceContract`를 통해 UI와 세션 관리자가 분리됩니다.
- **시그널링과 서버 연동**: feature/call 모듈은 Foreground Service(`CallServiceBinder`)를 통해 서버와 WebSocket으로 연결합니다. 서버는 Janus를 통해 SFU 기능과 Whisper STT/번역 마이크로서비스를 조합하여 번역된 자막을 전달합니다.
- **자막 동기화**: 앱은 서버가 보내는 `TranslationMessage` 스트림을 수신하여 UI에 표시하고, 로컬 DB에 저장합니다. 대화는 커서 기반으로 페이징되며 `getConversationPagingDataFlow`로 Flow<PagingData> 형태로 제공됩니다.

## 데이터 관리 및 오프라인 전략
- **로컬 데이터베이스**: Room 데이터베이스를 통해 방 정보, 통화 기록, 대화 메시지 등을 저장합니다. 외부 네트워크가 끊겨도 로컬 DB를 사용해 UI를 표시할 수 있으며, 서버와의 동기화는 변경된 데이터만 가져오는 방식으로 최적화되었습니다.
- **RemoteMediator**: 대화(자막) 영역에서 Paging3 `RemoteMediator`를 사용하여 새 메시지가 서버에 생길 때마다 로컬 DB에 자동 반영합니다. 이 과정은 WorkManager 및 FCM을 통해 트리거됩니다.
- **데이터스토어(DataStore)**: 사용자 설정(예: 언어, 마이크/카메라 기본 설정)을 저장합니다.

## 인증 및 보안
- **Google OAuth**: 로그인을 위해 Google 인증을 사용하고 서버로 nonce와 idToken을 전송해 JWT를 발급받습니다. 이 JWT는 네트워크 계층에서 자동으로 헤더에 첨부되고, 만료 시 새 토큰을 발급받는 로직이 구현되어 있습니다.
- **권한 관리**: 앱 실행 시 카메라·마이크 권한을 요구하며, Android 13 이상에서는 알림 권한을 추가로 요청합니다. 거부 시 rationale 메시지를 표시하여 권한의 필요성을 설명합니다.

## 프로젝트 구조
```
TransCall/
  app/                    # Android 애플리케이션 모듈
  feature/
    home/                 # 홈 화면 모듈
    call/                 # 통화 화면 모듈 (서비스 포함)
    room/                 # 방 생성/참여 화면
    history/              # 통화 기록 화면
    auth/                 # 로그인/회원가입 화면
    splash/               # 초기 로딩/스플래시 화면
  domain/
    room/                 # 방 도메인 로직
    calling/              # 통화 로직 (WebRTC)
    conversation/         # 대화(번역 자막) 로직
    history/              # 통화 기록 도메인
    user/                 # 사용자 도메인
    auth/                 # 인증 도메인
  data/
    room/                 # 방 Repository 구현
    calling/              # 통화 데이터소스 구현
    conversation/         # 대화 Repository 구현 및 RemoteMediator
    history/              # 통화 기록 Repository
    user/                 # 사용자 Repository
    auth/                 # 인증 Repository
  core/
    network/              # 네트워크 클라이언트, 인터셉터, JWT 관리
    webRtc/               # WebRTC 세션 관리, PeerConnection 설정
    database/             # Room 데이터베이스, Dao
    notification/         # FCM 처리
    design/               # 공통 UI 디자인 시스템
    permission/           # 퍼미션 요청 핸들러
    route/                # Compose Navigation 추상화
    model/                # 앱 전반에서 사용되는 모델 정의
```