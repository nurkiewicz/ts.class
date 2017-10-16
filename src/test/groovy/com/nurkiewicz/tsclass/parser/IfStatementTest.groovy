package com.nurkiewicz.tsclass.parser

import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import com.nurkiewicz.tsclass.parser.ast.Method
import spock.lang.Specification

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

            ]

    }

}