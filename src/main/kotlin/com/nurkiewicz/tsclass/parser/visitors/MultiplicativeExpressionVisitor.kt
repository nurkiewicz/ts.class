package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.Expression
import com.nurkiewicz.tsclass.parser.ast.MultiplicativeExpression

internal class MultiplicativeExpressionVisitor : TypeScriptBaseVisitor<Expression>() {

    override fun visitMultiplicativeExpression(ctx: TypeScriptParser.MultiplicativeExpressionContext): Expression {
        val right = ctx.accept(UnaryExpressionVisitor())
        if (ctx.multiplicativeExpression() != null) {
            val left = ctx.multiplicativeExpression().accept(this)
            val op = MultiplicativeExpression.Operator.of(ctx.op.getText())
            return MultiplicativeExpression(left, op, right)
        }
        return right
    }

}
