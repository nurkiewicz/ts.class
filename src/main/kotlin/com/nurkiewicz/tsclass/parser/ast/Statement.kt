package com.nurkiewicz.tsclass.parser.ast

import com.nurkiewicz.tsclass.parser.ast.expr.Expression

sealed class Statement

data class ReturnStatement(val expression: Expression): Statement() {
    companion object {
        @JvmStatic
        fun ret(expression: Expression) = ReturnStatement(expression)
    }
}
