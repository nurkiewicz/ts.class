package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.Block
import com.nurkiewicz.tsclass.parser.ast.If
import com.nurkiewicz.tsclass.parser.ast.Return
import com.nurkiewicz.tsclass.parser.ast.Statement
import com.nurkiewicz.tsclass.parser.ast.expr.Expression

internal class StatementVisitor : TypeScriptBaseVisitor<Statement>() {

    override fun visitReturnStatement(ctx: TypeScriptParser.ReturnStatementContext): Statement {
        val expression = ctx.expression().accept(ExpressionVisitor())
        return Return(expression)
    }

    override fun visitIfStatement(ctx: TypeScriptParser.IfStatementContext): Statement {
        println("if " + ctx.text)
        val condition: Expression = ctx.expression().accept(ExpressionVisitor())
        val ifBlock: Statement = ctx.ifBlock.accept(this)
        val elseBlock: Block? = when(ctx.elseBlock) {
            null -> null
            else -> Block(listOf(ctx.elseBlock.accept(this)))
        }
        return If(condition, Block(listOf(ifBlock)), elseBlock)
    }
}
