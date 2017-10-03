package com.nurkiewicz.tsclass.parser.ast.expr;

import lombok.Value;

@Value
public class NumberLiteral implements Expression {
    private final double value;

    public String toString() {
        return String.valueOf(value);
    }
}
