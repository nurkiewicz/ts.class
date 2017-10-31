package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.Type

internal object TypeVisitor : TypeScriptBaseVisitor<Type>() {

    override fun visitTypeAnnotation(ctx: TypeScriptParser.TypeAnnotationContext) =
            Type(ctx.type().typeName().moduleOrTypeName().IDENT().text, 2)
}