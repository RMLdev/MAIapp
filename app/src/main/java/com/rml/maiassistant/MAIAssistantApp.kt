package com.rml.maiassistant

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rml.maiassistant.data.database.MAIDatabase

class MAIAssistantApp : Application() {

    override fun onCreate() {
        instance = this
        database = Room
            .databaseBuilder(instance, MAIDatabase::class.java, "MAIDatabase")
            .build()
        super.onCreate()
    }
    companion object {
        lateinit var instance: MAIAssistantApp
        lateinit var database: MAIDatabase
    }
}
