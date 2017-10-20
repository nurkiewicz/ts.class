package com.nurkiewicz.tsclass.codegen.asm

import com.nurkiewicz.tsclass.codegen.Bytecode
import org.objectweb.asm.MethodVisitor

class MethodEmitter(
        private val callEmitter: CallEmitter
) {

    fun emitBytecode(writer: MethodVisitor, bytecode: List<Bytecode>) {
        bytecode.forEach { emitInstruction(it, writer) }
    }

    private fun emitInstruction(c: Bytecode, writer: MethodVisitor) {
        when (c) {
            is Bytecode.NoArg ->
                writer.visitInsn(c.code)
            is Bytecode.IntArg ->
                writer.visitVarInsn(c.code, c.arg)
            is Bytecode.DoubleArg ->
                writer.visitLdcInsn(c.arg)
            is Bytecode.Call ->
                callEmitter.emit(writer, c)
            is Bytecode.Jump ->
                writer.visitJumpInsn(c.code, c.label)
            is Bytecode.LabelPlace ->
                writer.visitLabel(c.label)
        }.let {}  //force compilation error when not exhaustive
    }

    companion object {
        @JvmStatic
        fun build() = MethodEmitter(CallEmitter())
    }

}

