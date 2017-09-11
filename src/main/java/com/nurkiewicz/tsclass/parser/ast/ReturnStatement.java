package com.nurkiewicz.tsclass.parser.ast;

import lombok.Value;

@Value
public class ReturnStatement implements Statement {

    private final Expression expression;

}
