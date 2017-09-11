package com.nurkiewicz.tsclass.parser.ast;

import com.google.common.collect.ImmutableList;
import lombok.Value;

@Value
public class Method {

    private final String name;
    private final String type;
    private final ImmutableList<String> arguments;
    private final ImmutableList<Statement> statements;

}
