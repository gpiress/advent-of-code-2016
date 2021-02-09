package day_6

import java.io.File


fun findMostCommonLetterForIndex(words: List<String>, index: Int): Char {
    val letters: List<Char> = words.map { word -> word[index] }

    var mostCommonChar: Char = 'å';
    var mostCommonFrequency: Int = 0;

    letters.forEach { letter -> run {
        val count = letters.count { l -> l.equals(letter, true) }

        if (count > mostCommonFrequency) {
            mostCommonChar = letter
            mostCommonFrequency = count
        }
    } }

    return mostCommonChar
}

fun findLeastCommonLetterForIndex(words: List<String>, index: Int): Char {
    val letters: List<Char> = words.map { word -> word[index] }

    var leastCommonChar: Char = 'å';
    var leastCommonFrequency: Int = Int.MAX_VALUE;

    letters.forEach { letter -> run {
        val count = letters.count { l -> l.equals(letter, true) }

        if (count < leastCommonFrequency) {
            leastCommonChar = letter
            leastCommonFrequency = count
        }
    } }

    return leastCommonChar
}

fun firstPart(words: List<String>) {
    val charRange = IntRange(0, words[0].length - 1)

    val errorCorrected = charRange
        .map { index -> findMostCommonLetterForIndex(words, index) }
        .joinToString("")

    println("First part error corrected: $errorCorrected")
}

fun secondPart(words: List<String>) {
    val charRange = IntRange(0, words[0].length - 1)

    val errorCorrected = charRange
        .map { index -> findLeastCommonLetterForIndex(words, index) }
        .joinToString("")

    println("Second part error corrected: $errorCorrected")
}

fun processInput(): List<String> {
    val filePath = "D:\\JavaProjects\\AdventOfCode\\src\\main\\kotlin\\day_6\\input.in"
    //val filePath = "D:\\JavaProjects\\AdventOfCode\\src\\main\\kotlin\\day_6\\test.in"
    return File(filePath).readLines(Charsets.UTF_8)
}

fun main(args: Array<String>) {
    val input = processInput()

    firstPart(input)
    secondPart(input)
}
