enum class TokenType {
    // Single-character tokens
    STAR,
    DOT,
    COMMA,
    PLUS,
    MINUS,
    SLASH,
    SEMICOLON,
    COLON,

    // Parentheses and braces
    LEFT_PAREN,
    RIGHT_PAREN,
    LEFT_BRACE,
    RIGHT_BRACE,

    // Keywords
    EOF,
}


data class Token(val type: TokenType, val value: String) {
    override fun toString(): String {
        return "$type $value null"
    }
}
