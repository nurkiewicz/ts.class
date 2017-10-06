package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.expr.Expression

internal class ExpressionVisitor : TypeScriptBaseVisitor<Expression>() {

    override fun visitAssignmentExpression(ctx: TypeScriptParser.AssignmentExpressionContext): Expression {
        return ctx.accept(AdditiveExpressionVisitor())
    }

}