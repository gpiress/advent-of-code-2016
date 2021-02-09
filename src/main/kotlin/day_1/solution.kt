package day_1

import java.io.File
import kotlin.math.abs
import kotlin.math.max

class Position(val direction: String = "N", val x: Int = 0, val y: Int = 0) {}

class Move(instruction: String) {
    val turnTo: String = instruction[0].toString()
    val distance = instruction.substring(1).trim().toInt()
}

fun newDirection(oldDirection: String, turnTo: String): String {
    if (oldDirection.equals("N", true)) {
        return if (turnTo.equals("L", true)) "W" else "E"
    }

    if (oldDirection.equals("E", true)) {
        return if (turnTo.equals("L", true)) "N" else "S"
    }

    if (oldDirection.equals("S", true)) {
        return if (turnTo.equals("L", true)) "E" else "W"
    }

    if (oldDirection.equals("W", true)) {
        return if (turnTo.equals("L", true)) "S" else "N"
    }

    return "N"
}

fun move(position: Position, move: Move): Position {

    val newDirection = newDirection(position.direction, move.turnTo)
    //println("Move: ${move.turnTo}, ${move.distance}")
    //println("Old direction: ${position.direction}. New direction: $newDirection")

    if (newDirection.equals("N", true)) {
        return Position(newDirection, position.x, position.y + move.distance)
    }

    if (newDirection.equals("S", true)) {
        return Position(newDirection, position.x, position.y - move.distance)
    }

    if (newDirection.equals("E", true)) {
        return Position(newDirection, position.x + move.distance, position.y)
    }

    if (newDirection.equals("W", true)) {
        return Position(newDirection, position.x - move.distance, position.y)
    }

    return Position()
}

fun taxiDistance(position: Position): Int {
    return abs(position.x) + abs(position.y);
}

fun processInput(): List<Move> {
    val filePath = "D:\\JavaProjects\\AdventOfCode\\src\\main\\kotlin\\day_1\\input.in"
    val inputRaw = File(filePath).readText(Charsets.UTF_8)

    return inputRaw.split(", ").map { splitMove -> Move(splitMove) }
}

fun firstPart(moves: List<Move>) {
    val lastPosition = moves.fold(Position(), { oldPosition, aMove -> move(oldPosition, aMove) })
    val distance = taxiDistance(lastPosition)
    println("Final position: { ${lastPosition.x}, ${lastPosition.y} }")
    println("Manhattan distance from initial position: $distance")
}

fun positionsBetween(from: Position, to: Position): List<Position> {
    var positionsToCheck = MutableList(0) { _ -> Position() }
    if (to.x != from.x) {
        var initial = from.x + 1
        var final = to.x
        if (to.x < from.x) {
            initial = to.x
            final = from.x - 1
        }

        for (i in initial..final) {
            //println("   Will check position {${i}, ${from.y}}")
            positionsToCheck.add(Position(to.direction, i, from.y))
        }
    } else if (to.y != from.y) {
        var initial = from.y + 1
        var final = to.y
        if (to.y < from.y) {
            initial = to.y
            final = from.y - 1
        }

        for (i in initial..final) {
            //println("   Will check position {${from.x}, ${i}}")
            positionsToCheck.add(Position(to.direction, from.x, i))
        }
    }

    return positionsToCheck
}

fun secondPart(moves: List<Move>) {
    val visitedPositions = HashSet<String>()

    var currentPosition = Position()
    visitedPositions.add("0,0")

    var found = false
    for (aMove in moves) {
        val from = Position(currentPosition.direction, currentPosition.x, currentPosition.y)

        currentPosition = move(currentPosition, aMove)
        val positionString = "${currentPosition.x},${currentPosition.y}"
        //println("Current position: $positionString")

        val positionsToCheck = positionsBetween(from, currentPosition)

        for (position in positionsToCheck) {
            val positionToCheckString = "${position.x},${position.y}"
            if (visitedPositions.contains(positionToCheckString)) {
                println("Second time I reach position $positionToCheckString. Breaking")
                found = true
                currentPosition = position
                break
            }
            visitedPositions.add(positionToCheckString)
        }

        if (found) break
    }

    val finalDistance = taxiDistance(currentPosition)
    println("Distance for place visited 2nd time $finalDistance")
}

fun main(args: Array<String>) {
    //val moves: List<Move> = listOf("L20", "L20", "L20").map { instruction -> Move(instruction) }
    //val moves: List<Move> = listOf("R5", "L5", "R5", "R3").map { instruction -> Move(instruction) }
    //val moves: List<Move> = listOf("R8", "R4", "R4", "R8").map { instruction -> Move(instruction) }
    val moves = processInput()

    firstPart(moves)
    secondPart(moves)
}