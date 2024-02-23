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
    val intList: List<Int> = IntStream.rangeClosed(1, 81).map { (it % 9) + 1 }.toList()

    @Suppress("unused") // Needed for OptaPlanner
    @PlanningScore
    lateinit var score: SimpleScore

    @Suppress("unused") // Needed for OptaPlanner
    constructor() : this(listOf())
}