package com.nurkiewicz.tsclass.codegen.asm

import com.nurkiewicz.tsclass.codegen.Bytecode
import org.objectweb.asm.MethodVisitor

class MethodEmitter(
        private val callEmitter: CallEmitter
) {

    fun emitBytecode(writer: MethodVisitor, bytecode: List<Bytecode>) {
        bytecode.forEach { emitInstruction(it, writer) }
    }

    private fun emitInstruction(instr: Bytecode, writer: MethodVisitor) {
        when (instr) {
            is Bytecode.NoArg ->
                writer.visitInsn(instr.code)
            is Bytecode.IntArg ->
                writer.visitVarInsn(instr.code, instr.arg)
            is Bytecode.DoubleArg ->
                writer.visitLdcInsn(instr.arg)
            is Bytecode.Call ->
                callEmitter.emit(writer, instr)
        }.let {}  //force compilation error when not exhaustive
    }
}

