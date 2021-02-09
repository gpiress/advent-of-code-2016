package day_2

import java.io.File


fun runOneMove(previous: Int, move: Char): Int {
    if (move.equals('U', true)) {
        if (previous <= 3) {
            return previous
        }

        return previous - 3
    }

    if (move.equals('D', true)) {
        if (previous >= 7) {
            return previous
        }

        return previous + 3
    }

    if (move.equals('L', true)) {
        if (previous % 3 == 1) {
            return previous
        }

        return previous - 1
    }

    if (move.equals('R', true)) {
        if (previous % 3 == 0) {
            return previous
        }

        return previous + 1
    }

    return previous
}

fun runOneMoveWeirdKeypad(previous: Char, move: Char): Char {
    if (move.equals('U', true)) {
        val ignored = listOf('5', '9', '2', '4', '1')
        if (ignored.contains(previous)) {
            return previous
        }

        if (previous == '3') {
            return '1'
        }

        if (previous == 'D') {
            return 'B'
        }

        val previousAsInt = previous.toString().toInt(16)
        return (previousAsInt - 4).toString(16).toUpperCase()[0]
    }

    if (move.equals('D', true)) {
        val ignored = listOf('5', '9', 'A', 'C', 'D')
        if (ignored.contains(previous)) {
            return previous
        }

        if (previous == '1') {
            return '3'
        }

        if (previous == 'B') {
            return 'D'
        }

        val previousAsInt = previous.toString().toInt(16)
        return (previousAsInt + 4).toString(16).toUpperCase()[0]
    }

    if (move.equals('L', true)) {
        val ignored = listOf('5', '2', '1', 'A', 'D')
        if (ignored.contains(previous)) {
            return previous
        }

        val previousAsInt = previous.toString().toInt(16)
        return (previousAsInt - 1).toString(16).toUpperCase()[0]
    }

    if (move.equals('R', true)) {
        val ignored = listOf('1', '4', '9', 'C', 'D')
        if (ignored.contains(previous)) {
            return previous
        }

        val previousAsInt = previous.toString().toInt(16)
        return (previousAsInt + 1).toString(16).toUpperCase()[0]
    }

    return previous
}

fun findNewDigit(previous: Int, moves: String): Int {
    return moves.fold(previous, { currentDigit, move -> runOneMove(currentDigit, move) })
}

fun findNewDigitWeirdKeypad(previous: Char, moves: String): Char {
    return moves.fold(previous, { currentDigit, move -> runOneMoveWeirdKeypad(currentDigit, move) })
}

fun firstPart(moves: List<String>) {
    var previousDigit = 5

    val result = moves.fold("", { doorCode, move ->
        run {
            previousDigit = findNewDigit(previousDigit, move)
            "$doorCode$previousDigit"
        }
    })

    println("The first door code to the bathroom is: $result")
}

fun secondPart(moves: List<String>) {
    var previousDigit = '5'

    val result = moves.fold("", { doorCode, move ->
        run {
            previousDigit = findNewDigitWeirdKeypad(previousDigit, move)
            "$doorCode$previousDigit"
        }
    })

    println("The second door code to the bathroom is: $result")
}

fun processInput(): List<String> {
    val filePath = "D:\\JavaProjects\\AdventOfCode\\src\\main\\kotlin\\day_2\\input.in"
    //val filePath = "D:\\JavaProjects\\AdventOfCode\\src\\main\\kotlin\\day_2\\test.in"
    return File(filePath).readLines(Charsets.UTF_8)
}

fun main(args: Array<String>) {
    val moves = processInput()

    firstPart(moves)
    secondPart(moves)
}