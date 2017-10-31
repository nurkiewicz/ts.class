package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.CompilationError
import com.nurkiewicz.tsclass.parser.ast.AdditiveExpression
import com.nurkiewicz.tsclass.parser.ast.Expression
import com.nurkiewicz.tsclass.parser.ast.Identifier
import com.nurkiewicz.tsclass.parser.ast.MethodCall
import com.nurkiewicz.tsclass.parser.ast.MultiplicativeExpression
import com.nurkiewicz.tsclass.parser.ast.Neg
import com.nurkiewicz.tsclass.parser.ast.NumberLiteral
import com.nurkiewicz.tsclass.parser.ast.Relational
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.DLOAD
import org.objectweb.asm.Opcodes.LDC

class ExpressionGenerator {

    fun generate(expression: Expression, tab: SymbolTable): List<Bytecode> {
        return when (expression) {
            is NumberLiteral -> listOf(
                    Bytecode.DoubleArg(LDC, expression.value)
            )
            is AdditiveExpression ->
                generate(expression.left, tab) +
                        generate(expression.right, tab) +
                        operatorToBytecode(expression.operator)
            is MultiplicativeExpression ->
                generate(expression.left, tab) +
                        generate(expression.right, tab) +
                        operatorToBytecode(expression.operator)
            is Identifier ->
                resolveSymbol(tab, expression)
            is MethodCall ->
                aload0() + pushParametersOnStack(expression, tab) + thisClassCall(expression, tab)
            is Neg ->
                this.generate(expression.expression, tab) + Bytecode.NoArg(Opcodes.DNEG)
            is Relational ->
                generate(expression.left, tab) +
                        generate(expression.right, tab) +
                        Bytecode.NoArg(Opcodes.DCMPL) +
                        operatorToConditionalJump(expression.operator)
        }
    }

    private fun aload0() = listOf(Bytecode.IntArg(Opcodes.ALOAD, 0))

    private fun pushParametersOnStack(expression: MethodCall, tab: SymbolTable) =
            expression.parameters.flatMap { generate(it, tab) }

    private fun thisClassCall(methodCall: MethodCall, tab: SymbolTable): Bytecode.Call {
        val matchingMethod = tab.currentClass().findBestMatchingMethod(methodCall)
        if (matchingMethod != null) {
            return Bytecode.Call(Opcodes.INVOKESPECIAL, tab.currentClass().name, matchingMethod, false)
        } else {
            throw UnknownMethod(methodCall)
        }
    }

    private fun resolveSymbol(tab: SymbolTable, expression: Identifier): List<Bytecode> {
        val symbol = tab.lookup(expression.name)
        return when (symbol) {
            is Symbol.MethodParameter -> listOf(Bytecode.IntArg(DLOAD, symbol.offset))
            is Symbol.LocalVariable -> listOf(Bytecode.IntArg(DLOAD, symbol.offset))
            null -> throw CompilationError("Unknown symbol ${expression.name}")
        }
    }

    private fun operatorToBytecode(operator: AdditiveExpression.Operator) = when (operator) {
        AdditiveExpression.Operator.PLUS -> Bytecode.NoArg(Opcodes.DADD)
        AdditiveExpression.Operator.MINUS -> Bytecode.NoArg(Opcodes.DSUB)
    }

    private fun operatorToBytecode(operator: MultiplicativeExpression.Operator) = when (operator) {
        MultiplicativeExpression.Operator.MUL -> Bytecode.NoArg(Opcodes.DMUL)
        MultiplicativeExpression.Operator.DIV -> Bytecode.NoArg(Opcodes.DDIV)
        MultiplicativeExpression.Operator.MOD -> Bytecode.NoArg(Opcodes.DREM)
    }

    private fun operatorToConditionalJump(operator: Relational.Operator) = when (operator) {
        Relational.Operator.LT -> Bytecode.Jump(Opcodes.IFGE, Label())
        Relational.Operator.LTE -> Bytecode.Jump(Opcodes.IFGT, Label())
        Relational.Operator.GT -> Bytecode.Jump(Opcodes.IFLE, Label())
        Relational.Operator.GTE -> Bytecode.Jump(Opcodes.IFLT, Label())
    }

}