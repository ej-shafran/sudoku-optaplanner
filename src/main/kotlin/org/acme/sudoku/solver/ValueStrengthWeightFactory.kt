package org.acme.sudoku.solver

import org.acme.sudoku.domain.Board
import org.apache.commons.lang3.builder.CompareToBuilder
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory
import kotlin.random.Random

class ValueStrengthWeightFactory : SelectionSorterWeightFactory<Board, Int> {
    class ValueStrengthWeight(private val value: Int, private val count: Int) : Comparable<ValueStrengthWeight> {
        override fun compareTo(other: ValueStrengthWeight): Int {
            return CompareToBuilder().append(other.count, count).append(
                value, other.value
            ).append(Random.nextInt(), Random.nextInt()).toComparison()
        }

    }

    override fun createSorterWeight(board: Board, value: Int): ValueStrengthWeight {
        return ValueStrengthWeight(value, board.cellList.count { it.value == value })
    }
}