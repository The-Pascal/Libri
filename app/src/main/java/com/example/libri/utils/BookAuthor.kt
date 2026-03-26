package com.example.libri.utils

sealed class BookAuthor(val olid: String, val displayName: String, val imageUrl: String) {

    // Fantasy & Sci-Fi
    data object J_K_ROWLING : BookAuthor(
        "OL23919A",
        "J.K. Rowling",
        "https://covers.openlibrary.org/a/olid/OL23919A-L.jpg?default=false"
    )

    data object J_R_R_TOLKIEN : BookAuthor(
        "OL26320A",
        "J.R.R. Tolkien",
        "https://covers.openlibrary.org/a/olid/OL26320A-L.jpg?default=false"
    )

    data object GEORGE_RR_MARTIN : BookAuthor(
        "OL234664A",
        "George R.R. Martin",
        "https://covers.openlibrary.org/a/olid/OL234664A-L.jpg?default=false"
    )

    // Thriller & Mystery
    data object STEPHEN_KING : BookAuthor(
        "OL19981A",
        "Stephen King",
        "https://covers.openlibrary.org/a/olid/OL19981A-L.jpg?default=false"
    )

    data object AGATHA_CHRISTIE : BookAuthor(
        "OL27695A",
        "Agatha Christie",
        "https://covers.openlibrary.org/a/olid/OL27695A-L.jpg?default=false"
    )

    data object DAN_BROWN : BookAuthor(
        "OL39307A",
        "Dan Brown",
        "https://covers.openlibrary.org/a/olid/OL39307A-L.jpg?default=false"
    )

    // Contemporary & Literary
    data object HARUKI_MURAKAMI : BookAuthor(
        "OL382524A",
        "Haruki Murakami",
        "https://covers.openlibrary.org/a/olid/OL382524A-L.jpg?default=false"
    )

    data object COLLEEN_HOOVER : BookAuthor(
        "OL7315784A",
        "Colleen Hoover",
        "https://covers.openlibrary.org/a/olid/OL7315784A-L.jpg?default=false"
    )

    // Classics
    data object JANE_AUSTEN : BookAuthor(
        "OL21594A",
        "Jane Austen",
        "https://covers.openlibrary.org/a/olid/OL21594A-L.jpg?default=false"
    )

    data object MARK_TWAIN : BookAuthor(
        "OL18319A",
        "Mark Twain",
        "https://covers.openlibrary.org/a/olid/OL18319A-L.jpg?default=false"
    )

    companion object {
        val all = listOf(
            J_K_ROWLING, J_R_R_TOLKIEN, GEORGE_RR_MARTIN,
            STEPHEN_KING, AGATHA_CHRISTIE, DAN_BROWN,
            HARUKI_MURAKAMI, COLLEEN_HOOVER, JANE_AUSTEN, MARK_TWAIN
        )
    }
}