package com.nurkiewicz.tsclass.parser.visitors;

import com.google.common.collect.ImmutableList;
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor;
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser;
import com.nurkiewicz.tsclass.parser.ast.Method;
import com.nurkiewicz.tsclass.parser.ast.Parameter;
import com.nurkiewicz.tsclass.parser.ast.Statement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.google.common.collect.ImmutableList.toImmutableList;

class MemberFunctionDeclarationVisitor extends TypeScriptBaseVisitor<Method> {

    @Override
    public Method visitMemberFunctionDeclaration(TypeScriptParser.MemberFunctionDeclarationContext ctx) {
        final TypeScriptParser.FunctionSignatureContext sig = ctx.memberFunctionImplementation().functionSignature();
        final String methodName = sig.IDENT().getText();
        final TypeScriptParser.TypeContext typeCtx = sig.returnTypeAnnotation().returnType().type();
        final List<Parameter> parameters = sig.accept(parameterListVisitor());
        return new Method(methodName, typeOf(typeCtx), ImmutableList.copyOf(parameters), parseBody(ctx));
    }

    private TypeScriptBaseVisitor<List<Parameter>> parameterListVisitor() {
        return new TypeScriptBaseVisitor<List<Parameter>>() {
            @Override
            public List<Parameter> visitParameterList(TypeScriptParser.ParameterListContext ctx) {
                final Parameter param = ctx.accept(parameterVisitor());
                return Collections.singletonList(param);
            }

            @Override
            protected List<Parameter> aggregateResult(List<Parameter> aggregate, List<Parameter> nextResult) {
                if (aggregate == null) {
                    return nextResult == null? new ArrayList<>() : nextResult;
                }
                final ArrayList<Parameter> params = new ArrayList<>(aggregate);
                params.addAll(nextResult);
                return params;
            }
        };
    }

    private TypeScriptBaseVisitor<Parameter> parameterVisitor() {
        return new TypeScriptBaseVisitor<Parameter>() {
            @Override
            public Parameter visitRequiredParameterList(TypeScriptParser.RequiredParameterListContext ctx) {
                final String name = ctx.requiredParameter().IDENT().getText();
                final String typeName = ctx.requiredParameter().typeAnnotation().type().typeName().moduleOrTypeName().IDENT().getText();
                return new Parameter(name, typeName);
            }
        };
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
