package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.codegen.Bytecode.IntArg
import com.nurkiewicz.tsclass.parser.ast.VarAssignment
import org.objectweb.asm.Opcodes

class AssignmentGenerator(private val expressionGenerator: ExpressionGenerator) {

    fun generate(assignment: VarAssignment, tab: SymbolTable): GenCode {
        val expr = expressionGenerator.generate(assignment.initial, tab)
        val offset = tab.nextLocalOffset()
        val bytecode = expr + IntArg(Opcodes.DSTORE, offset)
        val local = Symbol.LocalVariable(offset, assignment.type)
        val symbolsWithNewLocal = LocalVariables(assignment.identifier, local, tab)
        return GenCode(bytecode, symbolsWithNewLocal)
    }

}