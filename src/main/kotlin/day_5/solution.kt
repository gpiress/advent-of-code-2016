package day_5

import java.security.MessageDigest


fun findNextChar(id: String, initial: Int): Pair<Char, Int> {
    var found = false
    var index = initial
    var nextChar = 'Å'

    while (!found) {
        val toTest = "$id$index"

        val digest = MessageDigest.getInstance("MD5").digest(toTest.toByteArray(Charsets.UTF_8))
        val digestString = digest.joinToString("") { c -> "%02x".format(c) }

        if (digestString.startsWith("00000")) {
            found = true
            nextChar = digestString[5]
            //println("   Char: $nextChar, index: $index, hash: $digestString")
        } else {
            index++
        }
    }

    return Pair(nextChar, index)
}

fun firstPart(id: String) {
    var index = 0;
    val result = MutableList(0){ _ -> 'A' }

    while (result.size < 8) {
        val nextChar = findNextChar(id, index)
        result.add(nextChar.first)
        index = nextChar.second + 1
    }

    println("First part: ${ result.joinToString("") }")
}

fun getCharForIndex(id: String, index: Int): Pair<Char, Int> {
    val toTest = "$id$index"

    val digest = MessageDigest.getInstance("MD5").digest(toTest.toByteArray(Charsets.UTF_8))
    val digestString = digest.joinToString("") { c -> "%02x".format(c) }

    if (digestString.startsWith("00000")) {
        val position = digestString[5] - '0'
        val char = digestString[6]
        //println("   $digestString")

        return Pair(char, position)
    }

    return Pair('Å', -1)
}

fun secondPart(id: String) {
    var pass = "-".repeat(8)

    var charsFound = 0
    var index = -1

    while (charsFound < 8) {
        index++
        val charAndIndex = getCharForIndex(id, index)
//        if (charAndIndex.first != 'Å') {
//            println("   $charAndIndex")
//        }

        if (charAndIndex.second == -1 || charAndIndex.second >= 8) {
            continue
        }

        if (pass[charAndIndex.second] != '-') {
            continue
        }

        val first = pass.substring(0, charAndIndex.second)
        val second = pass.substring(charAndIndex.second + 1)

        pass = "$first${charAndIndex.first}$second"

        charsFound++

        println(pass)
    }

    println("Second part: $pass")
}

fun main(args: Array<String>) {
    //val input = "abc"
    val input = "cxdnnyjw"

    //firstPart(input)
    secondPart(input)
}
