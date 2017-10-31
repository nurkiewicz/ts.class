package com.nurkiewicz.tsclass.codegen

class LocalVariables(private val name: String, private val local: Symbol.LocalVariable, private val parent: SymbolTable) : SymbolTable {
    override fun lookup(symbol: String) =
            when (symbol) {
                name -> local
                else -> parent.lookup(symbol)
            }

    override fun currentClass() = parent.currentClass()

    override fun nextLocalOffset() = parent.nextLocalOffset() + local.type.stackSize
}