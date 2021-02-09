package day_9

import java.io.File


fun decompress(compressedString: String): String {
    var decompressed = ""

    var insideMarker = false
    var seenCharacters = 0
    var charactersToRepeat = 0
    var timesToRepeat = 0
    var stringToRepeat = ""

    compressedString.forEachIndexed { index, char ->
        run {
            if (!insideMarker) {
                if (seenCharacters < charactersToRepeat) {
                    stringToRepeat += char
                    seenCharacters++

                    if (seenCharacters == charactersToRepeat) {
                        decompressed += stringToRepeat.repeat(timesToRepeat)
                    }
                } else if (char.equals('(', false)) {
                    // Marker = (10x51) -- 10 characters repeated 51 times
                    insideMarker = true
                    seenCharacters = 0
                    stringToRepeat = ""

                    val markerEnd =
                        compressedString.substring(index).indexOfFirst { c -> c.equals(')', false) }
                    val split = compressedString.substring(index + 1, index + markerEnd).split("x")
                    charactersToRepeat = split[0].toInt(10)
                    timesToRepeat = split[1].toInt(10)
                } else {
                    decompressed += char
                }
            } else {
                insideMarker = !char.equals(')', false)
            }
        }
    }

    return decompressed
}

fun decompressedLength(compressed: String): Long {
    if (!compressed.contains('(')) {
        return compressed.length.toLong()
    }

    val firstMarkerStart = compressed.indexOfFirst { c -> c.equals('(', false) }

    val firstMarkerEnd =
        firstMarkerStart + compressed.substring(firstMarkerStart)
            .indexOfFirst { c -> c.equals(')', false) }

    val firstMarkerRange = compressed.substring(firstMarkerStart + 1, firstMarkerEnd)
    val split = firstMarkerRange.split("x")
    val markerLength = split[0].toInt(10)
    val toRepeat = split[1].toInt(10)

    // X(8x2)(3x3)ABCY
    val markerSubstring = compressed.substring(firstMarkerEnd + 1, firstMarkerEnd + 1 + markerLength)
    val theRest = compressed.substring(firstMarkerEnd + markerLength + 1)

    return (firstMarkerStart
        + (toRepeat * decompressedLength(markerSubstring))
        + decompressedLength(theRest))
}

fun firstPart(compressedStrings: List<String>) {

    val decompressed: List<String> = compressedStrings
        .map(::decompress)

    decompressed
        .forEach(::println)

    val nonWhiteSpace = decompressed[0].length
    println("First part: $nonWhiteSpace")
}

fun secondPart(compressedStrings: List<String>) {

    val decompressedLengths = compressedStrings.map(::decompressedLength)

    decompressedLengths.forEach(::println)

    val totalLength = decompressedLengths[0]
    println("Second part: $totalLength")
}

fun processInput(): List<String> {
    val filePath = "D:\\JavaProjects\\AdventOfCode\\src\\main\\kotlin\\day_9\\input.in"
    //val filePath = "D:\\JavaProjects\\AdventOfCode\\src\\main\\kotlin\\day_9\\test.in"
    return File(filePath).readLines(Charsets.UTF_8)
}

fun main(args: Array<String>) {
    val input = processInput()

    firstPart(input)
    secondPart(input)
}
