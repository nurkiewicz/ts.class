package com.nurkiewicz.tsclass.parser.ast

data class Type(val name: String, val stackSize: Int) {

    constructor(name: String?) : this(
            name ?: "void",
            if(name == "number") 2 else 1
    )

    fun toJavaType(): org.objectweb.asm.Type {
        return when (name) {
            "number" -> org.objectweb.asm.Type.DOUBLE_TYPE
            "string" -> org.objectweb.asm.Type.getType(String::class.java)
            else -> throw IllegalArgumentException("Uknown type: " + name)
        }
    }

    override fun toString() = "$name[$stackSize]"

    companion object {
        val number = Type("number", 2)
    }
}
