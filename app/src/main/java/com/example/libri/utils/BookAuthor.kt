package com.example.libri.utils

sealed class BookAuthor(val olid: String, val displayName: String, val imageUrl: String) {

    // Fantasy & Sci-Fi
    data object J_K_ROWLING : BookAuthor(
        "OL311W",
        "J.K. Rowling",
        "https://images.mubicdn.net/images/cast_member/23749/cache-699739-1627338129/image-w856.jpg"
    )

    data object J_R_R_TOLKIEN : BookAuthor(
        "OL123W",
        "J.R.R. Tolkien",
        "https://cdn.britannica.com/65/66765-050-63A945A7/JRR-Tolkien.jpg"
    )

    data object GEORGE_RR_MARTIN : BookAuthor(
        "OL26242A",
        "George R.R. Martin",
        "https://images.gr-assets.com/authors/1351200547p8/346732.jpg"
    )

    // Thriller & Mystery

    data object STEPHEN_KING : BookAuthor(
        "OL21622A",
        "Stephen King",
        "https://authors.b-cdn.net/wp-content/uploads/2020/07/stephen-king.jpg"
    )

    data object AGATHA_CHRISTIE : BookAuthor(
        "OL27695A",
        "Agatha Christie",
        "https://images.gr-assets.com/authors/1574341147p8/123715.jpg"
    )

    data object DAN_BROWN : BookAuthor(
        "OL26354A",
        "Dan Brown",
        "https://images.gr-assets.com/authors/1399399211p8/630.jpg"
    )

    // Contemporary & Literary
    data object HARUKI_MURAKAMI : BookAuthor(
        "OL118077A",
        "Haruki Murakami",
        "https://images.gr-assets.com/authors/1614760596p8/3354.jpg"
    )

    data object COLLEEN_HOOVER : BookAuthor(
        "OL7135010A",
        "Colleen Hoover",
        "https://images.gr-assets.com/authors/1647466540p8/5430144.jpg"
    )

    // Classics
    data object JANE_AUSTEN : BookAuthor(
        "OL21594A",
        "Jane Austen",
        "https://images.gr-assets.com/authors/1470483259p8/1265.jpg"
    )

    data object MARK_TWAIN : BookAuthor(
        "OL18319A",
        "Mark Twain",
        "https://images.gr-assets.com/authors/1327366363p8/1244.jpg"
    )

    companion object {
        val all = listOf(
            J_K_ROWLING, J_R_R_TOLKIEN, GEORGE_RR_MARTIN,
            STEPHEN_KING, AGATHA_CHRISTIE, DAN_BROWN,
            HARUKI_MURAKAMI, COLLEEN_HOOVER, JANE_AUSTEN, MARK_TWAIN
        )
    }
}