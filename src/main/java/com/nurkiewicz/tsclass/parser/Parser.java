package com.nurkiewicz.tsclass.parser;

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptLexer;
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser;
import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor;
import com.nurkiewicz.tsclass.parser.visitors.ClassVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Parser {

    public ClassDescriptor parse(InputStream sourceCode) throws IOException {
        final String program = IOUtils.toString(sourceCode, StandardCharsets.UTF_8);
        final CharStream charStream = new ANTLRInputStream(program);
        final TypeScriptLexer lexer = new TypeScriptLexer(charStream);
        final TokenStream tokens = new CommonTokenStream(lexer);
        final TypeScriptParser parser = new TypeScriptParser(tokens);
        return parser.classDeclaration().accept(new ClassVisitor());
    }

}
