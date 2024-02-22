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
    @ValueRangeProvider @ProblemFactCollectionProperty val prefilledCellList: List<PrefilledCell>,
) {
    @Suppress("unused") // Needed for OptaPlanner
    @ValueRangeProvider
    @ProblemFactCollectionProperty
    val intList: List<Int> = IntStream.rangeClosed(1, 81).map { (it % 9) + 1 }.toList()

    @PlanningEntityCollectionProperty
    val fillableCellList: List<FillableCell> =
        IntStream.rangeClosed(1, 81).filter { id -> !prefilledCellList.any { cell -> cell.id == id } }
            .mapToObj { FillableCell(it) }.toList()

    @Suppress("unused") // Needed for OptaPlanner
    @PlanningScore
    lateinit var score: SimpleScore

    @Suppress("unused") // Needed for OptaPlanner
    constructor() : this(listOf())
}