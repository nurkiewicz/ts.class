package com.nurkiewicz.tsclass.codegen

data class GenCode(val bytecode: List<Bytecode>, val current: SymbolTable) {
    fun prepend(b: List<Bytecode>) = GenCode(b + bytecode, current)
    fun append(b: List<Bytecode>) = GenCode(bytecode + b, current)
}