package com.nurkiewicz.tsclass.parser.ast

import com.google.common.collect.ImmutableList
import java.util.stream.Collectors

data class Method(
        val name: String,
        val type: Type,
        val parameters: ImmutableList<Parameter>,
        val statements: ImmutableList<Statement>) {

    fun methodDescriptor(): String {
        return org.objectweb.asm.Type.getMethodDescriptor(type.toJavaType(), *paramJavaTypes)
    }

    private val paramJavaTypes: Array<org.objectweb.asm.Type>
        get() = parameters
                .stream()
                .map { it.type }
                .map { it.toJavaType() }
                .collect(Collectors.toList())
                .toTypedArray()
}
