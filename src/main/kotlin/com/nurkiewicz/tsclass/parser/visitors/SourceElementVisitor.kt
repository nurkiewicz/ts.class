package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.ReturnStatement
import com.nurkiewicz.tsclass.parser.ast.Statement

internal class SourceElementVisitor : TypeScriptBaseVisitor<Statement>() {

    override fun visitReturnStatement(ctx: TypeScriptParser.ReturnStatementContext): Statement {
        val expression = ctx.expression().accept(ExpressionVisitor())
        return ReturnStatement(expression)
    }

}
