package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.Return
import com.nurkiewicz.tsclass.parser.ast.Statement

internal class StatementVisitor : TypeScriptBaseVisitor<Statement>() {

    override fun visitReturnStatement(ctx: TypeScriptParser.ReturnStatementContext): Statement {
        val expression = ctx.expression().accept(ExpressionVisitor())
        return Return(expression)
    }

    override fun visitIfStatement(ctx: TypeScriptParser.IfStatementContext?): Statement {
        println("ifStatement")
        return super.visitIfStatement(ctx)
    }
}
