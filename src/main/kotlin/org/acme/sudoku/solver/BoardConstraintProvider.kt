package org.acme.sudoku.solver

import org.acme.sudoku.domain.FillableCell
import org.acme.sudoku.domain.PrefilledCell
import org.optaplanner.core.api.score.buildin.simple.SimpleScore
import org.optaplanner.core.api.score.stream.Constraint
import org.optaplanner.core.api.score.stream.ConstraintFactory
import org.optaplanner.core.api.score.stream.ConstraintProvider
import org.optaplanner.core.api.score.stream.Joiners

class BoardConstraintProvider : ConstraintProvider {
    override fun defineConstraints(constraintFactory: ConstraintFactory): Array<Constraint> {
        return rowConflict(constraintFactory) + colConflict(constraintFactory) + boxConflict(constraintFactory)
    }

    private fun boxConflict(constraintFactory: ConstraintFactory): Array<Constraint> {
        return arrayOf(
            constraintFactory.forEach(FillableCell::class.java).join(PrefilledCell::class.java,
                Joiners.filtering { a, b -> a.value == b.value },
                Joiners.filtering { a, b -> a.getBox() == b.getBox() },
                Joiners.filtering { a, b -> a.id != b.id }).penalize(SimpleScore.ONE).asConstraint("Box(Prefilled)"),

            constraintFactory.forEach(FillableCell::class.java).join(FillableCell::class.java,
                Joiners.filtering { a, b -> a.value == b.value },
                Joiners.filtering { a, b -> a.getBox() == b.getBox() },
                Joiners.filtering { a, b -> a.id != b.id }).penalize(SimpleScore.ONE).asConstraint("Box")
        )
    }

    private fun colConflict(constraintFactory: ConstraintFactory): Array<Constraint> {
        return arrayOf(
            constraintFactory.forEach(FillableCell::class.java).join(PrefilledCell::class.java,
                Joiners.filtering { a, b -> a.value == b.value },
                Joiners.filtering { a, b -> a.getCol() == b.getCol() },
                Joiners.filtering { a, b -> a.id != b.id }).penalize(SimpleScore.ONE).asConstraint("Col(Prefilled)"),

            constraintFactory.forEach(FillableCell::class.java).join(FillableCell::class.java,
                Joiners.filtering { a, b -> a.value == b.value },
                Joiners.filtering { a, b -> a.getCol() == b.getCol() },
                Joiners.filtering { a, b -> a.id != b.id }).penalize(SimpleScore.ONE).asConstraint("Col")
        )
    }


    private fun rowConflict(constraintFactory: ConstraintFactory): Array<Constraint> {
        return arrayOf(
            constraintFactory.forEach(FillableCell::class.java).join(PrefilledCell::class.java,
                Joiners.filtering { a, b -> a.value == b.value },
                Joiners.filtering { a, b -> a.getRow() == b.getRow() },
                Joiners.filtering { a, b -> a.id != b.id }).penalize(SimpleScore.ONE).asConstraint("Row(Prefilled)"),

            constraintFactory.forEach(FillableCell::class.java).join(FillableCell::class.java,
                Joiners.filtering { a, b -> a.value == b.value },
                Joiners.filtering { a, b -> a.getRow() == b.getRow() },
                Joiners.filtering { a, b -> a.id != b.id }).penalize(SimpleScore.ONE).asConstraint("Row")
        )
    }

}