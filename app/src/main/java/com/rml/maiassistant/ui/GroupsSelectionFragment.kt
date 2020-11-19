package com.rml.maiassistant.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rml.maiassistant.R
import com.rml.maiassistant.model.SelectionCell
import com.rml.maiassistant.model.SelectionState
import com.rml.maiassistant.model.SelectionViewModel
import com.rml.maiassistant.ui.adapter.SelectionRecyclerAdapter

class GroupsSelectionFragment : Fragment() {
    private lateinit var viewModel: SelectionViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(activity!!).get(SelectionViewModel::class.java)
        activity!!.supportFragmentManager.addOnBackStackChangedListener {
            viewModel.onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.groups_selection_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.groups_selection_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val loadingProgressBar: CardView = view.findViewById(R.id.loading)
        viewModel.getLoadingState().observe(viewLifecycleOwner, Observer<Boolean> { isLoading ->
            when (isLoading) {
                true -> {
                    loadingProgressBar.visibility = View.VISIBLE
                }
                false -> {
                    loadingProgressBar.visibility = View.GONE
                }
            }
        })
        viewModel.getGroupsState().observe(viewLifecycleOwner, Observer {groupsState ->
            if (groupsState == null) {
                return@Observer
            }
            val recyclerList: List<SelectionCell> = groupsState.getCellsList()!!
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
    }

    interface OnBackInGroupsFragmentListener {
        fun onBackPressed()
    }
}