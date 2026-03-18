package com.example.libri.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.libri.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM bookentity")
    fun getAll(): List<BookEntity>

    @Query("SELECT * FROM bookentity")
    fun getAllFlow(): Flow<List<BookEntity>>

    @Query("SELECT * FROM bookentity WHERE id = :id")
    fun getById(id: String): BookEntity?

    @Query("SELECT * FROM bookentity WHERE is_bookmarked = 1")
    fun getBookmarked(): Flow<List<BookEntity>>

    @Upsert
    suspend fun insertBook(book: BookEntity)
}