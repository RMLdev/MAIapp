package com.rml.maiassistant.data.database

import androidx.room.*
import com.rml.maiassistant.model.GroupDatabaseEntity

@Dao
interface GroupDao {

    @Query ("SELECT * FROM GroupDatabaseEntity")
    fun getAllGroups(): List<GroupDatabaseEntity>

    @Query ("SELECT * FROM GroupDatabaseEntity WHERE id = :id")
    fun getGroupById(id: Long): GroupDatabaseEntity

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertGroup(group: GroupDatabaseEntity)

    @Update
    fun updateGroup(group: GroupDatabaseEntity)

    @Delete
    fun deleteGroup(group: GroupDatabaseEntity)
}