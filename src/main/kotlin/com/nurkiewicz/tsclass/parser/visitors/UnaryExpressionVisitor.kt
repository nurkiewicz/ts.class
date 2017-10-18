package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.expr.Expression
import com.nurkiewicz.tsclass.parser.ast.expr.Neg

class UnaryExpressionVisitor : TypeScriptBaseVisitor<Expression>() {

    override fun visitUnaryExpression(ctx: TypeScriptParser.UnaryExpressionContext): Expression {
        if (ctx.postfixExpression() != null) {
            return ctx.postfixExpression().accept(LeftHandSideExpressionVisitor())
        }
        val expression = ctx.unaryExpression().accept(this)
        return when(ctx.unaryOperator().text) {
            "-" -> Neg(expression)
            else -> throw IllegalArgumentException("Unsupported operator ")
        }
    }
}