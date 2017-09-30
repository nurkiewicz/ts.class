package com.nurkiewicz.tsclass.parser.ast;

import com.google.common.collect.ImmutableList;
import lombok.Value;

@Value
public class Method {

    private final String name;
    private final Type type;
    private final ImmutableList<Parameter> parameters;
    private final ImmutableList<Statement> statements;

}
