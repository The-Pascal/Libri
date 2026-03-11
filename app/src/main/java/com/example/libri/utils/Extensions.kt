package com.example.libri.utils

fun String.removeIdPrefix(): String = this.removePrefix("/works/")