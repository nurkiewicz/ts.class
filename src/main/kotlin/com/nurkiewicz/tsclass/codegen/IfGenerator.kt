package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.parser.ast.If
import com.nurkiewicz.tsclass.parser.ast.Statement
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes

class IfGenerator(private val expressionGenerator: ExpressionGenerator ) {

    fun generate(ifs: If, tab: SymbolTable, statementGenerator: StatementGenerator): GenCode {
        val condition = expressionGenerator.generate(ifs.condition, tab)
        val elseLabelPlace = Bytecode.LabelPlace((condition.last() as Bytecode.Jump).label)
        val ifBlock = blockToBytecode(ifs.block, tab, statementGenerator)
        val affirmativePart = ifBlock.prepend(condition)
        return if (ifs.elseBlock != null) {
            val endOfElse = Label()
            val elseBytecode = blockToBytecode(ifs.elseBlock, tab, statementGenerator)
            val negativePart = listOf(Bytecode.Jump(Opcodes.GOTO, endOfElse)) + elseLabelPlace + elseBytecode.bytecode + Bytecode.LabelPlace(endOfElse)
            affirmativePart.append(negativePart)
        } else {
            affirmativePart.append(listOf(elseLabelPlace))
        }
    }

    private fun blockToBytecode(statement: Statement, tab: SymbolTable, statementGenerator: StatementGenerator) =
            statementGenerator.generate(statement, tab)

}