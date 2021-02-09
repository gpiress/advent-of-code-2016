package day_8

import java.io.File


fun firstPart(commands: List<Command>) {
    val tinyScreen = TinyScreen(50, 6)

    commands
        .forEach { command ->
            run {
                when (command.commandType) {
                    CommandType.RECT -> tinyScreen.addRect(command.firstValue, command.secondValue)
                    CommandType.ROTATE_ROW -> tinyScreen.rotateRow(command.firstValue, command.secondValue)
                    CommandType.ROTATE_COL -> tinyScreen.rotateCol(command.firstValue, command.secondValue)
                    CommandType.NOOP -> {}
                }

//                println("")
//                println("---")
//                println("")
//                println(tinyScreen)
            }
        }

    println("First part: ${tinyScreen.getLitPixels()}")
    println("Second part: ")
    println(tinyScreen)
}

fun processInput(): List<String> {
    val filePath = "D:\\JavaProjects\\AdventOfCode\\src\\main\\kotlin\\day_8\\input.in"
    //val filePath = "D:\\JavaProjects\\AdventOfCode\\src\\main\\kotlin\\day_8\\test.in"
    return File(filePath).readLines(Charsets.UTF_8)
}

fun main(args: Array<String>) {
    val input = processInput().map(::parseCommand)

    firstPart(input)
}

enum class CommandType { RECT, ROTATE_ROW, ROTATE_COL, NOOP }
class Command(val commandType: CommandType, val firstValue: Int, val secondValue: Int)

class TinyScreen(private val width: Int, private val height: Int) {
    private val pixels: MutableList<String> = MutableList(height) { ".".repeat(width) }

    fun addRect(w: Int, h: Int) {
        pixels
            .take(h)
            .forEachIndexed { index, oldLine -> run {
                val toKeep = oldLine.substring(w)
                val activePixels = "#".repeat(w)
                pixels[index] = "$activePixels$toKeep"
            } }
    }

    fun rotateCol(col: Int, by: Int) {
        val columnToRotate: List<Char> = pixels.map { it[col] }
        val newColumn = MutableList(columnToRotate.size) { '.' }

        columnToRotate.forEachIndexed { index, c ->
            run {
                val newIndex = (index + by) % columnToRotate.size
                newColumn[newIndex] = c
            }
        }

        pixels
            .forEachIndexed { index, oldLine -> run {
                val newLine =
                    "${oldLine.substring(0, col)}${newColumn[index]}${oldLine.substring(col + 1)}"

                pixels[index] = newLine
            } }
    }

    fun rotateRow(row: Int, by: Int) {
        val rowToRotate: String = pixels[row]
        val newRow = MutableList(rowToRotate.length) { '.' }

        rowToRotate.forEachIndexed { index, c ->
            run {
                val newIndex = (index + by) % rowToRotate.length
                newRow[newIndex] = c
            }
        }

        pixels[row] = newRow.joinToString("")
    }

    fun getLitPixels(): Int {
        return pixels.sumOf { line -> line.count { c -> c.equals('#', false) } }
    }

    override fun toString(): String {
        return pixels.joinToString("\n")
    }
}

fun parseCommand(command: String): Command {
    if (command.startsWith("rect")) {
        // "rect AxB"
        val splitNumbers = command.split(" ")[1].split("x")
        val first = splitNumbers[0].trim().toInt(10)
        val second = splitNumbers[1].trim().toInt(10)

        return Command(CommandType.RECT, first, second)
    }

    if (command.contains("row")) {
        // "rotate row y=A by B"
        val splitNumbers = command.substringAfter("=").split(" by ")
        val first = splitNumbers[0].trim().toInt(10)
        val second = splitNumbers[1].trim().toInt(10)

        return Command(CommandType.ROTATE_ROW, first, second)
    }

    if (command.contains("column")) {
        // "rotate column x=A by B"
        val splitNumbers = command.substringAfter("=").split(" by ")
        val first = splitNumbers[0].trim().toInt(10)
        val second = splitNumbers[1].trim().toInt(10)

        return Command(CommandType.ROTATE_COL, first, second)
    }

    return Command(CommandType.NOOP, -1, -1)
}
