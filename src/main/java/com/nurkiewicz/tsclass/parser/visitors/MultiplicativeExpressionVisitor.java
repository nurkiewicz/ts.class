package com.nurkiewicz.tsclass.parser.visitors;

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor;
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser;
import com.nurkiewicz.tsclass.parser.ast.expr.Expression;
import com.nurkiewicz.tsclass.parser.ast.expr.MultiplicativeExpression;

class MultiplicativeExpressionVisitor extends TypeScriptBaseVisitor<Expression> {

    @Override
    public Expression visitMultiplicativeExpression(TypeScriptParser.MultiplicativeExpressionContext ctx) {
        final Expression right = ctx.accept(new MemberExpressionVisitor());
        if (ctx.multiplicativeExpression() != null) {
            final Expression left = ctx.multiplicativeExpression().accept(this);
            final MultiplicativeExpression.Operator op = MultiplicativeExpression.Operator.of(ctx.op.getText());
            return new MultiplicativeExpression(left, op, right);
        }
        return right;
    }

}
