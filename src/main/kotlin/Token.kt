data class Token(val type: TokenType, val value: String?, val line: Int, val column: Int) {
    override fun toString(): String {
        return when {
            type == TokenType.STRING -> "$type \"$value\" $value"
            value != null -> "$type $value null"
            else -> "$type null"
        }
    }
}
