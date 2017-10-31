package com.nurkiewicz.tsclass.parser.ast

import com.nurkiewicz.tsclass.parser.ast.expr.Expression

sealed class Statement

data class Return(val expression: Expression): Statement() {

    override fun toString() = "return $expression"

    companion object {
        @JvmStatic
        fun ret(expression: Expression) = Return(expression)
    }
}

data class If(val condition: Expression, val block: Statement, val elseBlock: Statement?): Statement()

data class Block(val statements: List<Statement>): Statement() {

    override fun toString() = statements.toString()

    companion object {

        @JvmStatic
        fun block(statements: List<Statement>) = Block(statements)

        @JvmStatic
        fun block() = block(emptyList())

    }

}