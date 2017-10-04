package com.nurkiewicz.tsclass.parser.visitors;

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor;
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser;
import com.nurkiewicz.tsclass.parser.ast.expr.Expression;
import com.nurkiewicz.tsclass.parser.ast.expr.Identifier;
import com.nurkiewicz.tsclass.parser.ast.expr.NumberLiteral;

class MemberExpressionVisitor extends TypeScriptBaseVisitor<Expression> {
        @Override
        public Expression visitPrimaryExpression(TypeScriptParser.PrimaryExpressionContext ctx) {
            if (ctx.openParen() != null) {
                return ctx.expression().accept(new ExpressionVisitor());
            }
            if (ctx.IDENT() != null) {
                return new Identifier(ctx.IDENT().getText());
            }
            return ctx.accept(new TypeScriptBaseVisitor<Expression>() {
                @Override
                public Expression visitLiteral(TypeScriptParser.LiteralContext ctx) {
                    return new NumberLiteral(Double.valueOf(ctx.NUMERIC_LITERAL().getText()));
                }
            });
        }
    }