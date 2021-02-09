package day_3

import java.io.File


fun firstPart(possibleTriangles: List<String>) {
    val validTriangles = possibleTriangles
        .map { maybeTriangle -> Triangle(maybeTriangle) }
        .filter { maybeTriangle -> maybeTriangle.isValid() }
        .size

    println("Valid triangles: $validTriangles")
}

fun secondPart(possibleTriangles: List<String>) {

    var possibleTrianglesMutable: List<String> = ArrayList(possibleTriangles)
    var validTriangles = 0;

    while (possibleTrianglesMutable.size >= 3) {
        val trianglesToParse = possibleTrianglesMutable.take(3)
        possibleTrianglesMutable = possibleTrianglesMutable.drop(3)

        val firstLineSides = trianglesToParse[0].trim().split("\\s+".toRegex())
        val secondLineSides = trianglesToParse[1].trim().split("\\s+".toRegex())
        val thirdLineSides = trianglesToParse[2].trim().split("\\s+".toRegex())

        val maybeTriangles = listOf(
            Triangle("${firstLineSides[0]} ${secondLineSides[0]} ${thirdLineSides[0]}"),
            Triangle("${firstLineSides[1]} ${secondLineSides[1]} ${thirdLineSides[1]}"),
            Triangle("${firstLineSides[2]} ${secondLineSides[2]} ${thirdLineSides[2]}"),
        )

        validTriangles += maybeTriangles.count { maybeTri -> maybeTri.isValid() }
    }

    println("2nd phase valid triangles: $validTriangles")
}

fun processInput(): List<String> {
    val filePath = "D:\\JavaProjects\\AdventOfCode\\src\\main\\kotlin\\day_3\\input.in"
    //val filePath = "D:\\JavaProjects\\AdventOfCode\\src\\main\\kotlin\\day_3\\test.in"
    return File(filePath).readLines(Charsets.UTF_8)
}

fun main(args: Array<String>) {
    val possibleTriangles = processInput()

    firstPart(possibleTriangles)
    secondPart(possibleTriangles)
}


class Triangle(sidesString: String) {
    private val sides: List<Int> = sidesString
        .trim()
        .split(" ")
        .map(String::trim)
        .filter(String::isNotEmpty)
        .map(String::toInt)
        .sorted()

    fun isValid(): Boolean {
        //println("Checking if triangle is valid: $sides")
        return sides[0] + sides[1] > sides[2]
    }
}