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

    // Operators
    EQUAL,
    BANG,
    GREATER,
    LESS,

    // Multi-character tokens
    EQUAL_EQUAL,
    BANG_EQUAL,
    GREATER_EQUAL,
    LESS_EQUAL,
    COMMENT,

    // Literals
    STRING,

    NUMBER,
    
    // Keywords
    EOF,
}
