data class Token(val type: TokenType, val value: String) {
    override fun toString(): String {
        return "$type $value null"
    }
}
