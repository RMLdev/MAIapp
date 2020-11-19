package com.rml.maiassistant.model

data class SelectionState(
    private val cellsList: List<SelectionCell>,
    private val fragmentType: Int
) {
    fun getCellsList(): List<SelectionCell>? = cellsList

    fun setCellsList(newList : List<SelectionCell>): SelectionState = SelectionState(newList, this.fragmentType)

    fun getFragmentType() : Int = fragmentType

    fun setFragmentType(fragmentType: Int): SelectionState = SelectionState(this.cellsList, fragmentType)

    companion object {
        const val DEPARTMENTS_FRAGMENT = 0
        const val GROUPS_FRAGMENT = 1
    }
}