package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.Statement

internal class StatementListVisitor : TypeScriptBaseVisitor<List<Statement>>() {

    override fun visitStatementList(ctx: TypeScriptParser.StatementListContext): List<Statement> {
        val statement = ctx.statement().accept(StatementVisitor())
        val statementList = ctx.statementList()?.accept(this) ?: listOf()
        return statementList + statement
    }
}