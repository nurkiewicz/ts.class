package com.nurkiewicz.tsclass.parser.ast;

import com.nurkiewicz.tsclass.parser.ast.expr.Expression;
import lombok.Value;

@Value
public class ReturnStatement implements Statement {

    private final Expression expression;

}
