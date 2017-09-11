package com.nurkiewicz.tsclass.parser

import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import com.nurkiewicz.tsclass.parser.ast.Method
import com.nurkiewicz.tsclass.parser.ast.NumberLiteral
import com.nurkiewicz.tsclass.parser.ast.ReturnStatement
import spock.lang.Specification

class ParserTest extends Specification {

    def 'should parse parameterless method'() {
        given:
            String code = """
                class Animal {
                    answer(): number {
                        return 42;
                    }
                }
            """
        when:
            ClassDescriptor cls = new Parser().parse(code)
        then:
            cls.methods.size() == 1
            Method method = cls.methods[0]
            method.name == 'answer'
            method.type == 'number'
            method.arguments == []
            method.statements.size() == 1
            ReturnStatement statement = method.statements[0] as ReturnStatement
            (statement.expression as NumberLiteral).value == 42.0d
    }

}
