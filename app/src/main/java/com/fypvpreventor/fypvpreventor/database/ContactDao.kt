package com.fypvpreventor.VpreventorFYP.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(contact: Contacts)

    @Update
    suspend fun update(contact: Contacts)

    @Delete
    suspend fun delete(contact : Contacts)

    @Query("SELECT * FROM contacts")
    fun getAll() : Flow<List<Contacts>>

    @Query("SELECT * from contacts WHERE id = :id")
    fun getContact(id: Int) : Flow<Contacts>

}