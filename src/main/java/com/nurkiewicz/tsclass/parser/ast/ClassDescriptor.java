package com.nurkiewicz.tsclass.parser.ast;

import com.google.common.collect.ImmutableList;
import lombok.Value;

@Value
public class ClassDescriptor {
    private String name;
    private ImmutableList<Field> fields;
    private ImmutableList<Method> methods;
}
