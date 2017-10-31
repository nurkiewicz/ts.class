package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.Block

internal class BlockVisitor: TypeScriptBaseVisitor<Block>() {

    override fun visitBlock(ctx: TypeScriptParser.BlockContext): Block {
        val statementList = ctx.statementList()
        return when(statementList) {
            null -> Block.block()
            else -> Block(statementList.accept(StatementListVisitor()))
        }
    }
}