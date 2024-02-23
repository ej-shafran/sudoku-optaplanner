package org.acme.sudoku.solver

import org.acme.sudoku.domain.Cell
import org.optaplanner.core.api.score.buildin.simple.SimpleScore
import org.optaplanner.core.api.score.stream.Constraint
import org.optaplanner.core.api.score.stream.ConstraintFactory
import org.optaplanner.core.api.score.stream.ConstraintProvider
import org.optaplanner.core.api.score.stream.Joiners

class BoardConstraintProvider : ConstraintProvider {
    override fun defineConstraints(constraintFactory: ConstraintFactory): Array<Constraint> {
        return arrayOf(
            boxConflict(constraintFactory),
            rowConflict(constraintFactory),
            colConflict(constraintFactory),
        )
    }

    private fun boxConflict(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory.forEachUniquePair(
            Cell::class.java, Joiners.equal(Cell::getBox), Joiners.equal(Cell::value)
        ).penalize(SimpleScore.ONE).asConstraint("Box")
    }

    private fun rowConflict(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory.forEachUniquePair(
            Cell::class.java, Joiners.equal(Cell::getRow), Joiners.equal(Cell::value)
        ).penalize(SimpleScore.ONE).asConstraint("Row")
    }

    private fun colConflict(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory.forEachUniquePair(
            Cell::class.java, Joiners.equal(Cell::getCol), Joiners.equal(Cell::value)
        ).penalize(SimpleScore.ONE).asConstraint("Col")
    }
}