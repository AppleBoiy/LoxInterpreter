enum class TokenType {
    // Single character tokens
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    STAR, DOT, COMMA, PLUS, MINUS, SLASH, SEMICOLON,
    COLON, EQUAL, BANG, GREATER, LESS,

    // Multi-character tokens
    EQUAL_EQUAL, BANG_EQUAL, GREATER_EQUAL, LESS_EQUAL,
    COMMENT,

    // Literals
    STRING, NUMBER, IDENTIFIER,

    // Keywords
    AND, CLASS, ELSE, FALSE, FOR, FUN, IF, NIL, OR, PRINT, RETURN,
    SUPER, THIS, TRUE, VAR, WHILE,

    // End of file
    EOF
}
