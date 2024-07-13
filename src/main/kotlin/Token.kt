data class Token(val type: TokenType, val value: String?, val line: Int, val column: Int) {
    override fun toString(): String {
        return when (type) {
            TokenType.STRING -> "STRING \"${value}\" $value"
            TokenType.NUMBER -> "NUMBER $value ${value!!.toDouble()}"
            TokenType.EOF -> "EOF  null"
            else -> "$type $value null"
        }
    }
}
