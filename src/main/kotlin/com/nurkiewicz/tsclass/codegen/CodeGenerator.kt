package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import com.nurkiewicz.tsclass.parser.ast.Method
import com.nurkiewicz.tsclass.parser.ast.ReturnStatement
import com.nurkiewicz.tsclass.parser.ast.expr.AdditiveExpression
import com.nurkiewicz.tsclass.parser.ast.expr.Identifier
import com.nurkiewicz.tsclass.parser.ast.expr.NumberLiteral
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.ClassWriter.COMPUTE_FRAMES
import org.objectweb.asm.ClassWriter.COMPUTE_MAXS
import org.objectweb.asm.Opcodes.ACC_PUBLIC
import org.objectweb.asm.Opcodes.ACC_SUPER
import org.objectweb.asm.Opcodes.ALOAD
import org.objectweb.asm.Opcodes.DADD
import org.objectweb.asm.Opcodes.DLOAD
import org.objectweb.asm.Opcodes.DRETURN
import org.objectweb.asm.Opcodes.INVOKESPECIAL
import org.objectweb.asm.Opcodes.RETURN

class CodeGenerator {

    fun generate(cls: ClassDescriptor): ByteArray {
        val writer = ClassWriter(COMPUTE_MAXS or COMPUTE_FRAMES)
        writer.visit(49, ACC_PUBLIC or ACC_SUPER, cls.name, null, "java/lang/Object", null)
        writer.visitSource(cls.name + ".ts", null)

        defaultConstructor(writer)

        cls.methods.forEach { m -> generateMethod(writer, m) }

        writer.visitEnd()
        return writer.toByteArray()
    }

    private fun generateMethod(writer: ClassWriter, m: Method) {
        val mv = writer.visitMethod(ACC_PUBLIC, m.name, m.methodDescriptor(), null, null)
        m.statements
        val returnStatement = m.statements[0] as ReturnStatement
        val expression = returnStatement.expression
        if (expression is NumberLiteral) {
            val value = expression.value
            mv.visitLdcInsn(value)
        } else if (expression is Identifier) {
            // TODO: 03/10/17 Symbol table
            mv.visitVarInsn(DLOAD, 1)
        } else if (expression is AdditiveExpression) {
            mv.visitVarInsn(DLOAD, 1)
            mv.visitVarInsn(DLOAD, 3)
            mv.visitInsn(DADD)
        }
        mv.visitInsn(DRETURN)
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

}
