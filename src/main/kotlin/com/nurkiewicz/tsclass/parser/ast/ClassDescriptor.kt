package com.nurkiewicz.tsclass.parser.ast

import com.nurkiewicz.tsclass.parser.ast.expr.MethodCall

data class ClassDescriptor(
    val name: String,
    val fields: List<Field>,
    val methods: List<Method>) {

    fun findBestMatchingMethod(call: MethodCall): Method? =
            methods.find { it.matches(call) }

}

data class Field(val name: String, val type: String)
