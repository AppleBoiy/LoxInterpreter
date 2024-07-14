interface ExprVisitor<R> {
    fun visitBinaryExpr(expr: Expr.Binary): R
    fun visitLiteralExpr(expr: Expr.Literal): R
    fun visitGroupingExpr(expr: Expr.Grouping): R
    fun visitUnaryExpr(expr: Expr.Unary): R
}

class AstPrinter : ExprVisitor<String> {
    fun print(expr: Expr): String {
        return expr.accept(this)
    }

    override fun visitBinaryExpr(expr: Expr.Binary): String {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right)
    }

    override fun visitLiteralExpr(expr: Expr.Literal): String {
        return when (val lexeme = expr.value) {
            is Boolean -> lexeme.toString()
            is String -> {
                val value = lexeme.toDoubleOrNull()
                when {
                    value != null -> {
                        if (value.isInt()) {
                            "${value.toInt()}.0"
                        } else {
                            value.toString()
                        }
                    }

                    else -> lexeme.toString()
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
}

fun Expr.accept(visitor: ExprVisitor<String>): String {
    return when (this) {
        is Expr.Binary -> visitor.visitBinaryExpr(this)
        is Expr.Literal -> visitor.visitLiteralExpr(this)
        is Expr.Grouping -> visitor.visitGroupingExpr(this)
        is Expr.Unary -> visitor.visitUnaryExpr(this)
    }
}
