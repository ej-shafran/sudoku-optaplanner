package org.acme.sudoku.domain

import org.optaplanner.core.api.domain.entity.PlanningEntity
import org.optaplanner.core.api.domain.variable.PlanningVariable
import kotlin.math.ceil

open class Cell(val id: Int, open var value: Int?) {
    fun getRow(): Int = ceil((id.toDouble() / 9)).toInt()
    fun getCol(): Int = ((id - 1) % 9) + 1
    fun getBox(): Int = (getRow() - 1) / 3 * 3 + (getCol() - 1) / 3 + 1

    open fun toPrintRepresentation(): String = value?.toString() ?: " "

    override fun toString(): String {
        return "Cell($id) = $value"
    }
}

class PrefilledCell(id: Int, value: Int) : Cell(id, value) {
    override fun toPrintRepresentation(): String {
        return "\u001b[1m${super.toPrintRepresentation()}\u001b[0m"
    }
}

@PlanningEntity
class FillableCell(id: Int) : Cell(id, null) {
    @PlanningVariable
    override var value: Int? = null

    @Suppress("unused") // Needed for OptaPlanner
    constructor() : this(0)
}