package com.nurkiewicz.tsclass.parser.visitors;

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor;
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser;
import com.nurkiewicz.tsclass.parser.ast.expr.AdditiveExpression;
import com.nurkiewicz.tsclass.parser.ast.expr.Expression;

class AdditiveExpressionVisitor extends TypeScriptBaseVisitor<Expression> {
    @Override
    public Expression visitAdditiveExpression(TypeScriptParser.AdditiveExpressionContext ctx) {
        final Expression right = ctx.accept(new MemberExpressionVisitor());
        if (ctx.additiveExpression() != null) {
            final Expression left = ctx.additiveExpression().accept(this);
            final AdditiveExpression.Operator op = AdditiveExpression.Operator.of(ctx.op.getText());
            return new AdditiveExpression(left, op, right);
        }
        return right;
    }

}
