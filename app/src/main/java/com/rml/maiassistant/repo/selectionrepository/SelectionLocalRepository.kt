package com.rml.maiassistant.repo.selectionrepository

import android.util.Log
import com.rml.maiassistant.MAIAssistantApp
import com.rml.maiassistant.data.database.MAIDatabase
import com.rml.maiassistant.model.GroupDatabaseEntity
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*


class SelectionLocalRepository {

    private val database: MAIDatabase = MAIAssistantApp.database

    fun setUserGroup(groupName: String): Disposable{
        val group = GroupDatabaseEntity(1, groupName)
        val backgroundThread = Schedulers.io().createWorker()
        return backgroundThread.schedule {
            database.groupDao.insertGroup(group)
            Log.d("aaaa", database.groupDao.getAllGroups()[0].toString())
        }
    }
}