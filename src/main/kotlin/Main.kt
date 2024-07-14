import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 2) {
        println("Usage: ${args[0]} tokenize <source_file>")
        return
    }

    val command = args[0]
    val filename = args[1]

    val source = File(filename).readText()
    val lexer = Lexer(source)
    val tokens = lexer.getTokens()

    when (command) {
        "tokenize" -> {
            val hasError = lexer.hasError()
            tokens.forEach { println(it) }

            if (hasError) {
                exitProcess(65)
            } else {
                exitProcess(0)
            }
        }

        "parse" -> {
            val parser = Parser(tokens)
            val expression = parser.parse()

            val astPrinter = AstPrinter()
            val ast = astPrinter.print(expression)
            println(ast)
        }

        else -> {
            println("Unknown command: $command")
        }
    }
}

