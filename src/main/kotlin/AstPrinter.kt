import kotlin.system.exitProcess

class AstPrinter : ExprVisitor<String> {
    private var errorState: Boolean = false

    fun print(expr: Expr): String {
        return expr.accept(this)
    }

    fun hasError(): Boolean {
        return errorState
    }

    override fun visitBinaryExpr(expr: Expr.Binary): String {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right)
    }

    override fun visitLiteralExpr(expr: Expr.Literal): String {
        return when (val value = expr.value) {
            is Boolean -> value.toString()
            is String -> {
                if (value.isNumeric()) {
                    val numericValue = value.toDouble()
                    if (numericValue.isInt()) {
                        "${numericValue.toInt()}.0"
                    } else {
                        numericValue.toString()
                    }
                } else if (value.isString()) {
                    value.substring(1, value.length - 1)
                } else {
                    value // Assuming other literals like boolean or nil are already handled
                }
            }

            else -> "nil"
        }
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): String {
        return parenthesize("group", expr.expression)
    }

    override fun visitUnaryExpr(expr: Expr.Unary): String {
        return parenthesize(expr.operator.lexeme, expr.right)
    }

    private fun parenthesize(name: String, vararg exprs: Expr): String {
        val builder = StringBuilder()
        builder.append("(").append(name)
        for (expr in exprs) {
            builder.append(" ")
            builder.append(expr.accept(this))
        }
        builder.append(")")
        return builder.toString()
    }

    private fun Double.isInt(): Boolean {
        return this % 1 == 0.0
    }

    private fun String.isString(): Boolean {
        return this.startsWith("\"") && this.endsWith("\"")
    }

    private fun String.isNumeric(): Boolean {
        return this.toDoubleOrNull() != null
    }

    private fun String.isBoolean(): Boolean {
        return this == "true" || this == "false"
    }

    private fun String.isNil(): Boolean {
        return this == "nil"
    }

    fun checkParenthesesBalance(source: String) {
        var openParenCount = 0
        var closeParenCount = 0

        for (char in source) {
            when (char) {
                '(' -> openParenCount++
                ')' -> closeParenCount++
            }
        }

        if (openParenCount != closeParenCount) {
            errorState = true
            System.err.println("Error: Unbalanced parentheses")
            exitProcess(65)
        }
    }
}


fun Expr.accept(visitor: ExprVisitor<String>): String {
    return when (this) {
        is Expr.Binary -> visitor.visitBinaryExpr(this)
        is Expr.Literal -> visitor.visitLiteralExpr(this)
        is Expr.Grouping -> visitor.visitGroupingExpr(this)
        is Expr.Unary -> visitor.visitUnaryExpr(this)
    }
}
