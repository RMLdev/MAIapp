package com.rml.maiassistant.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rml.maiassistant.repo.selectionrepository.SelectionRepository
import com.rml.maiassistant.ui.GroupsSelectionFragment
import com.rml.maiassistant.ui.adapter.SelectionRecyclerAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class SelectionViewModel : MAIViewModel(), SelectionRecyclerAdapter.EventInRecyclerListener, GroupsSelectionFragment.OnBackInGroupsFragmentListener {

    private val _departmentsStateLiveData: MutableLiveData<SelectionState> = MutableLiveData()
    private val _groupsStateLiveData: MutableLiveData<SelectionState> = MutableLiveData()
    private val _loadingStateLiveData: MutableLiveData<Boolean> = MutableLiveData()

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

    private fun showLoading() {
        _loadingStateLiveData.value = true
        if (_groupsStateLiveData.value != null) {
            val value = _groupsStateLiveData.value!!.getCellsList()!!
            val newValue : MutableList<SelectionCell.GroupsSelectionCell> = mutableListOf()
            for (item in  value) {
                item as SelectionCell.GroupsSelectionCell
                newValue.add(item.setEnabled(false))
            }
            _groupsStateLiveData.value = _groupsStateLiveData.value!!.setCellsList(newValue)
        }
        if (_departmentsStateLiveData.value != null) {
            val value = _departmentsStateLiveData.value!!.getCellsList()!!
            val newValue : MutableList<SelectionCell.DepartmentSelectionCell> = mutableListOf()
            for (item in  value) {
                item as SelectionCell.DepartmentSelectionCell
                newValue.add(item.setEnabled(false))
            }
            _departmentsStateLiveData.value = _departmentsStateLiveData.value!!.setCellsList(newValue)
        }
    }

    private fun hideLoading() {
        _loadingStateLiveData.value = false
        if (_groupsStateLiveData.value != null) {
            val value = _groupsStateLiveData.value!!.getCellsList()!!
            val newValue : MutableList<SelectionCell.GroupsSelectionCell> = mutableListOf()
            for (item in  value) {
                item as SelectionCell.GroupsSelectionCell
                newValue.add(item.setEnabled(true))
            }
            _groupsStateLiveData.value = _groupsStateLiveData.value!!.setCellsList(newValue)
        }
        if (_departmentsStateLiveData.value != null) {
            val value = _departmentsStateLiveData.value!!.getCellsList()!!
            val newValue : MutableList<SelectionCell.DepartmentSelectionCell> = mutableListOf()
            for (item in  value) {
                item as SelectionCell.DepartmentSelectionCell
                newValue.add(item.setEnabled(true))
            }
            _departmentsStateLiveData.value = _departmentsStateLiveData.value!!.setCellsList(newValue)
        }
    }

    fun getGroupsState(): LiveData<SelectionState> = _groupsStateLiveData

    override fun onBackPressed() {
        _groupsStateLiveData.value = _groupsStateLiveData.value?.setFragmentType(SelectionState.DEPARTMENTS_FRAGMENT)
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

    override fun onDepartmentClick(courseId: Int, departmentId: Int) {
        loadGroupsStateLiveData(departmentId, courseId)
    }

    override fun onGroupClick(groupName: String) {
        compositeDisposable.add(SelectionRepository().setUserGroup(groupName))
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
            }
        }
    }
}