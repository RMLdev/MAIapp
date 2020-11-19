package com.rml.maiassistant.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rml.maiassistant.repo.selectionrepository.SelectionRepository
import com.rml.maiassistant.ui.GroupsSelectionFragment
import com.rml.maiassistant.ui.SelectionActivity
import com.rml.maiassistant.ui.adapter.SelectionRecyclerAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class SelectionViewModel : MAIViewModel(), SelectionRecyclerAdapter.EventInRecyclerListener, GroupsSelectionFragment.OnBackInGroupsFragmentListener {

    private val _departmentsStateLiveData: MutableLiveData<SelectionState> = MutableLiveData()
    private val _groupsStateLiveData: MutableLiveData<SelectionState> = MutableLiveData()
    private val _navigationLiveData: MutableLiveData<Int> = MutableLiveData()
    private val _loadingStateLiveData: MutableLiveData<Boolean> = MutableLiveData()

    override fun onDepartmentClick(courseId: Int, departmentId: Int) {
        loadGroupsStateLiveData(departmentId, courseId)
    }

    fun getGroupsState(): LiveData<SelectionState> = _groupsStateLiveData

    override fun onBackPressed() {
        _groupsStateLiveData.value = _groupsStateLiveData.value?.setFragmentType(SelectionState.DEPARTMENTS_FRAGMENT)
    }

    override fun onExpandChange(cell: SelectionCell, position: Int, isExpanded: Boolean) {
        when (cell) {
            is SelectionCell.DepartmentSelectionCell -> {
                val oldState = _departmentsStateLiveData.value!!
                val list = oldState.getCellsList()!!.toMutableList()
                list[position] = cell.setExpanded(isExpanded)
                val newState = oldState.setCellsList(list)
                _departmentsStateLiveData.value = newState
            }
            is SelectionCell.GroupsSelectionCell -> {
                val oldState = _groupsStateLiveData.value!!
                val list = oldState.getCellsList()!!.toMutableList()
                list[position] = cell.setExpanded(isExpanded)
                val newState = oldState.setCellsList(list)
                _groupsStateLiveData.value = newState
                Log.d("aaaaa", "click handled" )
            }
        }
    }

    fun getDepartmentsState(): LiveData<SelectionState> {
        if (_departmentsStateLiveData.value == null) {
            loadDepartmentsState()
        }
        return _departmentsStateLiveData
    }

    fun getLoadingState(): LiveData<Boolean> {
        if (_loadingStateLiveData.value == null) {
            _loadingStateLiveData.value = true
        }
        return _loadingStateLiveData
    }

    private fun loadDepartmentsState() {
        showLoading()
        compositeDisposable.add(SelectionRepository()
            .getDepartments()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<List<SelectionCell>>() {
                    override fun onComplete() {
                        hideLoading()
                    }

                    override fun onNext(t: List<SelectionCell>?) {
                        if (t!!.isNotEmpty()) {
                            val state = SelectionState(t, SelectionState.DEPARTMENTS_FRAGMENT)
                            _departmentsStateLiveData.value = state
                        }
                    }

                    override fun onError(e: Throwable?) {
                        throw e!!
                    }
                }
            )
        )
    }

    private fun loadGroupsStateLiveData(departmentId: Int, courseId: Int) {
        showLoading()
        compositeDisposable.add(SelectionRepository()
            .getGroups(departmentId, courseId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                object : DisposableObserver<List<SelectionCell>>() {
                    override fun onComplete() {
                        hideLoading()
                    }

                    override fun onNext(t: List<SelectionCell>?) {
                        if (t!!.isNotEmpty()) {
                            val state = SelectionState(t, SelectionState.GROUPS_FRAGMENT)
                            _groupsStateLiveData.value = state
                        }
                    }

                    override fun onError(e: Throwable?) {
                        throw e!!
                    }
                }
            )
        )
    }

    private fun showLoading() {
        _loadingStateLiveData.value = true
    }

    private fun hideLoading() {
        _loadingStateLiveData.value = false
    }
}