package com.nurkiewicz.tsclass.parser.ast

import com.nurkiewicz.tsclass.parser.ast.expr.Expression

sealed class Statement

data class Return(val expression: Expression): Statement() {

    override fun toString() = "return $expression"

}

data class If(val condition: Expression, val block: Statement, val elseBlock: Statement?): Statement()

data class Block(val statements: List<Statement>): Statement() {

    override fun toString() = statements.toString()

}