class Lexer(private val source: String) {
    private val tokens: MutableList<Token> = mutableListOf()
    private var currentIndex: Int = 0
    private var currentLine: Int = 1
    private var currentColumn: Int = 1
    private var errorOccurred: Boolean = false

    private val tokenMap = mapOf(
        '(' to TokenType.LEFT_PAREN,
        ')' to TokenType.RIGHT_PAREN,
        '{' to TokenType.LEFT_BRACE,
        '}' to TokenType.RIGHT_BRACE,
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
                currentChar == '\n' -> consumeNewline()
                currentChar.isWhitespace() -> consumeWhitespace()
                currentChar.isLetterOrUnderscore() -> tokenizeIdentifier()
                currentChar == '"' -> tokenizeString()
                currentChar.isDigit() -> tokenizeNumber()
                isStartOfMultiCharToken(currentChar) -> tokenizeMultiCharToken()
                tokenMap.containsKey(currentChar) -> tokenizeSingleCharToken(currentChar)
                else -> reportError(currentChar)
            }
        }
    }

    private fun consumeNewline() {
        currentLine++
        currentColumn = 1
        currentIndex++
    }

    private fun consumeWhitespace() {
        currentIndex++
        currentColumn++
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

    private fun tokenizeNumber() {
        val start = currentIndex
        consumeDigits()

        // Decimal point handling
        if (currentIndex < source.length && source[currentIndex] == '.') {
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

    private fun skipComment() {
        while (currentIndex < source.length && source[currentIndex] != '\n') {
            currentIndex++
        }
        if (currentIndex < source.length && source[currentIndex] == '\n') {
            currentIndex++
            currentLine++
            currentColumn = 1
        }
    }

    private fun tokenizeSingleCharToken(currentChar: Char) {
        val token = createToken(currentChar.toString())
        tokens.add(token)
        currentIndex++
        currentColumn++
    }

    private fun createToken(char: String): Token {
        val tokenType = tokenMap[char[0]]!!
        return Token(tokenType, char, currentLine, currentColumn)
    }

    private fun reportError(currentChar: Char, line: Int = currentLine, column: Int = currentColumn) {
        val errorMessage = when (currentChar) {
            '\"' -> "[line $line] Error: Unterminated string."
            else -> "[line $line] Error: Unexpected character: $currentChar"
        }
        System.err.println(errorMessage)
        errorOccurred = true
        currentIndex++
        currentColumn++
    }

    private fun tokenizeIdentifier() {
        val start = currentIndex
        while (currentIndex < source.length && (source[currentIndex].isLetterOrDigit() || source[currentIndex] == '_')) {
            currentIndex++
            currentColumn++
        }
        val identifier = source.substring(start, currentIndex)
        tokens.add(Token(TokenType.IDENTIFIER, identifier, currentLine, currentColumn))
    }

    private fun Char.isLetterOrUnderscore(): Boolean {
        return this.isLetter() || this == '_'
    }

    fun getTokens(): List<Token> {
        return tokens
    }

    fun hasError(): Boolean {
        return errorOccurred
    }
}
