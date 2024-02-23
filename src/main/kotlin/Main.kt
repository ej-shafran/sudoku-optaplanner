import org.acme.sudoku.domain.Board
import org.acme.sudoku.domain.Cell
import org.acme.sudoku.solver.BoardConstraintProvider
import org.optaplanner.core.api.solver.SolverFactory
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicPhaseConfig
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicType
import org.optaplanner.core.config.localsearch.LocalSearchPhaseConfig
import org.optaplanner.core.config.localsearch.LocalSearchType
import org.optaplanner.core.config.solver.SolverConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration
import kotlin.streams.asSequence

val LOGGER: Logger = LoggerFactory.getLogger("Sudoku App")

fun generateFromString(problem: String): Board {
    val cellList = problem.chars().asSequence().mapIndexed { i, c ->
        val char = c.toChar()
        if (char == '.') {
            return@mapIndexed Cell(i, null, false)
        } else {
            return@mapIndexed Cell(i, char.digitToInt(), true)
        }
    }.toList()
    return Board(cellList)
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
            .withConstraintProviderClass(BoardConstraintProvider::class.java)
            .withTerminationSpentLimit(Duration.ofSeconds(30)).withPhases(
                ConstructionHeuristicPhaseConfig().withConstructionHeuristicType(ConstructionHeuristicType.FIRST_FIT),
                LocalSearchPhaseConfig().withLocalSearchType(LocalSearchType.LATE_ACCEPTANCE)
            )
    )

    val problem = generateDemoData()
    printBoard(problem)
    val solution = solverFactory.buildSolver().solve(problem)
    printBoard(solution)
}

fun printBoard(board: Board) {
    val cellList = board.cellList

    val middleRow = "╠═══════════════╬═══════════════╬═══════════════╣"

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

        if (i == 2 || i == 5) LOGGER.info(middleRow)
    }
    LOGGER.info("╚═══════════════╩═══════════════╩═══════════════╝")
}