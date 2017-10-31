package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.codegen.Bytecode.NoArg
import com.nurkiewicz.tsclass.parser.ast.Block
import com.nurkiewicz.tsclass.parser.ast.If
import com.nurkiewicz.tsclass.parser.ast.Return
import com.nurkiewicz.tsclass.parser.ast.Statement
import com.nurkiewicz.tsclass.parser.ast.VarAssignment
import org.objectweb.asm.Opcodes

class StatementGenerator(
        private val expressionGenerator: ExpressionGenerator,
        private val ifGenerator: IfGenerator,
        private val assignmentGenerator: AssignmentGenerator
        ) {

    fun generate(statement: Statement, tab: SymbolTable): GenCode {
        return when (statement) {
            is Return ->
                GenCode(expressionGenerator.generate(statement.expression, tab) + NoArg(Opcodes.DRETURN), tab)
            is If ->
                ifGenerator.generate(statement, tab, this)
            is Block ->
                buildForAllStatements(statement, tab)
            is VarAssignment ->
                assignmentGenerator.generate(statement, tab)
        }
    }

    /**
     * Every statement inherits symbol table that might have been modified by previous statement
     */
    private fun buildForAllStatements(statement: Block, tab: SymbolTable) =
            statement.statements.fold(
                    GenCode(listOf(), tab),
                    this::appendBytecode)

    private fun appendBytecode(g: GenCode, s: Statement): GenCode {
        val (bytecode, nextSymTab) = this.generate(s, g.current)
        return GenCode(g.bytecode + bytecode, nextSymTab)
    }

    companion object {
        @JvmStatic
        fun build(): StatementGenerator {
            val expressionGenerator = ExpressionGenerator()
            return StatementGenerator(
                    expressionGenerator,
                    IfGenerator(expressionGenerator),
                    AssignmentGenerator(expressionGenerator)
            )
        }
    }
}

