package com.rml.maiassistant.model

import android.graphics.Bitmap

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

    data class GroupsSelectionCell(
        private val specialisationType: String,
        private val groupsList: List<String>,
        private val expanded: Boolean = false
    ) : SelectionCell() {

        fun getSpecialisation(): String = specialisationType

        fun getGroupsList(): List<String> = groupsList

        fun isExpanded(): Boolean = expanded

        fun setExpanded(isExpanded: Boolean): GroupsSelectionCell =
            GroupsSelectionCell(this.specialisationType, this.groupsList, isExpanded)
    }


}
