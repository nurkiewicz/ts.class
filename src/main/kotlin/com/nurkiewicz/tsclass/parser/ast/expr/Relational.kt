package com.nurkiewicz.tsclass.parser.ast.expr

data class Relational(val left: Expression, val operator: Operator, val right: Expression) : Expression {

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

    companion object {

        @JvmStatic
        fun gt(left: Expression, right: Expression) =
                Relational(left, Operator.GT, right)

        @JvmStatic
        fun gte(left: Expression, right: Expression) =
                Relational(left, Operator.GTE, right)

        @JvmStatic
        fun lt(left: Expression, right: Expression) =
                Relational(left, Operator.LT, right)

        @JvmStatic
        fun lte(left: Expression, right: Expression) =
                Relational(left, Operator.LTE, right)
    }


}