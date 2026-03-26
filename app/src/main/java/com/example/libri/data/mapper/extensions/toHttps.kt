package com.example.libri.data.mapper.extensions

val String?.toHttps: String?
    get() = this?.replace("http://", "https://")