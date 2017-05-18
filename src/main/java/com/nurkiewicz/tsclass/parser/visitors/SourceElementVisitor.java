package com.nurkiewicz.tsclass.parser.visitors;

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor;
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser;
import com.nurkiewicz.tsclass.parser.ast.Statement;

class SourceElementVisitor extends TypeScriptBaseVisitor<Statement> {

    @Override
    public Statement visitStatement(TypeScriptParser.StatementContext ctx) {
        return new Statement(ctx.getText());
    }
}
