package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.codegen.Bytecode.NoArg
import com.nurkiewicz.tsclass.parser.ast.ReturnStatement
import com.nurkiewicz.tsclass.parser.ast.Statement
import org.objectweb.asm.Opcodes

class StatementGenerator(private val expressionGenerator: ExpressionGenerator) {

    fun generate(statement: Statement, tab: SymbolTable): List<Bytecode> {
        return when (statement) {
            is ReturnStatement -> {
                expressionGenerator.generate(statement.expression, tab) + NoArg(Opcodes.DRETURN)
            }
        }
    }
}

