interface ExprVisitor<R> {
    fun visitBinaryExpr(expr: Expr.Binary): R
    fun visitLiteralExpr(expr: Expr.Literal): R
    fun visitGroupingExpr(expr: Expr.Grouping): R
    fun visitUnaryExpr(expr: Expr.Unary): R
}