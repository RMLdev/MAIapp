package com.rml.maiassistant.ui.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.rml.maiassistant.R
import com.squareup.picasso.Picasso
import java.lang.IllegalArgumentException

class SelectionGridViewAdapter(
    private val choicesList: List<String>,
    private val imagesList: List<Bitmap>,
    private val inflater: LayoutInflater,
    private val type: Int
) : BaseAdapter() {

    private lateinit var viewHolder: SelectionGridViewHolder

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.selection_grid_cell, parent, false)
            val imageView: ImageView = view.findViewById(R.id.grid_cell_image)
            val textView: TextView = view.findViewById(R.id.grid_cell_text)
            viewHolder = when (type) {
                DEPARTMENTS_GV_TYPE -> DepartmentsGridViewHolder(imageView, textView)
                GROUPS_GV_TYPE -> GroupsGridViewHolder(imageView, textView)
                else -> throw IllegalArgumentException("Unknown SelectionGridView type")
            }
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as SelectionGridViewHolder
        }
        when (type) {
            DEPARTMENTS_GV_TYPE -> {
                val departmentName = "Институт №" + choicesList[position]
                viewHolder.textView.text = departmentName
                //TODO: Replace in SelectionRemoteRepository
                val labelId = when (choicesList[position].toInt()) {
                    6 -> "logos/6"
                    8 -> "logos/8"
                    else -> "logos_2020/${choicesList[position]}"
                }
                Picasso.get().load("http://files.mai.ru/site/life/brand/$labelId.jpg")
                    .into(viewHolder.imageView)
            }
            GROUPS_GV_TYPE -> {
                viewHolder.textView.text = choicesList[position]
                //TODO: Replace in SelectionRemoteRepository

                Picasso.get().load("https://mai.ru/common/brand/mai.gif")
                    .into(viewHolder.imageView)
            }
        }

        return view!!
    }

    override fun getItem(position: Int): Any = choicesList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = choicesList.size

    abstract class SelectionGridViewHolder(val imageView: ImageView, val textView: TextView)

    class DepartmentsGridViewHolder(departmentLogo: ImageView, courseTittle: TextView) :
        SelectionGridViewHolder(departmentLogo, courseTittle)

    class GroupsGridViewHolder(groupsImage: ImageView, specialisationTittle: TextView) :
        SelectionGridViewHolder(groupsImage, specialisationTittle)

    companion object {
        const val DEPARTMENTS_GV_TYPE = 0
        const val GROUPS_GV_TYPE = 1
    }
}