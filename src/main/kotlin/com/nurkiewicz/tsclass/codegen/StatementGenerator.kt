package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.codegen.Bytecode.NoArg
import com.nurkiewicz.tsclass.parser.ast.ReturnStatement
import com.nurkiewicz.tsclass.parser.ast.Statement
import org.objectweb.asm.Opcodes

class StatementGenerator(private val expressionGenerator: ExpressionGenerator) {

    fun generate(statement: Statement, tab: SymbolTable): List<Bytecode> {
        return when (statement) {
            is ReturnStatement -> {
                val bytecode = expressionGenerator.generate(statement.expression, tab)
                bytecode + NoArg(Opcodes.DRETURN)
            }
        }

    }
}

sealed class Bytecode {
    data class NoArg(val code: Int) : Bytecode()
    data class IntArg(val code: Int, val arg: Int) : Bytecode()
    data class DoubleArg(val code: Int, val arg: Double) : Bytecode()
}

