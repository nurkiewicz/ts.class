package com.nurkiewicz.tsclass.parser.visitors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor;
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser;
import com.nurkiewicz.tsclass.parser.ast.Method;
import com.nurkiewicz.tsclass.parser.ast.Parameter;
import com.nurkiewicz.tsclass.parser.ast.Statement;
import com.nurkiewicz.tsclass.parser.ast.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.google.common.collect.ImmutableList.toImmutableList;

class MemberFunctionDeclarationVisitor extends TypeScriptBaseVisitor<Method> {

    @Override
    public Method visitMemberFunctionDeclaration(TypeScriptParser.MemberFunctionDeclarationContext ctx) {
        final TypeScriptParser.FunctionSignatureContext sig = ctx.memberFunctionImplementation().functionSignature();
        final String methodName = sig.IDENT().getText();
        final TypeScriptParser.TypeContext typeCtx = sig.returnTypeAnnotation().returnType().type();
        return new Method(methodName, typeOf(typeCtx), parameters(sig), parseBody(ctx));
    }

    private ImmutableList<Parameter> parameters(TypeScriptParser.FunctionSignatureContext sig) {
        final TypeScriptParser.ParameterListContext paramsCtx = sig.parameterList();
        if (paramsCtx != null) {
            return ImmutableList.copyOf(paramsCtx.accept(new ParameterListVisitor()));
        } else {
            return ImmutableList.of();
        }
    }

    private Type typeOf(TypeScriptParser.TypeContext typeCtx) {
        return new Type(typeCtx != null ? typeCtx.typeName().getText() : "void");
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

    private static class RequiredParameterListVisitor extends TypeScriptBaseVisitor<List<Parameter>> {

        @Override
        public List<Parameter> visitRequiredParameter(TypeScriptParser.RequiredParameterContext ctx) {
            final String name = ctx.IDENT().getText();
            final Type typeName = new Type(ctx.typeAnnotation().type().typeName().moduleOrTypeName().IDENT().getText());
            final Parameter parameter = new Parameter(name, typeName);
            return Lists.newArrayList(parameter);
        }

        @Override
        protected List<Parameter> defaultResult() {
            return new ArrayList<>();
        }

        @Override
        protected List<Parameter> aggregateResult(List<Parameter> aggregate, List<Parameter> nextResult) {
            if (nextResult == null) {
                return aggregate;
            }
            aggregate.addAll(nextResult);
            return aggregate;
        }
    }

    private static class ParameterListVisitor extends TypeScriptBaseVisitor<List<Parameter>> {

        @Override
        public List<Parameter> visitRequiredParameterList(TypeScriptParser.RequiredParameterListContext ctx) {
            return ctx.accept(new RequiredParameterListVisitor());
        }

    }
}
