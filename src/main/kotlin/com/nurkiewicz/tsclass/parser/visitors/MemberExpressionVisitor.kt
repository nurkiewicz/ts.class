package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.Expression
import com.nurkiewicz.tsclass.parser.ast.Identifier
import com.nurkiewicz.tsclass.parser.ast.NumberLiteral

internal class MemberExpressionVisitor : TypeScriptBaseVisitor<Expression>() {
    override fun visitPrimaryExpression(ctx: TypeScriptParser.PrimaryExpressionContext): Expression {
        if (ctx.openParen() != null) {
            return ctx.expression().accept(ExpressionVisitor)
        }
        return if (ctx.IDENT() != null) {
            Identifier(ctx.IDENT().text)
        } else ctx.accept(object : TypeScriptBaseVisitor<Expression>() {
            override fun visitLiteral(ctx: TypeScriptParser.LiteralContext): Expression {
                return NumberLiteral(ctx.NUMERIC_LITERAL().text.toDouble())
            }
        })
    }
}