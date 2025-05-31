package com.youhajun.core.stt

import android.content.Context
import com.youhajun.transcall.core.common.DefaultDispatcher
import com.youhajun.transcall.core.common.MainImmediateDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ServiceComponent::class)
internal object SttModule {

    @Provides
    @ServiceScoped
    fun provideStreamSpeechRecognizerManager(
        @ApplicationContext context: Context,
        @MainImmediateDispatcher mainDispatcher: CoroutineDispatcher,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): StreamSpeechRecognizerManager {
        return StreamSpeechRecognizerManager(
            context = context,
            mainDispatcher = mainDispatcher,
            defaultDispatcher = defaultDispatcher
        )
    }
}
