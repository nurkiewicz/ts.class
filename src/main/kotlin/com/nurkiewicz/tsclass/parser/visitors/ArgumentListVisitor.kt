package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.expr.Expression

class ArgumentListVisitor: TypeScriptBaseVisitor<List<Expression>>() {

//    override fun visitArgumentList(ctx: TypeScriptParser.ArgumentListContext): List<Expression> {
//        println(ctx.text)
//        return super.visitArgumentList(ctx)
//    }

    override fun defaultResult(): List<Expression> {
        return emptyList()
    }

    override fun visitAssignmentExpression(ctx: TypeScriptParser.AssignmentExpressionContext): List<Expression> {
        println(ctx.text)
        return listOf(ctx.accept(ExpressionVisitor()))
    }

    override fun aggregateResult(aggregate: List<Expression>, nextResult: List<Expression>): List<Expression> {
        return aggregate + nextResult
    }
}