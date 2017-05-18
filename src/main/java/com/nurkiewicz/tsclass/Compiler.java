package com.nurkiewicz.tsclass;

import com.nurkiewicz.tsclass.parser.Parser;
import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor;

import java.io.IOException;
import java.io.InputStream;

public class Compiler {

    public static void main(String[] args) throws IOException {
        final InputStream programStream = Compiler.class.getResourceAsStream("Greeter.ts");
        final ClassDescriptor cls = new Parser().parse(programStream);
        System.out.println(cls);
    }
}
