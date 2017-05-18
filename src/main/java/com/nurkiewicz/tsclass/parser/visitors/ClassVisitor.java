package com.nurkiewicz.tsclass.parser.visitors;

import com.google.common.collect.ImmutableList;
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor;
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser;
import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor;
import com.nurkiewicz.tsclass.parser.ast.Field;
import com.nurkiewicz.tsclass.parser.ast.Method;

import java.util.Objects;

import static com.google.common.collect.ImmutableList.toImmutableList;

public class ClassVisitor extends TypeScriptBaseVisitor<ClassDescriptor> {

    @Override
    public ClassDescriptor visitClassDeclaration(TypeScriptParser.ClassDeclarationContext ctx) {
        return new ClassDescriptor(ctx.IDENT().getSymbol().getText(), fields(ctx), methods(ctx));
    }

    private ImmutableList<Field> fields(TypeScriptParser.ClassDeclarationContext ctx) {
        return ctx
                .classBody()
                .classElement()
                .stream()
                .map(c -> c.accept(new MemberVariableDeclarationVisitor()))
                .filter(Objects::nonNull)
                .collect(toImmutableList());
    }

    private ImmutableList<Method> methods(TypeScriptParser.ClassDeclarationContext ctx) {
        return ctx
                .classBody()
                .classElement()
                .stream()
                .map(c -> c.accept(new MemberFunctionDeclarationVisitor()))
                .filter(Objects::nonNull)
                .collect(toImmutableList());
    }
}
