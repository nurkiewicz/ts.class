package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import com.nurkiewicz.tsclass.parser.ast.Field
import com.nurkiewicz.tsclass.parser.ast.Method

class ClassVisitor : TypeScriptBaseVisitor<ClassDescriptor>() {

    override fun visitClassDeclaration(ctx: TypeScriptParser.ClassDeclarationContext): ClassDescriptor {
        return ClassDescriptor(ctx.IDENT().symbol.text, fields(ctx), methods(ctx))
    }

    private fun fields(ctx: TypeScriptParser.ClassDeclarationContext): List<Field> {
        return ctx
                .classBody()
                .classElement()
                .map({ c -> c.accept(MemberVariableDeclarationVisitor()) })
                .filter{ it != null }
    }

    private fun methods(ctx: TypeScriptParser.ClassDeclarationContext): List<Method> {
        return ctx
                .classBody()
                .classElement()
                .map({ c -> c.accept(MemberFunctionDeclarationVisitor()) })
                .filter{ it != null }
    }
}
