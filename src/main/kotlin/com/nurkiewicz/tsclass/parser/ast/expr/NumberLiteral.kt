package com.nurkiewicz.tsclass.parser.ast.expr

data class NumberLiteral(val value: Double) : Expression {

    override fun toString(): String {
        return value.toString()
    }

    companion object {

        @JvmStatic
        fun num(x: Double): NumberLiteral {
            return NumberLiteral(x)
        }
    }
}
