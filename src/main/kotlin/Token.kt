data class Token(
    val type: TokenType,
    val lexeme: String?,
    val line: Int,
    val column: Int
) {
    override fun toString(): String = when (type) {
        TokenType.STRING -> "STRING \"$lexeme\" $lexeme"
        TokenType.NUMBER -> "NUMBER $lexeme ${lexeme?.toDoubleOrNull()}"
        TokenType.EOF -> "EOF  null"
        else -> "$type $lexeme null"
    }

    fun isEndOfFile(): Boolean = type == TokenType.EOF
}
