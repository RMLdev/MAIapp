package com.rml.maiassistant.ui.adapter

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.rml.maiapp.wrappingViews.WrappingGridView
import com.rml.maiassistant.R
import com.rml.maiassistant.model.SelectionCell
import java.lang.IllegalArgumentException

class SelectionRecyclerAdapter(
    private var cells: List<SelectionCell>,
    private val layoutInflater: LayoutInflater,
    private val listener: EventInRecyclerListener
) : RecyclerView.Adapter<SelectionRecyclerAdapter.AbsSelectionViewHolder>() {

    private enum class ViewType {
        DEPARTMENT_SELECTION,
        GROUP_SELECTION
    }

    private val SelectionCell.viewType: ViewType
        get() = when (this) {
            is SelectionCell.DepartmentSelectionCell -> ViewType.DEPARTMENT_SELECTION
            is SelectionCell.GroupSelectionCell -> ViewType.GROUP_SELECTION
            else -> throw IllegalArgumentException("Unknown view type")
        }

    private val viewTypeValues = ViewType.values()

    fun update(newCells: List<SelectionCell>, inPosition: Int?) {
        cells = newCells
        if (inPosition == null) {
            notifyDataSetChanged()
        } else {
            Log.d("aaaaa", "itemChanged in position $inPosition")
            notifyItemChanged(inPosition)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewTypeOrdinal: Int
    ): AbsSelectionViewHolder = when (viewTypeValues[viewTypeOrdinal]) {
        ViewType.DEPARTMENT_SELECTION -> DepartmentSelectionViewHolder(
            layoutInflater,
            parent,
            listener
        )
        /*  ViewType.GROUP_SELECTION -> GroupSelectionViewHolder(
              layoutInflater,
              parent
          )*/
        else -> throw Throwable()
    }

    override fun getItemCount(): Int = cells.size

    override fun getItemViewType(position: Int): Int = cells[position].viewType.ordinal


    override fun onBindViewHolder(holder: AbsSelectionViewHolder, position: Int) {
        holder.bind(cells[position])
    }

    abstract class AbsSelectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(cell: SelectionCell)
    }

    class DepartmentSelectionViewHolder(
        private val inflater: LayoutInflater,
        private val parent: ViewGroup,
        private val listener: EventInRecyclerListener
    ) : AbsSelectionViewHolder(
        inflater.inflate(
            R.layout.department_selection_recycler_cell, parent, false
        )
    ) {

        override fun bind(cell: SelectionCell) {
            cell as SelectionCell.DepartmentSelectionCell
            val departmentsExpandableView: ConstraintLayout =
                itemView.findViewById(R.id.expandableView)
            departmentsExpandableView.visibility = when (cell.isExpanded()) {
                true -> View.VISIBLE
                false -> View.GONE
            }
            val departments: WrappingGridView = itemView.findViewById(R.id.grid_view)
            val course: TextView = itemView.findViewById(R.id.title_text)
            val courseNumber =
                parent.context.resources.getString(R.string.course_number) + " " + cell.getCourseNumber()
            val departmentsList = cell.getDepartmentList()
            val departmentsLabelsList: List<Bitmap> = cell.getImagesList()
            course.text = courseNumber
            course.setOnClickListener {
                listener.onExpandChange(cell, adapterPosition, !cell.isExpanded())
            }
            val adapter = SelectionGridViewAdapter(departmentsList, departmentsLabelsList, inflater)
            departments.adapter = adapter
        }
    }

    interface EventInRecyclerListener {
        fun onExpandChange(cell: SelectionCell, position: Int, isExpanded: Boolean)
    }
}