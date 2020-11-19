package com.rml.maiassistant.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rml.maiassistant.repo.selectionrepository.SelectionRepository
import com.rml.maiassistant.ui.adapter.SelectionRecyclerAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class SelectionViewModel : BaseViewModel(), SelectionRecyclerAdapter.EventInRecyclerListener {

    private val _departmentsStateLiveData: MutableLiveData<SelectionState> = MutableLiveData()
    private val _loadingStateLiveData: MutableLiveData<Boolean> = MutableLiveData()

    override fun onExpandChange(cell: SelectionCell, position: Int, isExpanded: Boolean) {
        cell as SelectionCell.DepartmentSelectionCell
        val oldState = _departmentsStateLiveData.value!!
        val list = oldState.getCellsList()!!.toMutableList()
        list[position] = cell.setExpanded(isExpanded)
        val newState = oldState.setCellsList(list).setChangesPosition(position)
        _departmentsStateLiveData.value = newState
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
                            val state = SelectionState(t, null)
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

    private fun showLoading() {
        _loadingStateLiveData.value = true
    }

    private fun hideLoading() {
        _loadingStateLiveData.value = false
    }
}