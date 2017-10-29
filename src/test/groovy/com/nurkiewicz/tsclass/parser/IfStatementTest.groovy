package com.nurkiewicz.tsclass.parser

import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import spock.lang.Specification
import spock.lang.Unroll

import static com.nurkiewicz.tsclass.parser.ast.Block.block
import static com.nurkiewicz.tsclass.parser.ast.If.ifs
import static com.nurkiewicz.tsclass.parser.ast.Return.ret
import static com.nurkiewicz.tsclass.parser.ast.expr.AdditiveExpression.add
import static com.nurkiewicz.tsclass.parser.ast.expr.AdditiveExpression.sub
import static com.nurkiewicz.tsclass.parser.ast.expr.Identifier.ident
import static com.nurkiewicz.tsclass.parser.ast.expr.MethodCall.call
import static com.nurkiewicz.tsclass.parser.ast.expr.Neg.neg
import static com.nurkiewicz.tsclass.parser.ast.expr.NumberLiteral.num
import static com.nurkiewicz.tsclass.parser.ast.expr.Relational.gt
import static com.nurkiewicz.tsclass.parser.ast.expr.Relational.gte
import static com.nurkiewicz.tsclass.parser.ast.expr.Relational.lt
import static com.nurkiewicz.tsclass.parser.ast.expr.Relational.lte

@Unroll
class IfStatementTest extends Specification {

    def 'should parse "#ifCode"'() {
        given:
            String code = ifStatement(ifCode)
        when:
            ClassDescriptor cls = new Parser().parse(code)
        then:
            cls.methods[0].body.statements == [expectedAst]
        where:
            ifCode                                       || expectedAst
            'if(num >= 0) return num; else return -num;' || ifs(gte(ident('num'), num(0)), block([ret(ident('num'))]), block([ret(neg(ident('num')))]))
            'if(x < y) return x; else return y;'         || ifs(lt(ident('x'), ident('y')), block([ret(ident('x'))]), block([ret(ident('y'))]))
            'if(x > f(x)) return x;'                     || ifs(gt(ident('x'), call('f', ident('x'))), block([ret(ident('x'))]))
            'if(a <= b + c) return b + (c - a);'         || ifs(lte(ident('a'), add(ident('b'), ident('c'))), block([ret(add(ident('b'), sub(ident('c'), ident('a'))))]))
    }

    String ifStatement(String code) {
        """
                class MathHelper {
                    abs(num: number): number {
                        """ +
                code +
                """
                    }
                }
            """
    }

}