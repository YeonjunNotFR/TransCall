package com.youhajun.core.design

import com.youhajun.core.model.CountryType

fun CountryType.toCountryIcon(): Int = when(this) {
    CountryType.ARGENTINA -> R.drawable.country_ar
    CountryType.AUSTRALIA -> R.drawable.country_au
    CountryType.AUSTRIA -> R.drawable.country_at
    CountryType.BELGIUM -> R.drawable.country_be
    CountryType.BRAZIL -> R.drawable.country_br
    CountryType.BULGARIA -> R.drawable.country_bg
    CountryType.CANADA -> R.drawable.country_ca
    CountryType.CHILE -> R.drawable.country_cl
    CountryType.CHINA -> R.drawable.country_cn
    CountryType.COLOMBIA -> R.drawable.country_co
    CountryType.CROATIA -> R.drawable.country_hr
    CountryType.CZECH_REPUBLIC -> R.drawable.country_cz
    CountryType.DENMARK -> R.drawable.country_dk
    CountryType.EGYPT -> R.drawable.country_eg
    CountryType.ESTONIA -> R.drawable.country_ee
    CountryType.FINLAND -> R.drawable.country_fi
    CountryType.FRANCE -> R.drawable.country_fr
    CountryType.GERMANY -> R.drawable.country_de
    CountryType.GREECE -> R.drawable.country_gr
    CountryType.HONG_KONG -> R.drawable.country_hk
    CountryType.HUNGARY -> R.drawable.country_hu
    CountryType.ICELAND -> R.drawable.country_is
    CountryType.INDIA -> R.drawable.country_in
    CountryType.INDONESIA -> R.drawable.country_id
    CountryType.IRELAND -> R.drawable.country_ie
    CountryType.ISRAEL -> R.drawable.country_il
    CountryType.ITALY -> R.drawable.country_it
    CountryType.JAPAN -> R.drawable.country_jp
    CountryType.KAZAKHSTAN -> R.drawable.country_kz
    CountryType.KENYA -> R.drawable.country_ke
    CountryType.MALAYSIA -> R.drawable.country_my
    CountryType.MEXICO -> R.drawable.country_mx
    CountryType.MOROCCO -> R.drawable.country_ma
    CountryType.NETHERLANDS -> R.drawable.country_nl
    CountryType.NEW_ZEALAND -> R.drawable.country_nz
    CountryType.NIGERIA -> R.drawable.country_ng
    CountryType.NORWAY -> R.drawable.country_no
    CountryType.PHILIPPINES -> R.drawable.country_ph
    CountryType.POLAND -> R.drawable.country_pl
    CountryType.PORTUGAL -> R.drawable.country_pt
    CountryType.ROMANIA -> R.drawable.country_ro
    CountryType.RUSSIA -> R.drawable.country_ru
    CountryType.SAUDI_ARABIA -> R.drawable.country_sa
    CountryType.SERBIA -> R.drawable.country_rs
    CountryType.SINGAPORE -> R.drawable.country_sg
    CountryType.SLOVAKIA -> R.drawable.country_sk
    CountryType.SLOVENIA -> R.drawable.country_si
    CountryType.SOUTH_AFRICA -> R.drawable.country_za
    CountryType.SOUTH_KOREA -> R.drawable.country_kr
    CountryType.SPAIN -> R.drawable.country_es
    CountryType.SWEDEN -> R.drawable.country_se
    CountryType.SWITZERLAND -> R.drawable.country_ch
    CountryType.TAIWAN -> R.drawable.country_tw
    CountryType.THAILAND -> R.drawable.country_th
    CountryType.TURKEY -> R.drawable.country_tr
    CountryType.UKRAINE -> R.drawable.country_ua
    CountryType.UNITED_ARAB_EMIRATES -> R.drawable.country_ae
    CountryType.UNITED_KINGDOM -> R.drawable.country_gb
    CountryType.UNITED_STATES -> R.drawable.country_us
    CountryType.VIETNAM -> R.drawable.country_vn
}