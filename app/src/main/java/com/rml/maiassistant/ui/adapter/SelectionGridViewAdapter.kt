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

class SelectionGridViewAdapter(
    private val choicesList: List<String>,
    private val imagesList: List<Bitmap>,
    private val inflater: LayoutInflater
) : BaseAdapter() {

    private lateinit var viewHolder: SelectionGridViewHolder

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.selection_grid_cell, parent, false)
            val imageView: ImageView = view.findViewById(R.id.grid_cell_image)
            val textView: TextView = view.findViewById(R.id.grid_cell_text)
            viewHolder = SelectionGridViewHolder(imageView, textView)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as SelectionGridViewHolder
        }
        val departmentName = "Институт №" + choicesList[position]
        viewHolder.textView.text = departmentName
        //TODO: Replace in SelectionRemoteRepository
        val labelId = when(choicesList[position].toInt()){
            6 -> "logos/6"
            8 -> "logos/8"
            else -> "logos_2020/${choicesList[position]}"
        }
        Picasso.get().load("http://files.mai.ru/site/life/brand/$labelId.jpg").into(viewHolder.imageView)
        return view!!
    }

    override fun getItem(position: Int): Any = choicesList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = choicesList.size

    class SelectionGridViewHolder(val imageView: ImageView, val textView: TextView)
}