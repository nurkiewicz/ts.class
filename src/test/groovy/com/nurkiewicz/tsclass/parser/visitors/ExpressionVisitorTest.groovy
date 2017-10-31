package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.parser.Parser
import com.nurkiewicz.tsclass.parser.ast.AdditiveExpression
import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import com.nurkiewicz.tsclass.parser.ast.Expression
import com.nurkiewicz.tsclass.parser.ast.Identifier
import com.nurkiewicz.tsclass.parser.ast.Method
import com.nurkiewicz.tsclass.parser.ast.MultiplicativeExpression
import com.nurkiewicz.tsclass.parser.ast.NumberLiteral
import com.nurkiewicz.tsclass.parser.ast.Return
import spock.lang.Specification
import spock.lang.Unroll

import static com.nurkiewicz.tsclass.ExpressionBuilder.add
import static com.nurkiewicz.tsclass.ExpressionBuilder.call
import static com.nurkiewicz.tsclass.ExpressionBuilder.div
import static com.nurkiewicz.tsclass.ExpressionBuilder.gt
import static com.nurkiewicz.tsclass.ExpressionBuilder.gte
import static com.nurkiewicz.tsclass.ExpressionBuilder.ident
import static com.nurkiewicz.tsclass.ExpressionBuilder.lt
import static com.nurkiewicz.tsclass.ExpressionBuilder.lte
import static com.nurkiewicz.tsclass.ExpressionBuilder.mod
import static com.nurkiewicz.tsclass.ExpressionBuilder.mul
import static com.nurkiewicz.tsclass.ExpressionBuilder.neg
import static com.nurkiewicz.tsclass.ExpressionBuilder.num
import static com.nurkiewicz.tsclass.ExpressionBuilder.sub
import static com.nurkiewicz.tsclass.parser.ast.AdditiveExpression.Operator.MINUS
import static com.nurkiewicz.tsclass.parser.ast.AdditiveExpression.Operator.PLUS
import static com.nurkiewicz.tsclass.parser.ast.MultiplicativeExpression.Operator.DIV
import static com.nurkiewicz.tsclass.parser.ast.MultiplicativeExpression.Operator.MOD
import static com.nurkiewicz.tsclass.parser.ast.MultiplicativeExpression.Operator.MUL

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

    def 'should parse #expr to #obj'() {
        expect:
            parse(expr) == obj
        where:
            expr                || obj
            '-1'                || neg(1)
            '-x'                || neg(ident('x'))
            '-(2 + 3)'          || neg(add(num(2), num(3)))
            'd() + -3'          || add(call('d'), neg(num(3)))
            '2 + 3 * 6'         || add(num(2), mul(num(3), num(6)))
            '2 + 3 * x'         || add(num(2), mul(num(3), ident("x")))
            '(2 + 3) * x'       || mul(add(num(2), num(3)), ident("x"))
            '2 + 3 / 4 + 5 * 6' || add(add(num(2), div(num(3), num(4))), mul(num(5), num(6)))
            '1 + 2 - 3 + 4 + 5' || add(add(sub(add(num(1), num(2)), num(3)), num(4)), num(5))
            '1 * 2 / 3 * 4 % 5' || mod(mul(div(mul(num(1), num(2)), num(3)), num(4)), num(5))
            '(x * y) + (a / b)' || add(mul(ident("x"), ident("y")), div(ident("a"), ident("b")))
            '1 > 2'             || gt(num(1), num(2))
            'f(x1) < f(x2)'     || lt(call('f', ident('x1')), call('f', ident('x2')))
            '3 >= x'            || gte(num(3), ident('x'))
            '4 <= -1'           || lte(num(4), neg(num(1)))
            '1+2 >= 1*2'        || gte(add(num(1), num(2)), mul(num(1), num(2)))
    }

    def 'should parse method invocation #expr to #obj'() {
        expect:
            parse(expr) == obj
        where:
            expr          || obj
            'f()'         || call("f")
            'g(1)'        || call("g", num(1))
            'hi(x)'       || call("hi", ident("x"))
            'jkl(1, 2)'   || call("jkl", num(1), num(2))
            'm(x, y + 3)' || call("m", ident("x"), add(ident("y"), num(3)))
            '1 + f()'     || add(num(1), call("f"))
            'g(4) * f()'  || mul(call("g", num(4)), call("f"))
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
        Return statement = method.body.statements[0] as Return
        return statement.expression

    }

}
