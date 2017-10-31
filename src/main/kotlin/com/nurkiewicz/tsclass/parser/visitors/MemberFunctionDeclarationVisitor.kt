package com.nurkiewicz.tsclass.parser.visitors

import com.google.common.collect.Lists
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.Block
import com.nurkiewicz.tsclass.parser.ast.Method
import com.nurkiewicz.tsclass.parser.ast.Parameter
import com.nurkiewicz.tsclass.parser.ast.Type
import java.util.ArrayList

internal class MemberFunctionDeclarationVisitor : TypeScriptBaseVisitor<Method>() {

    override fun visitMemberFunctionDeclaration(ctx: TypeScriptParser.MemberFunctionDeclarationContext): Method {
        val sig = ctx.memberFunctionImplementation().functionSignature()
        val methodName = sig.IDENT().text
        val typeCtx = sig.returnTypeAnnotation().returnType().type()
        return Method(methodName, typeOf(typeCtx), parameters(sig), parseBody(ctx))
    }

    private fun parameters(sig: TypeScriptParser.FunctionSignatureContext): List<Parameter> {
        val paramsCtx = sig.parameterList()
        return if (paramsCtx != null) {
            paramsCtx.accept(ParameterListVisitor())
        } else {
            emptyList()
        }
    }

    private fun typeOf(typeCtx: TypeScriptParser.TypeContext?) =
            Type(typeCtx?.typeName()?.text)

    private fun parseBody(ctx: TypeScriptParser.MemberFunctionDeclarationContext) = Block(
            ctx
                    .memberFunctionImplementation()
                    .functionBody()
                    .sourceElement()
                    .map({ se -> se.accept(StatementVisitor()) })
                    .filter { it != null }
    )

    private class RequiredParameterListVisitor : TypeScriptBaseVisitor<List<Parameter>>() {

        override fun visitRequiredParameter(ctx: TypeScriptParser.RequiredParameterContext): List<Parameter> {
            val name = ctx.IDENT().text
            val typeName = ctx.typeAnnotation().accept(TypeVisitor)
            val parameter = Parameter(name, typeName)
            return Lists.newArrayList(parameter)
        }

        override fun defaultResult(): List<Parameter> {
            return ArrayList()
        }

        override fun aggregateResult(aggregate: List<Parameter>, nextResult: List<Parameter>?): List<Parameter> {
            return aggregate + (nextResult ?: emptyList())
        }
    }

    private class ParameterListVisitor : TypeScriptBaseVisitor<List<Parameter>>() {

        override fun visitRequiredParameterList(ctx: TypeScriptParser.RequiredParameterListContext): List<Parameter> {
            return ctx.accept(RequiredParameterListVisitor())
        }

    }
}
