package com.nurkiewicz.tsclass.parser.visitors;

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor;
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser;
import com.nurkiewicz.tsclass.parser.ast.expr.Expression;

class ExpressionVisitor extends TypeScriptBaseVisitor<Expression> {

        @Override
        public Expression visitAssignmentExpression(TypeScriptParser.AssignmentExpressionContext ctx) {
            return ctx.accept(new AdditiveExpressionVisitor());
        }

}