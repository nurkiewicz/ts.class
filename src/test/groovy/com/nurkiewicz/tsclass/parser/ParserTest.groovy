package com.nurkiewicz.tsclass.parser

import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import com.nurkiewicz.tsclass.parser.ast.Identifier
import com.nurkiewicz.tsclass.parser.ast.Method
import com.nurkiewicz.tsclass.parser.ast.NumberLiteral
import com.nurkiewicz.tsclass.parser.ast.Parameter
import com.nurkiewicz.tsclass.parser.ast.Return
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
            method.type == Type.number
            method.parameters == []
            method.body.statements.size() == 1
            Return statement = method.body.statements[0] as Return
            (statement.expression as NumberLiteral).value == 42.0d
    }

    def 'should parse identity function'() {
        given:
            String code = """
                class Identity {
                    identity(x: string): number {
                        return x;
                    }
                }
            """
        when:
            ClassDescriptor cls = new Parser().parse(code)
        then:
            Method method = cls.methods[0]
            method.parameters == [new Parameter('x', new Type('string'))]
            method.body.statements.size() == 1
            Return statement = method.body.statements[0] as Return
            (statement.expression as Identifier).name == 'x'
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
            method.parameters == [new Parameter("question", new Type("number"))]
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
                    new Parameter("q", new Type("number")),
                    new Parameter("hint", new Type("string"))
            ]
    }

}
