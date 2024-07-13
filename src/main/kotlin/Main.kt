import java.io.File

fun main(args: Array<String>) {
    if (args.size != 2) {
        println("Usage: ${args[0]} tokenize <source_file>")
        return
    }

    val command = args[0]
    val filename = args[1]

    when (command) {
        "tokenize" -> {
            val source = File(filename).readText()
            val lexer = Lexer(source)
            val tokens = lexer.tokenize()
            tokens.forEach { println(it) }
        }

        else -> {
            println("Unknown command: $command")
        }
    }
}

