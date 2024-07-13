class Lexer(private val source: String) {
    private val tokens: MutableList<Token> = mutableListOf()
    private var currentIndex: Int = 0
    private var currentLine: Int = 1
    private var currentColumn: Int = 1
    private var errorOccurred: Boolean = false

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
        ':' to TokenType.COLON,
        '=' to TokenType.EQUAL,
        '!' to TokenType.BANG,
        '>' to TokenType.GREATER,
        '<' to TokenType.LESS
    )

    private val multiCharTokenMap = mapOf(
        "==" to TokenType.EQUAL_EQUAL,
        "!=" to TokenType.BANG_EQUAL,
        ">=" to TokenType.GREATER_EQUAL,
        "<=" to TokenType.LESS_EQUAL,
        "//" to TokenType.COMMENT
    )

    init {
        tokenize()
        tokens.add(Token(TokenType.EOF, "", currentLine, currentColumn))
    }

    private fun tokenize() {
        while (currentIndex < source.length) {
            val currentChar = source[currentIndex]

            when {
                currentChar == '\n' -> {
                    currentLine++
                    currentColumn = 1
                    currentIndex++
                }

                currentChar.isWhitespace() -> {
                    currentIndex++
                    currentColumn++
                }

                currentChar == '"' -> {
                    tokenizeString()
                }

                currentChar.isDigit() -> {
                    tokenizeNumber()
                }

                isStartOfMultiCharToken(currentChar) -> {
                    tokenizeMultiCharToken()
                }

                tokenMap.containsKey(currentChar) -> {
                    val token = createToken(currentChar.toString())
                    tokens.add(token)
                    currentIndex++
                    currentColumn++
                }

                else -> {
                    reportError(currentChar)
                }
            }
        }
    }

    private fun tokenizeNumber() {
        val start = currentIndex
        consumeDigits()

        // if there is a decimal point
        if (currentIndex < source.length && source[currentIndex] == '.') {
            // if there is a digit after the decimal point
            if (currentIndex + 1 < source.length && source[currentIndex + 1].isDigit()) {
                currentIndex++
                currentColumn++
                consumeDigits()
            }
        }

        val number = source.substring(start, currentIndex)
        tokens.add(Token(TokenType.NUMBER, number, currentLine, currentColumn))
    }


    private fun consumeDigits() {
        while (currentIndex < source.length && source[currentIndex].isDigit()) {
            currentIndex++
            currentColumn++
        }
    }

    private fun isStartOfMultiCharToken(currentChar: Char): Boolean {
        val nextIndex = currentIndex + 1
        if (nextIndex < source.length) {
            val nextChar = source[nextIndex]
            val potentialToken = "$currentChar$nextChar"
            return multiCharTokenMap.containsKey(potentialToken)
        }
        return false
    }

    private fun tokenizeMultiCharToken() {
        val currentChar = source[currentIndex]
        val nextChar = source[currentIndex + 1]
        val multiCharToken = "$currentChar$nextChar"
        val tokenType = multiCharTokenMap[multiCharToken]!!

        if (tokenType == TokenType.COMMENT) {
            skipComment()
        } else {
            tokens.add(Token(tokenType, multiCharToken, currentLine, currentColumn))
            currentIndex += 2
            currentColumn += 2
        }
    }

    private fun tokenizeString() {
        val startLine = currentLine
        val startColumn = currentColumn
        currentIndex++ // Move past the opening double quote
        currentColumn++

        val sb = StringBuilder()
        while (currentIndex < source.length && source[currentIndex] != '"') {
            if (source[currentIndex] == '\n') {
                reportError('\"', startLine, startColumn)
                return
            }
            sb.append(source[currentIndex])
            currentIndex++
            currentColumn++
        }

        if (currentIndex >= source.length) {
            reportError('\"', startLine, startColumn)
        } else {
            // Consume closing quote
            currentIndex++
            currentColumn++
            tokens.add(Token(TokenType.STRING, sb.toString(), startLine, startColumn))
        }
    }

    private fun skipComment() {
        // Skip until end of line or end of file
        while (currentIndex < source.length && source[currentIndex] != '\n') {
            currentIndex++
        }
        // Increment line count
        currentLine++
        // Reset column count
        currentColumn = 1
        // Move past the newline character
        if (currentIndex < source.length && source[currentIndex] == '\n') {
            currentIndex++
        }
    }

    private fun createToken(char: String): Token {
        val tokenType = tokenMap[char[0]]!!
        return Token(tokenType, char, currentLine, currentColumn)
    }

    private fun reportError(currentChar: Char, line: Int = currentLine, column: Int = currentColumn) {
        if (currentChar == '\"') {
            System.err.println("[line $line] Error: Unterminated string.")
        } else {
            System.err.println("[line $line] Error: Unexpected character: $currentChar")
        }
        errorOccurred = true
        currentIndex++
        currentColumn++
    }

    fun getTokens(): List<Token> {
        return tokens
    }

    fun hasError(): Boolean {
        return errorOccurred
    }
}
