package org.acme.sudoku.solver

import org.acme.sudoku.domain.Board
import org.acme.sudoku.domain.FillableCell
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory
import org.apache.commons.lang3.builder.CompareToBuilder

class FillableCellDifficultyWeightFactory : SelectionSorterWeightFactory<Board, FillableCell> {
    class FillableCellDifficultyWeight(private val fillableCell: FillableCell, private val optionCount: Int) :
        Comparable<FillableCellDifficultyWeight> {
        override fun compareTo(other: FillableCellDifficultyWeight): Int {
            return CompareToBuilder().append(other.optionCount, optionCount)
                .append(fillableCell.id, other.fillableCell.id).toComparison()
        }

    }

    private fun getOptionCount(board: Board, cell: FillableCell): Int {
        val row = board.cellList.filter { it.value != null && it.getRow() == cell.getRow() }.map { it.value!! }
        val col = board.cellList.filter { it.value != null && it.getCol() == cell.getCol() }.map { it.value!! }
        val box = board.cellList.filter { it.value != null && it.getBox() == cell.getBox() }.map { it.value!! }
        return 9 - HashSet<Int>(row + col + box).size
    }

    override fun createSorterWeight(board: Board, fillableCell: FillableCell): FillableCellDifficultyWeight {
        return FillableCellDifficultyWeight(fillableCell, getOptionCount(board, fillableCell))
    }
}