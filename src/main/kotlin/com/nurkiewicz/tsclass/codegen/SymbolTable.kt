package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import com.nurkiewicz.tsclass.parser.ast.Method
import com.nurkiewicz.tsclass.parser.ast.Parameter
import com.nurkiewicz.tsclass.parser.ast.Type


interface SymbolTable {
    fun lookup(symbol: String): Symbol?
    fun currentClass(): ClassDescriptor
    fun nextLocalOffset(): Int
}

class Empty: SymbolTable {
    override fun lookup(symbol: String) = null
    override fun currentClass() = throw IllegalStateException("Outer class unknown")
    override fun nextLocalOffset() = throw IllegalStateException("No local stack")
}

data class MethodParameters(
        private val symbols: Map<String, Symbol.MethodParameter>,
        private val parent: SymbolTable
): SymbolTable {

    constructor(m: Method, parent: SymbolTable): this(paramsToSymbols(m), parent)

    override fun lookup(symbol: String): Symbol? =
            symbols[symbol] ?: parent.lookup(symbol)

    override fun currentClass() = parent.currentClass()

    override fun nextLocalOffset()
            = symbols.values.map{it.offset + it.type.stackSize}.max() ?: 1

    companion object {
        private fun paramsToSymbols(method: Method): Map<String, Symbol.MethodParameter> = method
                .parameters
                .mapIndexed { idx, param -> Pair(param.name, symbol(idx, param)) }
                .toMap()

        private fun symbol(i: Int, param: Parameter) =
                Symbol.MethodParameter(i * DOUBLE_SIZE + 1, param.type)

        private val DOUBLE_SIZE = 2
    }

}

data class ClassSymbols(private val classDescriptor: ClassDescriptor, private val parent: SymbolTable): SymbolTable {
    override fun nextLocalOffset() = parent.nextLocalOffset()

    override fun lookup(symbol: String) = parent.lookup(symbol)

    override fun currentClass() = classDescriptor

}

sealed class Symbol {
    data class MethodParameter(val offset: Int, val type: Type): Symbol()
    data class LocalVariable(val offset: Int, val type: Type): Symbol()
}