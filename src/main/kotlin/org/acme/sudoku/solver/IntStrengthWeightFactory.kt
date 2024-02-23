package org.acme.sudoku.solver

import org.acme.sudoku.domain.Board
import org.apache.commons.lang3.builder.CompareToBuilder
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory
import kotlin.random.Random

class IntStrengthWeightFactory : SelectionSorterWeightFactory<Board, Int> {
    class IntStrengthWeight(private val int: Int, private val instanceCount: Int) : Comparable<IntStrengthWeight> {
        override fun compareTo(other: IntStrengthWeight): Int {
            return CompareToBuilder().append(other.instanceCount, instanceCount).append(int, other.int)
                .append(Random.nextInt(), Random.nextInt()).toComparison()
        }

    }

    private fun getInstanceCount(int: Int, board: Board): Int {
        return board.cellList.count { it.value == int }
    }

    override fun createSorterWeight(board: Board, int: Int): IntStrengthWeight {
        return IntStrengthWeight(int, getInstanceCount(int, board))
    }

}