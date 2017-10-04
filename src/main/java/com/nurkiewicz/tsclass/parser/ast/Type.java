package com.nurkiewicz.tsclass.parser.ast;

import lombok.Value;

@Value
public class Type {

    private final String name;

    public org.objectweb.asm.Type toJavaType() {
        switch (name) {
            case "number":
                return org.objectweb.asm.Type.DOUBLE_TYPE;
            case "string":
                return org.objectweb.asm.Type.getType(String.class);
            default:
                throw new IllegalArgumentException("Uknown type: " + name);
        }
    }

}
