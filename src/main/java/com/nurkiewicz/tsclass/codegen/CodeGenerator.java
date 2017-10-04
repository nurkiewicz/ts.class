package com.nurkiewicz.tsclass.codegen;

import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor;
import com.nurkiewicz.tsclass.parser.ast.Method;
import com.nurkiewicz.tsclass.parser.ast.ReturnStatement;
import com.nurkiewicz.tsclass.parser.ast.expr.Expression;
import com.nurkiewicz.tsclass.parser.ast.expr.Identifier;
import com.nurkiewicz.tsclass.parser.ast.expr.NumberLiteral;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.DLOAD;
import static org.objectweb.asm.Opcodes.DRETURN;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.RETURN;

public class CodeGenerator {

    byte[] generate(ClassDescriptor cls) {
        final ClassWriter writer = new ClassWriter(COMPUTE_MAXS | COMPUTE_FRAMES);
        writer.visit(49, ACC_PUBLIC | ACC_SUPER, cls.getName(), null, "java/lang/Object", null);
        writer.visitSource(cls.getName() + ".ts", null);

        defaultConstructor(writer);

        cls.getMethods().forEach(m -> generateMethod(writer, m));

        writer.visitEnd();
        return writer.toByteArray();
    }

    private void generateMethod(ClassWriter writer, Method m) {
        final MethodVisitor mv = writer.visitMethod(ACC_PUBLIC, m.getName(), m.methodDescriptor(), null, null);
        final ReturnStatement returnStatement = (ReturnStatement) m.getStatements().get(0);
        final Expression expression = returnStatement.getExpression();
        if (expression instanceof NumberLiteral) {
            final double value = ((NumberLiteral) expression).getValue();
            mv.visitLdcInsn(value);
            mv.visitInsn(DRETURN);
        } else if (expression instanceof Identifier) {
            // TODO: 03/10/17 Symbol table
            final String name = ((Identifier) expression).getName();
            mv.visitVarInsn(DLOAD, 1);
            mv.visitInsn(DRETURN);
        }
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private void defaultConstructor(ClassWriter writer) {
        MethodVisitor mv = writer.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

}
