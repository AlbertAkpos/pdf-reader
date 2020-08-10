package me.alberto.pdfreader.framework.db

import androidx.room.*


@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmark(bookmark: BookmarkEntity)

    @Query("SELECT * FROM bookmark WHERE documentUri = :documentUri")
    suspend fun getBookmarks(documentUri: String): List<BookmarkEntity>

    @Delete
    suspend fun removeBookmark(bookmark: BookmarkEntity)
}