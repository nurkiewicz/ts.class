package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.parser.ast.Block
import com.nurkiewicz.tsclass.parser.ast.If
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes

class IfGenerator(private val expressionGenerator: ExpressionGenerator ) {

    fun generate(ifs: If, tab: SymbolTable, statementGenerator: StatementGenerator): List<Bytecode> {
        val condition = expressionGenerator.generate(ifs.condition, tab)
        val elseLabelPlace = Bytecode.LabelPlace((condition.last() as Bytecode.Jump).label)
        val ifBlock = blockToBytecode(ifs.block, tab, statementGenerator)
        val affirmativePart = condition + ifBlock
        return if (ifs.elseBlock != null) {
            val endOfElse = Label()
            val elseBytecode = blockToBytecode(ifs.elseBlock, tab, statementGenerator)
            affirmativePart + Bytecode.Jump(Opcodes.GOTO, endOfElse) + elseLabelPlace + elseBytecode + Bytecode.LabelPlace(endOfElse)
        } else {
            affirmativePart + elseLabelPlace
        }
    }

    private fun blockToBytecode(block: Block, tab: SymbolTable, statementGenerator: StatementGenerator) =
            block.statements.flatMap { statementGenerator.generate(it, tab) }

}