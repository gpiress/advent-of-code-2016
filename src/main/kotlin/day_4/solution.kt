package day_4

import java.io.File


fun firstPart(rooms: List<Room>) {
    val sumOfSectorIds = rooms
        .fold(0, { acc, room -> acc + room.sectorId })

    println("Part 1 --- Sum of sector ids: $sumOfSectorIds")
}

fun secondPart(rooms: List<Room>) {

    val sectorId = rooms
        .first { room -> room.decrypt().equals("northpole object storage", true) }
        .sectorId

    println("Part 2 --- Storage sector id: $sectorId")
}

fun getValidRooms(input: List<String>): List<Room> {
    val roomsToCheck: List<Room> = input.map { roomString ->
        run {
            val splitTerms = roomString.split("[")
            val id = splitTerms[1].substringBefore("]")
            val roomName = splitTerms[0].replace("-", " ")

            val roomNameLetters = roomName.filter(Char::isLowerCase)
            val sectorId = roomName.filter(Char::isDigit).toInt(10)

            val lettersMap: Map<Char, Int> = roomNameLetters.associate { letter ->
                letter to roomNameLetters.count { l ->
                    l.equals(letter, true)
                }
            }

            val lettersList = lettersMap.map { entry -> Letter(entry.key, entry.value) }

            Room(roomName.substringBefore(sectorId.toString()).trim(), lettersList, sectorId, id)
        }
    }

    return roomsToCheck.filter(Room::isValid)
}

fun processInput(): List<String> {
    val filePath = "D:\\JavaProjects\\AdventOfCode\\src\\main\\kotlin\\day_4\\input.in"
    //val filePath = "D:\\JavaProjects\\AdventOfCode\\src\\main\\kotlin\\day_4\\test.in"
    return File(filePath).readLines(Charsets.UTF_8)
}

fun main(args: Array<String>) {
    val input = processInput()
    val validRooms = getValidRooms(input)

    firstPart(validRooms)
    secondPart(validRooms)
}


class Letter(val letter: Char, val count: Int)

class Room(val roomName: String, letters: List<Letter>, val sectorId: Int, private val id: String) {
    private val orderedLetters = letters.sortedWith(compareBy<Letter> { l -> -l.count }.thenBy(Letter::letter))

    fun isValid(): Boolean {
        val expectedId = orderedLetters.take(5).joinToString("") { letter -> letter.letter.toString() }
        //println("   Room letters: ${ orderedLetters.joinToString { letter -> "${letter.letter}-${letter.count}" } }")
        //println("   Expected id: $expectedId")
        //println("   Actual id:   $id")
        return expectedId.equals(id, true)
    }

    fun decrypt(): String {
        val rotatedLetters: List<Char> = roomName.map { letter ->
            run {
                if (!letter.isLowerCase()) {
                    letter
                } else {
                    val base = letter.minus('a')
                    val offset = (base + sectorId) % 26
                    'a'.plus(offset)
                }
            }
        }
        return rotatedLetters.joinToString("") { letter -> letter.toString() }
    }
}