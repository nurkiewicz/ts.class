package com.nurkiewicz.tsclass.codegen;

public class ByteArrayClassLoader extends ClassLoader {

    public ByteArrayClassLoader() {
        super(ByteArrayClassLoader.class.getClassLoader());
    }

    public Class<?> loadClass(byte[] classBytes) {
        return defineClass(null, classBytes, 0, classBytes.length);
    }

}
