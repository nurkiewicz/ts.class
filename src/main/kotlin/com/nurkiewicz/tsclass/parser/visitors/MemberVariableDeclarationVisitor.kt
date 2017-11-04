package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.Field
import com.nurkiewicz.tsclass.parser.ast.Type

internal class MemberVariableDeclarationVisitor : TypeScriptBaseVisitor<Field>() {

    override fun visitMemberVariableDeclaration(ctx: TypeScriptParser.MemberVariableDeclarationContext): Field {
        val name = ctx.variableDeclaration().IDENT().symbol.text
        val type = Type(ctx.variableDeclaration().typeAnnotation().type().text)
        return Field(name, type)
    }

}
