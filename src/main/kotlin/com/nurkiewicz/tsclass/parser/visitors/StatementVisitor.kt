package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.Block
import com.nurkiewicz.tsclass.parser.ast.If
import com.nurkiewicz.tsclass.parser.ast.Return
import com.nurkiewicz.tsclass.parser.ast.Statement

internal class StatementVisitor : TypeScriptBaseVisitor<Statement>() {

    override fun visitReturnStatement(ctx: TypeScriptParser.ReturnStatementContext): Statement {
        val expression = ctx.expression().accept(ExpressionVisitor())
        return Return(expression)
    }

    override fun visitIfStatement(ctx: TypeScriptParser.IfStatementContext): Statement {
        return If(
                ctx.expression().accept(ExpressionVisitor()),
                ctx.ifBlock.accept(this),
                ctx.elseBlock?.accept(this))
    }

    override fun visitBlock(ctx: TypeScriptParser.BlockContext): Block = ctx.accept(BlockVisitor())
}
