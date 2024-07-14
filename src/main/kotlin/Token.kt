data class Token(
    val type: TokenType,
    val lexeme: String,
    val line: Int,
    val column: Int
) {
    override fun toString(): String = when (type) {
        TokenType.STRING -> "STRING $lexeme ${lexeme.substring(1, lexeme.length - 1)}"
        TokenType.NUMBER -> "NUMBER $lexeme ${lexeme.toDoubleOrNull()}"
        TokenType.EOF -> "EOF  null"
        else -> "$type $lexeme null"
    }

    fun isEndOfFile(): Boolean = type == TokenType.EOF

    fun isOperator(): Boolean {
        return when (type) {
            TokenType.PLUS, TokenType.MINUS, TokenType.STAR, TokenType.SLASH -> true
            else -> false
        }
    }
}
