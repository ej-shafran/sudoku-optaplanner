package org.acme.sudoku.domain

import org.acme.sudoku.solver.ValueStrengthWeightFactory
import org.optaplanner.core.api.domain.entity.PlanningEntity
import org.optaplanner.core.api.domain.entity.PlanningPin
import org.optaplanner.core.api.domain.lookup.PlanningId
import org.optaplanner.core.api.domain.variable.PlanningVariable
import kotlin.math.ceil

@PlanningEntity
class Cell(
    @PlanningId val id: Int,
    @PlanningVariable(strengthWeightFactoryClass = ValueStrengthWeightFactory::class) var value: Int?,
    @PlanningPin val pinned: Boolean
) {
    fun getRow(): Int = ceil((id.toDouble() / 9)).toInt()
    fun getCol(): Int = ((id - 1) % 9) + 1
    fun getBox(): Int = (getRow() - 1) / 3 * 3 + (getCol() - 1) / 3 + 1

    @Suppress("unused") // Needed for OptaPlanner
    constructor() : this(0, null, false)

    fun toPrintRepresentation(): String {
        val baseRepresentation = value?.toString() ?: " "
        return if (pinned) "\u001b[1m${baseRepresentation}\u001b[0m" else baseRepresentation
    }

    override fun toString(): String {
        return "Cell($id) = $value"
    }
}
