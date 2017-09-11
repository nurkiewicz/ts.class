package com.nurkiewicz.tsclass.parser.ast;

import lombok.Value;

@Value
public class NumberLiteral implements Expression {
    private final double value;
}
