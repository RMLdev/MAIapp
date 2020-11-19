package com.rml.maiassistant.ui

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rml.maiassistant.R
import com.rml.maiassistant.model.SelectionCell
import com.rml.maiassistant.model.SelectionState
import com.rml.maiassistant.model.SelectionViewModel
import com.rml.maiassistant.ui.adapter.SelectionRecyclerAdapter

class DepartmentSelectionFragment : Fragment() {

    private lateinit var viewModel: SelectionViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(activity!!).get(SelectionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.department_selection_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.selection_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val loadingProgressBar: CardView = view.findViewById(R.id.loading)
        viewModel.getLoadingState().observe(viewLifecycleOwner, Observer<Boolean> { isLoading ->
            when (isLoading) {
                true -> {
                    loadingProgressBar.visibility = View.VISIBLE
                    recyclerView.isEnabled = false
                }
                false -> {
                    loadingProgressBar.visibility = View.GONE
                    recyclerView.isEnabled = true
                }
            }
        })
        viewModel.getDepartmentsState()
            .observe(viewLifecycleOwner, Observer<SelectionState?> { departmentsState ->
                if (departmentsState == null) {
                    return@Observer
                }
                val recyclerList: List<SelectionCell> = departmentsState.getCellsList()!!
                val adapter = recyclerView.adapter as SelectionRecyclerAdapter?
                if (adapter == null) {
                    val selectionRecyclerAdapter =
                        SelectionRecyclerAdapter(recyclerList, layoutInflater, viewModel)
                    recyclerView.adapter = selectionRecyclerAdapter
                } else {
                    recyclerView.adapter = adapter
                    adapter.update(recyclerList)
                }
            })

        viewModel.getGroupsState().observe(viewLifecycleOwner, Observer { groupsState ->
            if (groupsState == null) {
                return@Observer
            }
            if (groupsState.getFragmentType() == SelectionState.GROUPS_FRAGMENT) {
                activity!!
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.gs_fragment_container, GroupsSelectionFragment())
                    .addToBackStack(null)
                    .commit()
            }
        })
    }
}
