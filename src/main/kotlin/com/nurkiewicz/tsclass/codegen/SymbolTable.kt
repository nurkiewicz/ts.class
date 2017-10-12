package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.parser.ast.Method
import com.nurkiewicz.tsclass.parser.ast.Parameter
import com.nurkiewicz.tsclass.parser.ast.Type

data class SymbolTable(
        private val symbols: Map<String, Symbol>,
        private val parent: SymbolTable?
) {

    companion object {
        fun from(m: Method, parent: SymbolTable): SymbolTable {
            return SymbolTable(paramsToSymbols(m), parent)
        }

        private fun paramsToSymbols(method: Method): Map<String, Symbol> = method
                    .parameters
                    .mapIndexed { idx, param -> Pair(param.name, symbol(idx, param)) }
                    .toMap()

        private fun symbol(i: Int, param: Parameter) =
                Symbol.MethodParameter(i * 2 + 1, param.type)
    }

    fun lookup(symbol: String): Symbol? =
            symbols[symbol] ?: parent?.lookup(symbol)
}

sealed class Symbol {
    data class MethodParameter(val offset: Int, val type: Type): Symbol()
}