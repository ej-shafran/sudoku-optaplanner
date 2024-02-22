import org.acme.sudoku.domain.Board
import org.acme.sudoku.domain.FillableCell
import org.acme.sudoku.domain.PrefilledCell
import org.acme.sudoku.solver.BoardConstraintProvider
import org.optaplanner.core.api.solver.SolverFactory
import org.optaplanner.core.config.solver.SolverConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.stream.IntStream
import kotlin.streams.asSequence

val LOGGER: Logger = LoggerFactory.getLogger("Sudoku App")

fun generateFromString(problem: String): Board {
    val prefilledCellList =
        problem.chars().asSequence().mapIndexed { i, c -> Pair(i + 1, c.toChar()) }.filter { it.second != '.' }
            .map { PrefilledCell(it.first, it.second.digitToInt()) }.toList()
    return Board(prefilledCellList)
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
        SolverConfig().withSolutionClass(Board::class.java).withEntityClasses(FillableCell::class.java)
            .withConstraintProviderClass(BoardConstraintProvider::class.java)
            .withTerminationSpentLimit(Duration.ofSeconds(30))
    )

    val problem = generateDemoData()
    printBoard(problem)
    val solution = solverFactory.buildSolver().solve(problem)
    printBoard(solution)
}

fun printBoard(board: Board) {
    val cellList = IntStream.rangeClosed(1, 81).mapToObj() { id ->
        board.prefilledCellList.find { cell -> cell.id == id }
            ?: board.fillableCellList.find { cell -> cell.id == id }!!
    }.toList()

    val middleRow = "╠═══════════════╬═══════════════╬═══════════════╣"

    LOGGER.info("╔═══════════════╦═══════════════╦═══════════════╗")
    for (i in 0 until 9) {
        LOGGER.info("║┌───┐┌───┐┌───┐║┌───┐┌───┐┌───┐║┌───┐┌───┐┌───┐║")

        var row = ""
        for (j in 0 until 9) {
            if ((j + 1) % 3 == 1) row += "║"
            val value = cellList[(i * 9) + j]?.value?.toString() ?: " "
            row += "│ $value │"
        }
        row += "║"
        LOGGER.info(row)

        LOGGER.info("║└───┘└───┘└───┘║└───┘└───┘└───┘║└───┘└───┘└───┘║")

        if (i == 2 || i == 5) LOGGER.info(middleRow)
    }
    LOGGER.info("╚═══════════════╩═══════════════╩═══════════════╝")
}