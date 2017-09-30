package com.nurkiewicz.tsclass.parser.ast.expr;

import lombok.Value;

@Value
public class Identifier implements Expression {

    private final String name;

}
