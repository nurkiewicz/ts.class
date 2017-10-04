package com.nurkiewicz.tsclass.parser.ast;

import lombok.Value;

@Value
public class Parameter {
    private final String name;
    private final Type type;
}
