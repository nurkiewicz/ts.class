package com.nurkiewicz.tsclass.parser.visitors

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableList.toImmutableList
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import com.nurkiewicz.tsclass.parser.ast.Field
import com.nurkiewicz.tsclass.parser.ast.Method
import java.util.*

class ClassVisitor : TypeScriptBaseVisitor<ClassDescriptor>() {

    override fun visitClassDeclaration(ctx: TypeScriptParser.ClassDeclarationContext): ClassDescriptor {
        return ClassDescriptor(ctx.IDENT().getSymbol().getText(), fields(ctx), methods(ctx))
    }

    private fun fields(ctx: TypeScriptParser.ClassDeclarationContext): ImmutableList<Field> {
        return ctx
                .classBody()
                .classElement()
                .stream()
                .map({ c -> c.accept(MemberVariableDeclarationVisitor()) })
                .filter{ Objects.nonNull(it) }
                .collect(toImmutableList())
    }

    private fun methods(ctx: TypeScriptParser.ClassDeclarationContext): ImmutableList<Method> {
        return ctx
                .classBody()
                .classElement()
                .stream()
                .map({ c -> c.accept(MemberFunctionDeclarationVisitor()) })
                .filter{ Objects.nonNull(it) }
                .collect(toImmutableList())
    }
}
