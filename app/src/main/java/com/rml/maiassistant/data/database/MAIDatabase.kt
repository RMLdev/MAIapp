package com.rml.maiassistant.data.database

import androidx.room.Database

import androidx.room.RoomDatabase
import com.rml.maiassistant.model.GroupDatabaseEntity

@Database (entities = [GroupDatabaseEntity::class], version = 1)
abstract class MAIDatabase : RoomDatabase() {
    abstract val groupDao: GroupDao
}