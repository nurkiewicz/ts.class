package com.nurkiewicz.tsclass.parser.ast.expr

data class Neg(val expression: Expression): Expression {

    override fun toString() = "-($expression)"

    companion object {

        @JvmStatic
        fun neg(x: Double) = Neg(NumberLiteral(x))

        @JvmStatic
        fun neg(x: String) = Neg(Identifier(x))

        @JvmStatic
        fun neg(e: Expression) = Neg(e)
    }

}