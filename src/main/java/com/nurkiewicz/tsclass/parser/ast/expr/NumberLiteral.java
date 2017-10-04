package com.nurkiewicz.tsclass.parser.ast.expr;

import lombok.Value;

@Value
public class NumberLiteral implements Expression {
    private final double value;

    public static NumberLiteral num(double x) {
        return new NumberLiteral(x);
    }

    public String toString() {
        return String.valueOf(value);
    }
}
