package com.nurkiewicz.tsclass.parser.ast.expr;

import lombok.Value;

@Value
public class Identifier implements Expression {

    private final String name;

    public static Identifier ident(String name) {
        return new Identifier(name);
    }

    public String toString() {
        return name;
    }

}
