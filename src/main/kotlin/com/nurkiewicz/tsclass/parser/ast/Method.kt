package com.nurkiewicz.tsclass.parser.ast

import com.nurkiewicz.tsclass.parser.ast.expr.MethodCall

data class Method(
        val name: String,
        val type: Type,
        val parameters: List<Parameter>,
        val statements: List<Statement>) {

    fun methodDescriptor(): String {
        return org.objectweb.asm.Type.getMethodDescriptor(type.toJavaType(), *paramJavaTypes)
    }

    private val paramJavaTypes: Array<org.objectweb.asm.Type> =
            parameters
                .map { it.type }
                .map { it.toJavaType() }
                .toTypedArray()

    fun matches(call: MethodCall) =
            this.name == call.name && parameterTypesMatching(call)

    //TODO Also check call types, not only count
    private fun parameterTypesMatching(call: MethodCall) =
            this.parameters.size == call.parameters.size
}

data class Parameter(val name: String, val type: Type) {
    override fun toString() = "$name: $type"
}

