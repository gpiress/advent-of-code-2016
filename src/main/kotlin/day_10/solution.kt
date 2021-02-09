package day_10

import java.io.File


fun firstPart(initialState: ArrayList<Bot>, commands: List<String>) {

    val outputs = HashMap<Int, ArrayList<Int>>()

    while (initialState.count(Bot::isFull) > 0) {
        initialState
            .filter(Bot::isFull)
            .forEach { bot ->
                run {
                    val commandToRun = commands
                        .find { command -> command.startsWith("bot ${bot.id} gives") }
                        .orEmpty()

                    if (commandToRun.isNotEmpty()) {
                        runCommand(commandToRun, initialState, outputs)
                    }
                }
            }
        //println(initialState)
    }

    println(outputs)

    println("First part: ")
}

fun secondPart(commands: List<String>) {

    println("Second part: ")
}

fun processInput(): List<String> {
    val filePath = "D:\\JavaProjects\\AdventOfCode\\src\\main\\kotlin\\day_10\\input.in"
    //val filePath = "D:\\JavaProjects\\AdventOfCode\\src\\main\\kotlin\\day_10\\test.in"
    return File(filePath).readLines(Charsets.UTF_8)
}

fun main(args: Array<String>) {
    val input = processInput()
    val initialState: ArrayList<Bot> = parseInitialState(input)

    println(initialState)

    firstPart(initialState, input)
    secondPart(input)
}

fun runCommand(command: String, bots: ArrayList<Bot>, outputs: HashMap<Int, ArrayList<Int>>) {
    /*
    bot 2 gives low to bot 1 and high to bot 0
    bot 1 gives low to output 1 and high to bot 0
    bot 0 gives low to output 2 and high to output 0
     */
    val regex = """bot (\d+) gives low to (output|bot) (\d+) and high to (output|bot) (\d+)""".toRegex()
    val matchResult = regex.find(command)

    if (matchResult?.groupValues?.size!! > 4) {
        //println(command)

        val fromBotId = matchResult.groupValues[1].toInt(10)

        val isLowToOutput = matchResult.groupValues[2].equals("output", true)
        val lowTo = matchResult.groupValues[3].toInt(10)

        val isHighToOutput = matchResult.groupValues[4].equals("output", true)
        val highTo = matchResult.groupValues[5].toInt(10)

        val fromBot = bots.find { bot -> bot.id == fromBotId }
        val fromBotValues = fromBot!!.popFirstValues()
        val low = if (fromBotValues[0] < fromBotValues[1]) fromBotValues[0] else fromBotValues[1]
        val high = if (fromBotValues[0] > fromBotValues[1]) fromBotValues[0] else fromBotValues[1]

        if (low == 17 && high == 61) {
            println("Bot $fromBotId comparing $low and $high")
        }

        if (!isLowToOutput) {
            val lowToBot = bots.find { bot -> bot.id == lowTo }

            if (lowToBot == null) {
                bots.add(Bot(lowTo, ArrayList(listOf(low))))
            } else {
                lowToBot.addValue(low)
            }
        } else {
            val newOutputValues = outputs.getOrDefault(lowTo, ArrayList())
            newOutputValues.add(low)
            if (outputs.containsKey(lowTo)) outputs.replace(lowTo, newOutputValues) else outputs[lowTo] =
                newOutputValues
        }

        if (!isHighToOutput) {
            val highToBot = bots.find { bot -> bot.id == highTo }

            if (highToBot == null) {
                bots.add(Bot(highTo, ArrayList(listOf(high))))
            } else {
                highToBot.addValue(high)
            }
        } else {
            val newOutputValues = outputs.getOrDefault(highTo, ArrayList())
            newOutputValues.add(low)
            if (outputs.containsKey(highTo)) outputs.replace(highTo, newOutputValues) else outputs[highTo] =
                newOutputValues
        }
    }
}

fun parseInitialState(valueCommands: List<String>): ArrayList<Bot> {
    val initialState = HashMap<Int, Bot>()

    valueCommands
        .filter { command -> command.startsWith("value ") }
        .forEach { command ->
            run {
                // value 5 goes to bot 2
                val regex = """value (\d+) goes to bot (\d+)""".toRegex()
                val matchResult = regex.find(command)

                if (matchResult?.groupValues?.size!! > 2) {
                    val value = matchResult.groupValues[1].toInt(10)
                    val botId = matchResult.groupValues[2].toInt(10)

                    //println(command)
                    if (initialState.containsKey(botId)) {
                        initialState[botId]?.addValue(value)
                    } else {
                        initialState[botId] = Bot(botId, ArrayList(listOf(value)))
                    }
                }
            }
        }

    return ArrayList(initialState.values)
}

class Bot(val id: Int, private val values: ArrayList<Int>) {

    fun addValue(value: Int) {
        this.values.add(value)
    }

    fun popFirstValues(): List<Int> {
        if (values.size < 2) {
            return emptyList()
        }

        val firstValues = listOf(values[0], values[1])
        values.removeAt(1)
        values.removeAt(0)

        return firstValues
    }

    fun isFull(): Boolean {
        return values.size >= 2
    }

    override fun toString(): String {
        return "ID $id: $values"
    }
}