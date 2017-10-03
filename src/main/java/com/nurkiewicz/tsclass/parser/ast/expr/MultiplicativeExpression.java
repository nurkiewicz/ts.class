package com.nurkiewicz.tsclass.parser.ast.expr;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class MultiplicativeExpression extends BinaryExpression {

    private final Operator operator;

    public MultiplicativeExpression(Expression left, Operator operator, Expression right) {
        super(left, right);
        this.operator = operator;
    }

    public String toString() {
        return getLeft() + " " + operator + " " + getRight();
    }

    public enum Operator {
        MUL, DIV, MOD;

        public static Operator of(String s) {
            switch(s) {
                case "*": return MUL;
                case "/": return DIV;
                case "%": return MOD;
                default:
                    throw new IllegalArgumentException(s);
            }
        }
    }


}
