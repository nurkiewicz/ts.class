package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.Field

internal class MemberVariableDeclarationVisitor : TypeScriptBaseVisitor<Field>() {

    override fun visitMemberVariableDeclaration(ctx: TypeScriptParser.MemberVariableDeclarationContext): Field {
        val name = ctx.variableDeclaration().IDENT().symbol.text
        val type = ctx.variableDeclaration().typeAnnotation().type().getText()
        return Field(name, type)
    }

}
