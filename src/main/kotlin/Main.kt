import org.acme.sudoku.domain.Board
import org.acme.sudoku.domain.Cell
import org.acme.sudoku.solver.BoardConstraintProvider
import org.optaplanner.core.api.solver.SolverFactory
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicPhaseConfig
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicType
import org.optaplanner.core.config.localsearch.LocalSearchPhaseConfig
import org.optaplanner.core.config.localsearch.decider.forager.LocalSearchForagerConfig
import org.optaplanner.core.config.solver.SolverConfig
import org.optaplanner.core.config.solver.termination.TerminationConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration

val LOGGER: Logger = LoggerFactory.getLogger("Sudoku App")

fun generateFromString(problem: String): Board {
    return Board((0..8).flatMap { i ->
        (0..8).map { j ->
            val id = (i * 9) + j
            val char = problem[id]
            return@map if (char == '.') {
                Cell(id, null, false, i + 1, j + 1)
            } else {
                Cell(id, char.digitToInt(), true, i + 1, j + 1)
            }
        }
    })
}

fun generateDemoData(): Board {
    return generateFromString(buildString {
        append("..32....6")
        append("....4..9.")
        append("1.2...5..")
        append("7...29...")
        append(".4.3.7.5.")
        append("...81...2")
        append("..1...8.3")
        append(".2..8....")
        append("9....46..")
    })
}

fun main() {
    val solverFactory = SolverFactory.create<Board>(
        SolverConfig().withSolutionClass(Board::class.java).withEntityClasses(Cell::class.java)
            .withConstraintProviderClass(BoardConstraintProvider::class.java).withTerminationConfig(
                TerminationConfig().withSpentLimit(Duration.ofSeconds(30)).withBestScoreLimit("0")
            ).withPhases(
                ConstructionHeuristicPhaseConfig().withConstructionHeuristicType(ConstructionHeuristicType.WEAKEST_FIT),
                LocalSearchPhaseConfig().withForagerConfig(LocalSearchForagerConfig().withAcceptedCountLimit(300))
            )
    )

    val problem = generateDemoData()
    printBoard(problem)
    val solution = solverFactory.buildSolver().solve(problem)
    printBoard(solution)
}

fun printBoard(board: Board) {
    val cellList = board.cellList

    LOGGER.info("╔═══════════════╦═══════════════╦═══════════════╗")
    for (i in 0 until 9) {
        LOGGER.info("║┌───┐┌───┐┌───┐║┌───┐┌───┐┌───┐║┌───┐┌───┐┌───┐║")

        var row = ""
        for (j in 0 until 9) {
            if ((j + 1) % 3 == 1) row += "║"
            val value = cellList[(i * 9) + j].toPrintRepresentation()
            row += "│ $value │"
        }
        row += "║"
        LOGGER.info(row)

        LOGGER.info("║└───┘└───┘└───┘║└───┘└───┘└───┘║└───┘└───┘└───┘║")

        if (i == 2 || i == 5) LOGGER.info("╠═══════════════╬═══════════════╬═══════════════╣")
    }
    LOGGER.info("╚═══════════════╩═══════════════╩═══════════════╝")
}
