package com.nurkiewicz.tsclass.parser

import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import com.nurkiewicz.tsclass.parser.ast.Method
import com.nurkiewicz.tsclass.parser.ast.NumberLiteral
import com.nurkiewicz.tsclass.parser.ast.Parameter
import com.nurkiewicz.tsclass.parser.ast.ReturnStatement
import com.nurkiewicz.tsclass.parser.ast.Type
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
            method.type == new Type('number')
            method.parameters == []
            method.statements.size() == 1
            ReturnStatement statement = method.statements[0] as ReturnStatement
            (statement.expression as NumberLiteral).value == 42.0d
    }

    def 'should parse method with unused parameter'() {
        given:
            String code = """
                class Animal {
                    answer(question: number): number {
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
            method.parameters == [new Parameter("question", "number")]
    }

    def 'should parse method with two parameters'() {
        given:
            String code = """
                class Animal {
                    answer(q: number, hint: string): number {
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
            method.parameters == [
                    new Parameter("q", "number"),
                    new Parameter("hint", "string")
            ]
    }

}
