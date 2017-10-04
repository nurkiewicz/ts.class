package com.nurkiewicz.tsclass.parser.ast.expr;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class BinaryExpression implements Expression {

    private final Expression left;
    private final Expression right;

}
