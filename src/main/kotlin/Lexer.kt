class Lexer(private val source: String) {
    private val tokens: MutableList<Token> = mutableListOf()
    private var currentIndex: Int = 0

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
        ':' to TokenType.COLON
    )

    init {
        tokenize()
        tokens.add(Token(TokenType.EOF, ""))
    }

    private fun tokenize() {
        while (currentIndex < source.length) {
            val currentChar = source[currentIndex]
            if (currentChar.isWhitespace()) {
                currentIndex++
                continue
            }

            tokenMap[currentChar]?.let {
                tokens.add(createToken(it, currentChar))
            }
            currentIndex++
        }
    }

    private fun createToken(type: TokenType, char: Char): Token {
        return Token(type, char.toString())
    }

    fun getTokens(): List<Token> {
        return tokens
    }
}
