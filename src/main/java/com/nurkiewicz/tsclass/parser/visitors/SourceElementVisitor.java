package com.nurkiewicz.tsclass.parser.visitors;

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor;
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser;
import com.nurkiewicz.tsclass.parser.ast.Expression;
import com.nurkiewicz.tsclass.parser.ast.ReturnStatement;
import com.nurkiewicz.tsclass.parser.ast.Statement;

class SourceElementVisitor extends TypeScriptBaseVisitor<Statement> {

    @Override
    public Statement visitReturnStatement(TypeScriptParser.ReturnStatementContext ctx) {
        final Expression expression = ctx.expression().accept(new ExpressionVisitor());
        return new ReturnStatement(expression);
    }

}
