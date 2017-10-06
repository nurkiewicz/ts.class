package com.nurkiewicz.tsclass.parser.ast.expr

data class Identifier(val name: String) : Expression {

    override fun toString(): String {
        return name
    }

    companion object {

        @JvmStatic
        fun ident(name: String): Identifier {
            return Identifier(name)
        }
    }

}
