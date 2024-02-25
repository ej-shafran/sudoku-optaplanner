package org.acme.sudoku.domain

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty
import org.optaplanner.core.api.domain.solution.PlanningScore
import org.optaplanner.core.api.domain.solution.PlanningSolution
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider
import org.optaplanner.core.api.score.buildin.simple.SimpleScore
import java.util.stream.IntStream
import kotlin.streams.toList

@PlanningSolution
class Board(
    @PlanningEntityCollectionProperty val cellList: List<Cell>
) {
    @Suppress("unused") // Needed for OptaPlanner
    @ValueRangeProvider
    @ProblemFactCollectionProperty
    val intList: List<Int> = (1..9).shuffled()

    val cellList: MutableList<Cell>
        get() = IntStream.rangeClosed(1, 81).mapToObj() { id ->
            prefilledCellList.find { cell -> cell.id == id } ?: fillableCellList.find { cell -> cell.id == id }!!
        }.toList()

    @Suppress("unused") // Needed for OptaPlanner
    @PlanningScore
    lateinit var score: SimpleScore

    @Suppress("unused") // Needed for OptaPlanner
    constructor() : this(listOf())
}