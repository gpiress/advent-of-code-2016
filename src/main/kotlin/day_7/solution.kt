package day_7

import java.io.File


fun containsAbba(part: String): Boolean {
    if (part.length < 4) {
        return false
    }

    var first = part[0]
    var second = part[1]
    var found = false

    val possibleChars = IntRange(2, part.length - 2)
    possibleChars.forEach { index -> run {
        val centerMatch = part[index].equals(second, false)
        val outerMatch = part[index + 1].equals(first, false)
        val differentChars = !(first.equals(second, false))

        if (!found) {
            found = differentChars && centerMatch && outerMatch

//            if (found) {
//                println("   Found ABBA string: $first$second${part[index]}${part[index+1]}")
//            }
        }

        first = second
        second = part[index]
    } }

    return found
}

fun supportsTLS(ip: String): Boolean {

    var ipPart = ""
    var hyperNetsContainAbba = false
    var partsContainAbba = false

    ip.forEach { letter -> run {
        when {
            letter.equals('[', false) -> {
                if (!partsContainAbba) {
                    partsContainAbba = containsAbba(ipPart)
                }
                ipPart = ""
            }
            letter.equals(']', false) -> {
                if (!hyperNetsContainAbba) {
                    hyperNetsContainAbba = containsAbba(ipPart)
                }
                ipPart = ""
            }
            else -> {
                ipPart += letter
            }
        }
    } }

    if (!partsContainAbba) {
        partsContainAbba = containsAbba(ipPart)
    }

    return (partsContainAbba && !hyperNetsContainAbba)
}

fun findAllAbas(part: String): List<String> {
    if (part.length < 3) {
        return emptyList()
    }

    val validRange = IntRange(0, part.length - 3)

    return validRange
        .filter { index ->
            run {
                val first = part[index]
                val second = part[index + 1]
                val third = part[index + 2]

                (!first.equals(second, false) && first.equals(third, true))
            }
        }
        .map { index -> "${part[index]}${part[index + 1]}${part[index + 2]}" }
}

fun supportsSSL(ip: String): Boolean {
    var ipPart = ""

    val abas = MutableList(0) { _ -> "" }
    val babs = MutableList(0) { _ -> "" }

    ip.forEach { letter -> run {
        when {
            letter.equals('[', false) -> {
                abas.addAll(findAllAbas(ipPart))
                ipPart = ""
            }
            letter.equals(']', false) -> {
                babs.addAll(findAllAbas(ipPart))
                ipPart = ""
            }
            else -> {
                ipPart += letter
            }
        }
    } }

    abas.addAll(findAllAbas(ipPart))

    val maybeMatchAbaAndBab: String? = abas
        .find { aba ->
            run {
                val matchingBab = "${aba[1]}${aba[0]}${aba[1]}"
                val maybeMatchingBob = babs.find { bab -> bab.equals(matchingBab, false) }

                maybeMatchingBob != null
            }
        }

    return (maybeMatchAbaAndBab != null)
}

fun firstPart(ips: List<String>) {
    val tlsSupportedIps = ips
        .filter(::supportsTLS)
        .count()

    println("First part: [$tlsSupportedIps] IPs support TLS")
}

fun secondPart(ips: List<String>) {
    val sslSupportedIps = ips
        .filter(::supportsSSL)
        .map { ip -> run {
            println("$ip supports SSL")
            ip
        } }
        .count()

    println("Second part: [$sslSupportedIps] IPs support SSL")
}

fun processInput(): List<String> {
    val filePath = "D:\\JavaProjects\\AdventOfCode\\src\\main\\kotlin\\day_7\\input.in"
    //val filePath = "D:\\JavaProjects\\AdventOfCode\\src\\main\\kotlin\\day_7\\test.in"
    return File(filePath).readLines(Charsets.UTF_8)
}

fun main(args: Array<String>) {
    val input = processInput()

    firstPart(input)
    secondPart(input)
}
