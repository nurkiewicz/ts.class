package com.nurkiewicz.tsclass.parser

import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import com.nurkiewicz.tsclass.parser.ast.If
import com.nurkiewicz.tsclass.parser.ast.Method
import spock.lang.Specification

import static com.nurkiewicz.tsclass.parser.ast.Block.block
import static com.nurkiewicz.tsclass.parser.ast.Return.ret
import static com.nurkiewicz.tsclass.parser.ast.expr.Identifier.ident
import static com.nurkiewicz.tsclass.parser.ast.expr.Neg.neg

class IfStatementTest extends Specification {


    def 'should parse simple if-statement'() {
        given:
            String code = """
                class MathHelper {
                    abs(num: number): number {
                        if(num >= 0)
                            return num;
                        else
                            return -num;
                    }
                }
            """
        when:
            ClassDescriptor cls = new Parser().parse(code)
        then:
            cls.name == 'MathHelper'
            cls.methods.size() == 1
            Method method = cls.methods[0]
            method.name == 'abs'
            method.statements == [
                    If.ifs(
                            ident('num'),
                            block([ret(ident('num'))]),
                            block([ret(neg(ident('num')))])
                    )
            ]

    }

}