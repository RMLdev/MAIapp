package com.rml.maiassistant.model

data class SelectionState(
    private val cellsList: List<SelectionCell>,
    private val changesPosition: Int?
) {
    fun getCellsList(): List<SelectionCell>? = cellsList

    fun setCellsList(newList : List<SelectionCell>): SelectionState = SelectionState(newList, this.changesPosition)

    fun getChangesPosition(): Int? = changesPosition

    fun setChangesPosition(position: Int): SelectionState = SelectionState(this.cellsList, position)
}