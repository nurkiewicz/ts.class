package com.nurkiewicz.tsclass.parser;

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptLexer;
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser;
import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor;
import com.nurkiewicz.tsclass.parser.visitors.ClassVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.TokenStream;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Parser {

    public ClassDescriptor parse(InputStream sourceCode) throws IOException {
        return parse(IOUtils.toString(sourceCode, StandardCharsets.UTF_8));
    }

    public ClassDescriptor parse(String sourceCode) throws IOException {
        final CharStream charStream = new ANTLRInputStream(sourceCode);
        final TypeScriptLexer lexer = new TypeScriptLexer(charStream);
        final TokenStream tokens = new CommonTokenStream(lexer);
        final TypeScriptParser parser = new TypeScriptParser(tokens);
        parser.getErrorListeners().clear();
        parser.addErrorListener(throwingErrorListener());
        return parser.classDeclaration().accept(new ClassVisitor());
    }

    private BaseErrorListener throwingErrorListener() {
        return new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                throw new RuntimeException("Syntax error at line " + line + ":" + charPositionInLine + " " + msg);
            }
        };
    }

}
