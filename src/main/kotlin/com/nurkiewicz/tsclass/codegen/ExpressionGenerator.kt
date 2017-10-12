package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.CompilationError
import com.nurkiewicz.tsclass.parser.ast.expr.AdditiveExpression
import com.nurkiewicz.tsclass.parser.ast.expr.Expression
import com.nurkiewicz.tsclass.parser.ast.expr.Identifier
import com.nurkiewicz.tsclass.parser.ast.expr.MultiplicativeExpression
import com.nurkiewicz.tsclass.parser.ast.expr.NumberLiteral
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
                        Bytecode.NoArg(Opcodes.DMUL)
            is Identifier -> resolveSymbol(tab, expression)
            else -> throw IllegalArgumentException("Unknown expression: $expression")
        }
    }

    private fun resolveSymbol(tab: SymbolTable, expression: Identifier): List<Bytecode> {
        val symbol = tab.lookup(expression.name)
        return when (symbol) {
            is Symbol.MethodParameter -> listOf(Bytecode.IntArg(DLOAD, symbol.offset))
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
}