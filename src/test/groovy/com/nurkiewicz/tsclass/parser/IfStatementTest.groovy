package com.nurkiewicz.tsclass.parser

import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import spock.lang.Specification
import spock.lang.Unroll

import static com.nurkiewicz.tsclass.ExpressionBuilder.add
import static com.nurkiewicz.tsclass.ExpressionBuilder.call
import static com.nurkiewicz.tsclass.ExpressionBuilder.gt
import static com.nurkiewicz.tsclass.ExpressionBuilder.gte
import static com.nurkiewicz.tsclass.ExpressionBuilder.ident
import static com.nurkiewicz.tsclass.ExpressionBuilder.lt
import static com.nurkiewicz.tsclass.ExpressionBuilder.lte
import static com.nurkiewicz.tsclass.ExpressionBuilder.neg
import static com.nurkiewicz.tsclass.ExpressionBuilder.num
import static com.nurkiewicz.tsclass.ExpressionBuilder.sub
import static com.nurkiewicz.tsclass.StatementBuilder.ifs
import static com.nurkiewicz.tsclass.StatementBuilder.ret

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
            'if(num >= 0) return num; else return -num;' || ifs(gte(ident('num'), num(0)), ret(ident('num')), ret(neg(ident('num'))))
            'if(x < y) return x; else return y;'         || ifs(lt(ident('x'), ident('y')), ret(ident('x')), ret(ident('y')))
            'if(x > f(x)) return x;'                     || ifs(gt(ident('x'), call('f', ident('x'))), ret(ident('x')))
            'if(a <= b + c) return b + (c - a);'         || ifs(lte(ident('a'), add(ident('b'), ident('c'))), ret(add(ident('b'), sub(ident('c'), ident('a')))))
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