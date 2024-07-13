class Lexer(private val source: String) {
    private val tokens: MutableList<Token> = mutableListOf()
    private var currentIndex: Int = 0
    private var currentLine: Int = 1
    private var currentColumn: Int = 1
    private var errorOccurred: Boolean = false

    private val tokenMap = mapOf(
        // Parentheses and braces
        '(' to TokenType.LEFT_PAREN,
        ')' to TokenType.RIGHT_PAREN,
        '{' to TokenType.LEFT_BRACE,
        '}' to TokenType.RIGHT_BRACE,

        // Single-character tokens
        '*' to TokenType.STAR,
        '.' to TokenType.DOT,
        ',' to TokenType.COMMA,
        '+' to TokenType.PLUS,
        '-' to TokenType.MINUS,
        '/' to TokenType.SLASH,
        ';' to TokenType.SEMICOLON,
        ':' to TokenType.COLON,
        '=' to TokenType.EQUAL,
        '!' to TokenType.BANG,
        '>' to TokenType.GREATER,
        '<' to TokenType.LESS
    )

    private val multiCharTokenMap = mapOf(
        "==" to TokenType.EQUAL_EQUAL,
        "!=" to TokenType.BANG_EQUAL,
        ">=" to TokenType.GREATER_EQUAL,
        "<=" to TokenType.LESS_EQUAL
    )

    init {
        tokenize()
        tokens.add(Token(TokenType.EOF, "", currentLine, currentColumn))
    }

    private fun tokenize() {
        while (currentIndex < source.length) {
            val currentChar = source[currentIndex]

            when {
                currentChar == '\n' -> {
                    currentLine++
                    currentColumn = 1
                    currentIndex++
                }

                currentChar.isWhitespace() -> {
                    currentIndex++
                    currentColumn++
                }

                isStartOfMultiCharToken(currentChar) -> {
                    tokenizeMultiCharToken()
                }

                tokenMap.containsKey(currentChar) -> {
                    val tokenType = tokenMap[currentChar]!!
                    tokens.add(Token(tokenType, currentChar.toString(), currentLine, currentColumn))
                    currentIndex++
                    currentColumn++
                }

                else -> {
                    reportError(currentChar)
                }
            }
        }
    }

    private fun isStartOfMultiCharToken(currentChar: Char): Boolean {
        val nextIndex = currentIndex + 1
        if (nextIndex < source.length) {
            val nextChar = source[nextIndex]
            val potentialToken = "$currentChar$nextChar"
            return multiCharTokenMap.containsKey(potentialToken)
        }
        return false
    }

    private fun tokenizeMultiCharToken() {
        val currentChar = source[currentIndex]
        val nextChar = source[currentIndex + 1]
        val multiCharToken = "$currentChar$nextChar"
        val tokenType = multiCharTokenMap[multiCharToken]!!
        tokens.add(Token(tokenType, multiCharToken, currentLine, currentColumn))
        currentIndex += 2
        currentColumn += 2
    }

    private fun reportError(currentChar: Char) {
        System.err.println("[line $currentLine] Error: Unexpected character: $currentChar")
        errorOccurred = true
        currentIndex++
        currentColumn++
    }

    fun getTokens(): List<Token> {
        return tokens
    }

    fun hasError(): Boolean {
        return errorOccurred
    }
}
