package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.Expression
import com.nurkiewicz.tsclass.parser.ast.Identifier
import com.nurkiewicz.tsclass.parser.ast.MethodCall

internal class LeftHandSideExpressionVisitor: TypeScriptBaseVisitor<Expression>() {

    override fun visitLeftHandSideExpression(ctx: TypeScriptParser.LeftHandSideExpressionContext): Expression {
        val call = ctx.callExpression()
        if (call != null) {
            val identifier = call.memberExpression().accept(MemberExpressionVisitor()) as Identifier
            val arguments = if (call.arguments() != null)
                call.arguments().accept(ArgumentListVisitor())
            else
                emptyList()
            return MethodCall(identifier.name, arguments)
        } else {
            return ctx.accept(MemberExpressionVisitor())
        }
    }
}