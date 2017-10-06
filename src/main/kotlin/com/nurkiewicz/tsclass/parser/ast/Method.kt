package com.nurkiewicz.tsclass.parser.ast

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
}

data class Parameter(val name: String, val type: Type)

