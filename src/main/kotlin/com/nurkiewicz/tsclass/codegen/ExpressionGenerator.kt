package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.parser.ast.expr.AdditiveExpression
import com.nurkiewicz.tsclass.parser.ast.expr.Expression
import com.nurkiewicz.tsclass.parser.ast.expr.MultiplicativeExpression
import com.nurkiewicz.tsclass.parser.ast.expr.NumberLiteral
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.LDC

internal class ExpressionGenerator {

    fun generate(expression: Expression): List<Bytecode> {
        return when (expression) {
            is NumberLiteral -> listOf(
                    Bytecode.DoubleArg(LDC, expression.value)
            )
            is AdditiveExpression ->
                generate(expression.left) + generate(expression.right) + operatorToBytecode(expression.operator)
            is MultiplicativeExpression ->
                generate(expression.left) + generate(expression.right) + Bytecode.NoArg(Opcodes.DMUL)
            else -> throw IllegalArgumentException("Unknown: " + expression)
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