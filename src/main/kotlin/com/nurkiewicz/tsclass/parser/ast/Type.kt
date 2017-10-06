package com.nurkiewicz.tsclass.parser.ast

data class Type (val name: String) {

    fun toJavaType(): org.objectweb.asm.Type {
        when (name) {
            "number" -> return org.objectweb.asm.Type.DOUBLE_TYPE
            "string" -> return org.objectweb.asm.Type.getType(String::class.java)
            else -> throw IllegalArgumentException("Uknown type: " + name)
        }
    }

}
