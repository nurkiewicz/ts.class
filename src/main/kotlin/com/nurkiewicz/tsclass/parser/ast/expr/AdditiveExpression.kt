package com.nurkiewicz.tsclass.parser.ast.expr

data class AdditiveExpression(val left: Expression, val operator: Operator, val right: Expression): Expression {

    override fun toString(): String {
        return "($left $operator $right)"
    }

    enum class Operator private constructor(private val s: String) {
        PLUS("+"), MINUS("-");

        override fun toString(): String {
            return s
        }

        companion object {


            fun of(s: String): Operator {
                when (s) {
                    "+" -> return PLUS
                    "-" -> return MINUS
                    else -> throw IllegalArgumentException(s)
                }
            }
        }

    }

    companion object {

        @JvmStatic
        fun add(left: Expression, right: Expression): AdditiveExpression {
            return AdditiveExpression(left, Operator.PLUS, right)
        }

        @JvmStatic
        fun sub(left: Expression, right: Expression): AdditiveExpression {
            return AdditiveExpression(left, Operator.MINUS, right)
        }
    }

}
