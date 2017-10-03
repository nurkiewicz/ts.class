package com.nurkiewicz.tsclass.parser.ast.expr;

import lombok.Value;

@Value
public class AdditiveExpression implements Expression {

    private final Expression left;
    private final Operator operator;
    private final Expression right;

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


}
