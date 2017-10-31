package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.Expression
import com.nurkiewicz.tsclass.parser.ast.Relational

internal object ExpressionVisitor : TypeScriptBaseVisitor<Expression>() {

    override fun visitRelationalExpression(ctx: TypeScriptParser.RelationalExpressionContext): Expression {
        return if (ctx.relationalOperator() != null) {
            val left = ctx.relationalExpression().accept(this)
            val right = ctx.shiftExpression().accept(AdditiveExpressionVisitor())
            val operator = Relational.Operator.of(ctx.relationalOperator().text)
            Relational(left, operator, right)
        } else {
            ctx.accept(AdditiveExpressionVisitor())
        }
    }
}