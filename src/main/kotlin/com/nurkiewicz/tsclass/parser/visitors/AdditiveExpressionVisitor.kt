package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.expr.AdditiveExpression
import com.nurkiewicz.tsclass.parser.ast.expr.Expression

internal class AdditiveExpressionVisitor : TypeScriptBaseVisitor<Expression>() {
    override fun visitAdditiveExpression(ctx: TypeScriptParser.AdditiveExpressionContext): Expression {
        val right = ctx.accept(MultiplicativeExpressionVisitor())
        if (ctx.additiveExpression() != null) {
            val left = ctx.additiveExpression().accept(this)
            val op = AdditiveExpression.Operator.of(ctx.op.getText())
            return AdditiveExpression(left, op, right)
        }
        return right
    }

}
