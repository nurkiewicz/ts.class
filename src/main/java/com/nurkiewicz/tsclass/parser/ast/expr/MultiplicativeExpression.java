package com.nurkiewicz.tsclass.parser.ast.expr;

import lombok.EqualsAndHashCode;
import lombok.Value;

import static com.nurkiewicz.tsclass.parser.ast.expr.MultiplicativeExpression.Operator.DIV;
import static com.nurkiewicz.tsclass.parser.ast.expr.MultiplicativeExpression.Operator.MOD;
import static com.nurkiewicz.tsclass.parser.ast.expr.MultiplicativeExpression.Operator.MUL;

@Value
@EqualsAndHashCode(callSuper = true)
public class MultiplicativeExpression extends BinaryExpression {

    private final Operator operator;

    public MultiplicativeExpression(Expression left, Operator operator, Expression right) {
        super(left, right);
        this.operator = operator;
    }

    public static MultiplicativeExpression mul(Expression left, Expression right) {
        return new MultiplicativeExpression(left, MUL, right);
    }

    public static MultiplicativeExpression div(Expression left, Expression right) {
        return new MultiplicativeExpression(left, DIV, right);
    }

    public static MultiplicativeExpression mod(Expression left, Expression right) {
        return new MultiplicativeExpression(left, MOD, right);
    }

    public String toString() {
        return "(" + getLeft() + " " + operator + " " + getRight() + ")";
    }

    public enum Operator {
        MUL("*"), DIV("/"), MOD("%");

        private final String s;

        Operator(String s) {
            this.s = s;
        }

        public static Operator of(String s) {
            switch(s) {
                case "*": return MUL;
                case "/": return DIV;
                case "%": return MOD;
                default:
                    throw new IllegalArgumentException(s);
            }
        }


        @Override
        public String toString() {
            return s;
        }
    }


}
