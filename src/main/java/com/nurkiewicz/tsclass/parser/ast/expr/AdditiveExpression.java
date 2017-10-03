package com.nurkiewicz.tsclass.parser.ast.expr;

import lombok.Value;

@Value
public class AdditiveExpression extends BinaryExpression {

    private final Operator operator;

    public AdditiveExpression(Expression left, Operator operator, Expression right) {
        super(left, right);
        this.operator = operator;
    }

    public enum Operator {
        PLUS, MINUS;

        public static Operator of(String s) {
            switch(s) {
                case "+": return PLUS;
                case "-": return MINUS;
                default:
                    throw new IllegalArgumentException(s);
            }
        }
    }

    public String toString() {
        return getLeft() + " " + operator + " " + getRight();
    }

}
