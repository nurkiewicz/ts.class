package com.nurkiewicz.tsclass.codegen

import org.objectweb.asm.MethodVisitor

class AsmEmitter {

    fun emitBytecode(writer: MethodVisitor, bytecode: List<Bytecode>) {
        for (instr in bytecode) {
            when(instr) {
                is Bytecode.NoArg ->
                    writer.visitInsn(instr.code)
            }
        }
    }

}