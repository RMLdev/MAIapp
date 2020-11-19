package com.rml.maiassistant.repo.selectionrepository

import android.graphics.Bitmap
import android.util.Log
import com.rml.maiassistant.model.SelectionCell
import com.rml.maiassistant.model.SelectionNetworkEntity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class SelectionRepository {

    private val remoteRepository: SelectionRemoteRepository = SelectionRemoteRepository()
    private val localRepository: SelectionLocalRepository = SelectionLocalRepository()

    fun getDepartments(): Observable<List<SelectionCell>?> {
        val isConnected = true
        var networkList: MutableList<SelectionNetworkEntity> = mutableListOf()
        if (isConnected) {
            val obsrvbl = remoteRepository
                .getDepartments()
                .map {
                    val _cellsList: MutableList<SelectionCell> = mutableListOf()
                    it.iterator().forEach {
                        val courseId = it.name
                        val departmentsIdList = it.array
                        /* val imagesList: List<Bitmap> =
                             remoteRepository.getDepartmentsImages(departmentsIdList)*/
                        _cellsList.add(
                            SelectionCell.DepartmentSelectionCell(courseId, departmentsIdList, listOf())
                        )
                    }
                    return@map _cellsList.toList()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
            return obsrvbl
        }
       else throw Throwable()
    }

    fun getGroups(departmentId: Int, courseId: Int): Observable<List<SelectionCell>> {
        val isConnected = true
        if (isConnected) {
            val observableGroups = remoteRepository
                .getGroups(departmentId, courseId)
                .map { networkList ->
                    val _cellsList: MutableList<SelectionCell> = mutableListOf()
                    networkList.iterator().forEach { networkEntity->
                        val specialisationType = networkEntity.name
                        val groupsList = networkEntity.array
                        _cellsList.add(
                            SelectionCell.GroupsSelectionCell(specialisationType, groupsList)
                        )
                    }
                    return@map _cellsList.toList()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
            return observableGroups
        }
        else throw Throwable()
    }
}