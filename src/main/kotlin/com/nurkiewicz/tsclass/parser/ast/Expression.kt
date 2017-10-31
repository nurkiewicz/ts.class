package com.nurkiewicz.tsclass.parser.ast

sealed class Expression

data class AdditiveExpression(val left: Expression, val operator: Operator, val right: Expression): Expression() {

    override fun toString(): String {
        return "($left $operator $right)"
    }

    enum class Operator(private val s: String) {
        PLUS("+"), MINUS("-");

        override fun toString() = s

        companion object {


            fun of(s: String) = when (s) {
                "+" -> PLUS
                "-" -> MINUS
                else -> throw IllegalArgumentException(s)
            }
        }

    }

}

data class Identifier(val name: String) : Expression() {

    override fun toString() = name

}

data class MethodCall(val name: String, val parameters: List<Expression>): Expression()

data class MultiplicativeExpression(val left: Expression, val operator: Operator, val right: Expression): Expression() {

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

}

data class Neg(val expression: Expression): Expression() {

    override fun toString() = "-($expression)"

}

data class NumberLiteral(val value: Double) : Expression() {

    override fun toString() = value.toString()

}

data class Relational(val left: Expression, val operator: Operator, val right: Expression) : Expression() {

    override fun toString() = "($left) $operator ($right)"

    enum class Operator(private val s: String) {
        GT(">"), GTE(">="), LT("<"), LTE("<=");

        override fun toString() = s

        companion object {

            fun of(s: String): Operator {
                return when (s) {
                    ">" -> GT
                    ">=" -> GTE
                    "<" -> LT
                    "<=" -> LTE
                    else -> throw IllegalArgumentException("Unsupported operator $s")
                }
            }


        }
    }

}