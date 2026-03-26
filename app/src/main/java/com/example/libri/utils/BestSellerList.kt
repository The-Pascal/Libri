package com.example.libri.utils

enum class CategoryGroup(val displayName: String) {
    FICTION("Fiction"),
    NONFICTION("Nonfiction"),
    KIDS_YA("Kids & YA"),
    LIFESTYLE("Lifestyle"),
    AUDIO("Audio"),
    MISCELLANEOUS("Miscellaneous"),
}

enum class BestSellerList(
    val encodedName: String,
    val group: CategoryGroup // Associating sub-lists with pills
) {
    // Fiction
    COMBINED_FICTION("combined-print-and-e-book-fiction", CategoryGroup.FICTION),
    HARDCOVER_FICTION("hardcover-fiction", CategoryGroup.FICTION),
    PAPERBACK_FICTION("trade-fiction-paperback", CategoryGroup.FICTION),
    MASS_MARKET("mass-market-monthly", CategoryGroup.FICTION),

    // Nonfiction
    COMBINED_NONFICTION("combined-print-and-e-book-nonfiction", CategoryGroup.NONFICTION),
    HARDCOVER_NONFICTION("hardcover-nonfiction", CategoryGroup.NONFICTION),
    PAPERBACK_NONFICTION("paperback-nonfiction", CategoryGroup.NONFICTION),

    // Kids & YA
    PICTURE_BOOKS("picture-books", CategoryGroup.KIDS_YA),
    MIDDLE_GRADE_HC("childrens-middle-grade-hardcover", CategoryGroup.KIDS_YA),
    YA_HARDCOVER("young-adult-hardcover", CategoryGroup.KIDS_YA),
    SERIES_BOOKS("series-books", CategoryGroup.KIDS_YA),
    MIDDLE_GRADE_PB("middle-grade-paperback-monthly", CategoryGroup.KIDS_YA),
    YA_PAPERBACK("young-adult-paperback-monthly", CategoryGroup.KIDS_YA),

    // Lifestyle/Business
    ADVICE_HOW_TO("advice-how-to-and-miscellaneous", CategoryGroup.LIFESTYLE),
    BUSINESS("business-books", CategoryGroup.LIFESTYLE),
    GRAPHIC_BOOKS("graphic-books-and-manga", CategoryGroup.LIFESTYLE),

    // Audio
    AUDIO_FICTION("audio-fiction", CategoryGroup.AUDIO),
    AUDIO_NONFICTION("audio-nonfiction", CategoryGroup.AUDIO),

    UNKNOWN("unknown", CategoryGroup.MISCELLANEOUS);

    companion object {
        fun fromEncodedName(name: String?) = entries.find { it.encodedName == name } ?: UNKNOWN
    }
}