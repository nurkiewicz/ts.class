package com.nurkiewicz.tsclass.parser.ast;

import com.google.common.collect.ImmutableList;
import lombok.Value;

import java.util.stream.Collectors;

@Value
public class Method {

    private final String name;
    private final Type type;
    private final ImmutableList<Parameter> parameters;
    private final ImmutableList<Statement> statements;

    public String methodDescriptor() {
        return org.objectweb.asm.Type.getMethodDescriptor(getType().toJavaType(), getParamJavaTypes());
    }

    private org.objectweb.asm.Type[] getParamJavaTypes() {
        return getParameters()
                .stream()
                .map(Parameter::getType)
                .map(Type::toJavaType)
                .collect(Collectors.toList())
                .toArray(new org.objectweb.asm.Type[0]);
    }
}
