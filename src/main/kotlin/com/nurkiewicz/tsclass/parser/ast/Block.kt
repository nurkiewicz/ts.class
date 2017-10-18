package com.nurkiewicz.tsclass.parser.ast


data class Block(val statements: List<Statement>) {

    override fun toString() = statements.toString()

    companion object {

        @JvmStatic
        fun block(statements: List<Statement>) = Block(statements)

    }

}