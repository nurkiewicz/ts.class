package com.nurkiewicz.tsclass

import com.nurkiewicz.tsclass.parser.ast.AdditiveExpression
import com.nurkiewicz.tsclass.parser.ast.Expression
import com.nurkiewicz.tsclass.parser.ast.Identifier
import com.nurkiewicz.tsclass.parser.ast.MethodCall
import com.nurkiewicz.tsclass.parser.ast.MultiplicativeExpression
import com.nurkiewicz.tsclass.parser.ast.Neg
import com.nurkiewicz.tsclass.parser.ast.NumberLiteral
import com.nurkiewicz.tsclass.parser.ast.Relational


class ExpressionBuilder {

    static AdditiveExpression add(Expression left, Expression right) {
        return new AdditiveExpression(left, AdditiveExpression.Operator.PLUS, right)
    }

    static AdditiveExpression sub(Expression left, Expression right) {
        return new AdditiveExpression(left, AdditiveExpression.Operator.MINUS, right)
    }

    static Identifier ident(String name) {
        return new Identifier(name)
    }

    static MethodCall call(String name, Expression... parameters) {
        return new MethodCall(name, parameters.toList())
    }

    static MultiplicativeExpression mul(Expression left, Expression right) {
        return new MultiplicativeExpression(left, MultiplicativeExpression.Operator.MUL, right)
    }

    static MultiplicativeExpression div(Expression left, Expression right) {
        return new MultiplicativeExpression(left, MultiplicativeExpression.Operator.DIV, right)
    }

    static MultiplicativeExpression mod(Expression left, Expression right) {
        return new MultiplicativeExpression(left, MultiplicativeExpression.Operator.MOD, right)
    }

    static neg(Double x) {
        return new Neg(num(x))
    }

    static neg(String x) {
        return new Neg(new Identifier(x))
    }

    static neg(Expression e) {
        return new Neg(e)
    }

    static gt(Expression left, Expression right) {
        return new Relational(left, Relational.Operator.GT, right)
    }

    static gte(Expression left, Expression right) {
        return new Relational(left, Relational.Operator.GTE, right)
    }

    static lt(Expression left, Expression right) {
        return new Relational(left, Relational.Operator.LT, right)
    }

    static lte(Expression left, Expression right) {
        return new Relational(left, Relational.Operator.LTE, right)
    }

    static NumberLiteral num(Double x) {
        return new NumberLiteral(x)
    }
}
