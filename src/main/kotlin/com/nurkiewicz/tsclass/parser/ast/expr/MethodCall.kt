package com.nurkiewicz.tsclass.parser.ast.expr

data class MethodCall(val name: String, val parameters: List<Expression>): Expression {
    companion object {

        @JvmStatic
        fun call(name: String, vararg parameters: Expression): MethodCall {
            return MethodCall(name, parameters.toList())
        }
    }

}