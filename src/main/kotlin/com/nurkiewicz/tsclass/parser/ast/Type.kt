package com.nurkiewicz.tsclass.parser.ast

data class Type (val name: String) {

    companion object {
        val number = Type("number")
    }

    fun toJavaType(): org.objectweb.asm.Type {
        when (name) {
            "number" -> return org.objectweb.asm.Type.DOUBLE_TYPE
            "string" -> return org.objectweb.asm.Type.getType(String::class.java)
            else -> throw IllegalArgumentException("Uknown type: " + name)
        }
    }

    override fun toString() = name
}
