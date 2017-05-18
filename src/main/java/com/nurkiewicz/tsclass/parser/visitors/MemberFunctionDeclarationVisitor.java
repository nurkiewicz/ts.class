package com.nurkiewicz.tsclass.parser.visitors;

import com.google.common.collect.ImmutableList;
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor;
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser;
import com.nurkiewicz.tsclass.parser.ast.Method;
import com.nurkiewicz.tsclass.parser.ast.Statement;

import java.util.Objects;

import static com.google.common.collect.ImmutableList.toImmutableList;

class MemberFunctionDeclarationVisitor extends TypeScriptBaseVisitor<Method> {

    @Override
    public Method visitMemberFunctionDeclaration(TypeScriptParser.MemberFunctionDeclarationContext ctx) {
        final TypeScriptParser.FunctionSignatureContext sig = ctx.memberFunctionImplementation().functionSignature();
        final String methodName = sig.IDENT().getText();
        final TypeScriptParser.TypeContext typeCtx = sig.returnTypeAnnotation().returnType().type();
        return new Method(methodName, typeOf(typeCtx), parseBody(ctx));
    }

    private String typeOf(TypeScriptParser.TypeContext typeCtx) {
        return typeCtx != null ? typeCtx.typeName().getText() : "void";
    }

    private ImmutableList<Statement> parseBody(TypeScriptParser.MemberFunctionDeclarationContext ctx) {
        return ctx
                .memberFunctionImplementation()
                .functionBody()
                .sourceElement()
                .stream()
                .map(se -> se.accept(new SourceElementVisitor()))
                .filter(Objects::nonNull)
                .collect(toImmutableList());
    }
}
