package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.parser.Parser
import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import com.nurkiewicz.tsclass.parser.ast.Method
import com.nurkiewicz.tsclass.parser.ast.ReturnStatement
import com.nurkiewicz.tsclass.parser.ast.expr.AdditiveExpression
import com.nurkiewicz.tsclass.parser.ast.expr.Expression
import com.nurkiewicz.tsclass.parser.ast.expr.Identifier
import com.nurkiewicz.tsclass.parser.ast.expr.MultiplicativeExpression
import com.nurkiewicz.tsclass.parser.ast.expr.NumberLiteral
import spock.lang.Specification
import spock.lang.Unroll

import static com.nurkiewicz.tsclass.parser.ast.expr.AdditiveExpression.Operator.MINUS
import static com.nurkiewicz.tsclass.parser.ast.expr.AdditiveExpression.Operator.PLUS
import static com.nurkiewicz.tsclass.parser.ast.expr.MultiplicativeExpression.Operator.DIV
import static com.nurkiewicz.tsclass.parser.ast.expr.MultiplicativeExpression.Operator.MOD
import static com.nurkiewicz.tsclass.parser.ast.expr.MultiplicativeExpression.Operator.MUL

@Unroll
class ExpressionVisitorTest extends Specification {

    def 'should parse identity expression'() {
        when:
            Identifier identifier = parse('x')
        then:
            identifier.name == 'x'
    }

    def 'should parse additive #expr'() {
        when:
            AdditiveExpression additive = parse(expr)
        then:
            (additive.left as NumberLiteral).value == x
            (additive.right as NumberLiteral).value == y
            additive.operator == operator
        where:
            expr      || x | y   | operator
            '1 + 2'   || 1 | 2   | PLUS
            '2 - 1.5' || 2 | 1.5 | MINUS
    }

    def 'should parse complex addition expression'() {
        when:
            AdditiveExpression additive = parse('1 + 2 - 3 + 4 + 5')
        then:
            additive == new AdditiveExpression(
                    new AdditiveExpression(
                            new AdditiveExpression(
                                    new AdditiveExpression(
                                            new NumberLiteral(1),
                                            PLUS,
                                            new NumberLiteral(2)
                                    ),
                                    MINUS,
                                    new NumberLiteral(3)
                            ),
                            PLUS,
                            new NumberLiteral(4)
                    ),
                    PLUS,
                    new NumberLiteral(5)
            )
    }

    def 'should parse multiplication #expr'() {
        when:
            MultiplicativeExpression multiplicative = parse(expr)
        then:
            (multiplicative.left as NumberLiteral).value == x
            (multiplicative.right as NumberLiteral).value == y
            multiplicative.operator == operator
        where:
            expr      || x | y   | operator
            '3 * 4'   || 3 | 4   | MUL
            '7 / 2.5' || 7 | 2.5 | DIV
            '9 % 4'   || 9 | 4   | MOD
    }

    private static Expression parse(String value) {
        String code = """
                class Test {
                    fun(): number {
                        return $value;
                    }
                }
            """
//        AstWindow.open(code)
        ClassDescriptor cls = new Parser().parse(code)
        Method method = cls.methods[0]
        ReturnStatement statement = method.statements[0] as ReturnStatement
        return statement.expression

    }

}
