class Lexer(private val source: String) {
    private var currentIndex: Int = 0

    fun tokenize(): List<Token> {
        val tokens = mutableListOf<Token>()
        while (currentIndex < source.length) {
            when (val currentChar = source[currentIndex]) {
                '(' -> tokens.add(Token(TokenType.LEFT_PAREN, "("))
                ')' -> tokens.add(Token(TokenType.RIGHT_PAREN, ")"))
                '{' -> tokens.add(Token(TokenType.LEFT_BRACE, "{"))
                '}' -> tokens.add(Token(TokenType.RIGHT_BRACE, "}"))
                else -> {
                    // Skip whitespace
                    if (currentChar.isWhitespace()) {
                        currentIndex++
                        continue
                    }
                    // Handle other tokens like numbers, operators, etc.
                    // For simplicity, let's skip these for now
                }
            }
            currentIndex++
        }
        tokens.add(Token(TokenType.EOF, ""))
        return tokens
    }
}
