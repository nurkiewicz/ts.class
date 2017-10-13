package com.nurkiewicz.tsclass.codegen.asm

import com.nurkiewicz.tsclass.codegen.Bytecode
import com.nurkiewicz.tsclass.parser.ast.Method
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Type

class CallEmitter {

    fun emit(writer: MethodVisitor, call: Bytecode.Call) {
        writer.visitMethodInsn(
                call.code,
                call.owner,
                call.method.name,
                Type.getMethodDescriptor(Type.DOUBLE_TYPE, *argumentTypesOf(call.method)),
                call.isInterface)
    }

    private fun argumentTypesOf(method: Method) =
            method.parameters.map { Type.DOUBLE_TYPE }.toTypedArray()

}