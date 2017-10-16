package com.nurkiewicz.tsclass.parser.ast

import com.nurkiewicz.tsclass.parser.ast.expr.Expression

sealed class Statement

data class Return(val expression: Expression): Statement() {
    companion object {
        @JvmStatic
        fun ret(expression: Expression) = Return(expression)
    }
}

data class If(val condition: Expression, val block: Block, val elseBlock: Block?): Statement()