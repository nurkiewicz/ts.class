package com.nurkiewicz.tsclass;

import org.objectweb.asm.Type;

public class Foo {

    public static void main(String[] args) {
//        Type type = Type.getType(String.class);
        Type type = Type.getType(Double.class);
        System.out.println("type = " + type.getDescriptor());
    }

}
