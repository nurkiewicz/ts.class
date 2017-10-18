package com.nurkiewicz.tsclass.parser.ast.expr

import com.nurkiewicz.tsclass.parser.ast.expr.MultiplicativeExpression.Operator.DIV
import com.nurkiewicz.tsclass.parser.ast.expr.MultiplicativeExpression.Operator.MOD
import com.nurkiewicz.tsclass.parser.ast.expr.MultiplicativeExpression.Operator.MUL

data class MultiplicativeExpression(val left: Expression, val operator: Operator, val right: Expression): Expression {

    override fun toString(): String {
        return "($left $operator $right)"
    }

    enum class Operator(private val s: String) {
        MUL("*"), DIV("/"), MOD("%");


        override fun toString(): String {
            return s
        }

        companion object {

            fun of(s: String) = when (s) {
                "*" -> MUL
                "/" -> DIV
                "%" -> MOD
                else -> throw IllegalArgumentException(s)
            }
        }
    }

    companion object {

        @JvmStatic
        fun mul(left: Expression, right: Expression): MultiplicativeExpression {
            return MultiplicativeExpression(left, MUL, right)
        }

        @JvmStatic
        fun div(left: Expression, right: Expression): MultiplicativeExpression {
            return MultiplicativeExpression(left, DIV, right)
        }

        @JvmStatic
        fun mod(left: Expression, right: Expression): MultiplicativeExpression {
            return MultiplicativeExpression(left, MOD, right)
        }
    }


}
