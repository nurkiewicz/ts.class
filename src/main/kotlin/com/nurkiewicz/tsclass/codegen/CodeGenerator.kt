package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.codegen.asm.MethodEmitter
import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import com.nurkiewicz.tsclass.parser.ast.Method
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.ClassWriter.COMPUTE_FRAMES
import org.objectweb.asm.ClassWriter.COMPUTE_MAXS
import org.objectweb.asm.Opcodes.ACC_PUBLIC
import org.objectweb.asm.Opcodes.ACC_SUPER
import org.objectweb.asm.Opcodes.ALOAD
import org.objectweb.asm.Opcodes.INVOKESPECIAL
import org.objectweb.asm.Opcodes.RETURN

class CodeGenerator(
        private val statementGenerator: StatementGenerator,
        private val methodEmitter: MethodEmitter
) {

    fun generate(cls: ClassDescriptor): ByteArray {
        val writer = ClassWriter(COMPUTE_MAXS or COMPUTE_FRAMES)
        writer.visit(49, ACC_PUBLIC or ACC_SUPER, cls.name, null, "java/lang/Object", null)
        writer.visitSource(cls.name + ".ts", null)
        defaultConstructor(writer)
        val classSymbols = ClassSymbols(cls, Empty())
        cls.methods.forEach { m -> generateMethod(writer, m, classSymbols) }
        writer.visitEnd()
        return writer.toByteArray()
    }

    private fun generateMethod(writer: ClassWriter, m: Method, classSymbols: SymbolTable) {
        val mv = writer.visitMethod(ACC_PUBLIC, m.name, m.methodDescriptor(), null, null)
        val methodSymbols = MethodParameters(m, classSymbols)
        val code: List<Bytecode> = m.body.statements.flatMap { statementGenerator.generate(it, methodSymbols) }
        methodEmitter.emitBytecode(mv, code)
        mv.visitMaxs(0, 0)
        mv.visitEnd()
    }

    private fun defaultConstructor(writer: ClassWriter) {
        val mv = writer.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null)
        mv.visitVarInsn(ALOAD, 0)
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        mv.visitInsn(RETURN)
        mv.visitMaxs(0, 0)
        mv.visitEnd()
    }

    companion object {
        @JvmStatic
        fun build() = CodeGenerator(StatementGenerator.build(), MethodEmitter.build())

    }

}
