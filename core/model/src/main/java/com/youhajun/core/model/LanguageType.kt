package com.youhajun.core.model;

enum class LanguageType(
    val code: String,
) {
    AFRIKAANS("AF"),
    ARABIC("AR"),
    BENGALI("BN"),
    BULGARIAN("BG"),
    CATALAN("CA"),
    CHINESE("ZH"),
    CROATIAN("HR"),
    CZECH("CS"),
    DANISH("DA"),
    DUTCH("NL"),
    ENGLISH("EN"),
    ESTONIAN("ET"),
    FINNISH("FI"),
    FRENCH("FR"),
    GERMAN("DE"),
    GREEK("EL"),
    HEBREW("HE"),
    HINDI("HI"),
    HUNGARIAN("HU"),
    INDONESIAN("ID"),
    ITALIAN("IT"),
    JAPANESE("JA"),
    KOREAN("KO"),
    MALAY("MS"),
    NORWEGIAN("NO"),
    POLISH("PL"),
    PORTUGUESE("PT"),
    ROMANIAN("RO"),
    RUSSIAN("RU"),
    SERBIAN("SR"),
    SLOVAK("SK"),
    SLOVENIAN("SL"),
    SPANISH("ES"),
    SWEDISH("SV"),
    TAGALOG("TL"),
    THAI("TH"),
    TURKISH("TR"),
    UKRAINIAN("UK"),
    URDU("UR"),
    VIETNAMESE("VI");

    companion object {
        fun fromCode(code: String): LanguageType =
            entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
    }
}