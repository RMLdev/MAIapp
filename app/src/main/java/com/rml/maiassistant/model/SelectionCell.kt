package com.rml.maiassistant.model

import android.graphics.Bitmap
import java.lang.IllegalArgumentException

sealed class SelectionCell {

    data class DepartmentSelectionCell(
        private val courseNumber: String,
        private val departmentsList: List<String>,
        private val imagesList: List<Bitmap>,
        private val expanded: Boolean = false
    ) : SelectionCell() {

        fun getCourseNumber(): Int = courseNumber.toInt()

        fun getDepartmentList(): List<String> = departmentsList

        fun getImagesList(): List<Bitmap> = imagesList

        fun isExpanded(): Boolean = expanded

        fun setExpanded(isExpanded: Boolean): DepartmentSelectionCell =
            DepartmentSelectionCell(this.courseNumber, this.departmentsList, this.imagesList, isExpanded)
    }

    data class GroupSelectionCell(
        private val specialisationType: String,
        private val groupsList: List<String>
    ) : SelectionCell()
}
