package me.alberto.pdfreader.framework.db

import androidx.room.*

@Dao
interface DocumentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDocument(document: DocumentEntity)

    @Query("SELECT * FROM document")
    suspend fun getDocuments(): List<DocumentEntity>

    @Delete
    suspend fun removeDocument(document: DocumentEntity)
}
