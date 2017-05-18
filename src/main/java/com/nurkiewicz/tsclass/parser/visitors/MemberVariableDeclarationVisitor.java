package com.nurkiewicz.tsclass.parser.visitors;

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor;
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser;
import com.nurkiewicz.tsclass.parser.ast.Field;

class MemberVariableDeclarationVisitor extends TypeScriptBaseVisitor<Field> {

    @Override
    public Field visitMemberVariableDeclaration(TypeScriptParser.MemberVariableDeclarationContext ctx) {
        final String name = ctx.variableDeclaration().IDENT().getSymbol().getText();
        final String type = ctx.variableDeclaration().typeAnnotation().type().getText();
        return new Field(name, type);
    }

}
