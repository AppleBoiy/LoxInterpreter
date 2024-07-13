class Lexer(private val source: String) {
    private val tokens: MutableList<Token> = mutableListOf()
    private var currentIndex: Int = 0
    private var currentLine: Int = 1
    private var currentColumn: Int = 1
    private var errorOccurred: Boolean = false

    private val tokenMap = mapOf(
        '(' to TokenType.LEFT_PAREN,
        ')' to TokenType.RIGHT_PAREN,
        '{' to TokenType.LEFT_BRACE,
        '}' to TokenType.RIGHT_BRACE,
        '*' to TokenType.STAR,
        '.' to TokenType.DOT,
        ',' to TokenType.COMMA,
        '+' to TokenType.PLUS,
        '-' to TokenType.MINUS,
        '/' to TokenType.SLASH,
        ';' to TokenType.SEMICOLON,
        ':' to TokenType.COLON
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

                tokenMap.containsKey(currentChar) -> {
                    val tokenType = tokenMap[currentChar]!!
                    val token = Token(tokenType, currentChar.toString(), currentLine, currentColumn)
                    tokens.add(token)
                    currentIndex++
                    currentColumn++
                }

                else -> {
                    reportError(currentChar)
                }
            }
        }
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