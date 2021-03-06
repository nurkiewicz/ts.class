package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.CompilationError
import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import com.nurkiewicz.tsclass.parser.ast.Method
import com.nurkiewicz.tsclass.parser.ast.Parameter
import com.nurkiewicz.tsclass.parser.ast.Type
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.google.common.collect.ImmutableList.of
import static com.nurkiewicz.tsclass.ExpressionBuilder.add
import static com.nurkiewicz.tsclass.ExpressionBuilder.call
import static com.nurkiewicz.tsclass.ExpressionBuilder.ident
import static com.nurkiewicz.tsclass.ExpressionBuilder.mul
import static com.nurkiewicz.tsclass.ExpressionBuilder.num
import static com.nurkiewicz.tsclass.ExpressionBuilder.sub
import static com.nurkiewicz.tsclass.StatementBuilder.block
import static com.nurkiewicz.tsclass.StatementBuilder.ret
import static com.nurkiewicz.tsclass.codegen.ByteArrayClassLoader.classFrom
import static java.lang.Math.PI

@Unroll
class CodeGeneratorTest extends Specification {

    @Subject
    private CodeGenerator generator = CodeGenerator.build()


    def 'should generate simple class'() {
        given:
            Method methodReturning42 = new Method('answer', Type.number, of(), block([ret(num(42))]))
            def cls = new ClassDescriptor('Greeter', of(), of(methodReturning42))

        when:
            def bytes = generator.generate(cls)

        then:
            def generatedClass = classFrom(bytes)
            generatedClass.name == 'Greeter'
            def instance = generatedClass.newInstance()
            instance.answer() == 42.0d
    }

    def 'should generate identity function'() {
        given:
            Method identityFunction = new Method(
                    'identity',
                    Type.number,
                    of(new Parameter('input', Type.number)),
                    block([ret(ident('input'))])
            )
            def cls = new ClassDescriptor('Ident', of(), of(identityFunction))

        when:
            def bytes = generator.generate(cls)

        then:
            def generatedClass = classFrom(bytes)
            generatedClass.name == 'Ident'
            generatedClass.newInstance().identity(17) == 17
    }

    def 'should generate function adding two arguments'() {
        given:
            Method identityFunction = new Method(
                    'addUp',
                    Type.number,
                    of(
                            new Parameter('lt', Type.number),
                            new Parameter('rt', Type.number)
                    ),
                    block([ret(add(ident('lt'), ident('rt')))])  //return lt + rt
            )
            def cls = new ClassDescriptor('Cls', of(), of(identityFunction))

        when:
            def bytes = generator.generate(cls)

        then:
            def generatedClass = classFrom(bytes)
            generatedClass.newInstance().addUp(2, 3) == 5
    }

    def 'should generate function with expression a + b * (c + d) for: #a + #b * (#c + #d) == #expected'() {
        given:

            def cls = new ClassDescriptor('Cls', of(), of(
                    new Method(
                            'complex',
                            Type.number,
                            of(
                                    new Parameter('a', Type.number),
                                    new Parameter('b', Type.number),
                                    new Parameter('c', Type.number),
                                    new Parameter('d', Type.number),
                            ),
                            block([ret(add(
                                    ident('a'),
                                    mul(
                                            ident('b'),
                                            add(
                                                    ident('c'),
                                                    ident('d')))))])
                    )))

        when:
            def bytes = generator.generate(cls)
        then:
            def generatedClass = classFrom(bytes)
            generatedClass.newInstance().complex(a, b, c, d) == expected
        where:
            a     | b  | c  | d  || expected
            0     | 0  | 0  | 0  || 0
            1     | 0  | 0  | 0  || 1
            -0.5d | 0  | 10 | 20 || -0.5d
            0     | 2  | 3  | -1 || 0 + 2 * (3 + -1)
            1     | -1 | -3 | -2 || 1 + -1 * (-3 + -2)
    }

    def 'should fail with CompilationError when unknown symbol used'() {
        given:
            Method methodReturning42 = new Method('answer', Type.number, of(), block([ret(ident('bogus'))]))
            def cls = new ClassDescriptor('Greeter', of(), of(methodReturning42))

        when:
            generator.generate(cls)

        then:
            CompilationError error = thrown(CompilationError)
            error.message.contains('bogus')
    }

    def 'should call another private function'() {
        given:
            Method methodCallingBar = new Method('foo', Type.number, of(), block([ret(call('bar'))]))
            Method methodBar = new Method('bar', Type.number, of(), block([ret(num(PI))]))
            ClassDescriptor cls = new ClassDescriptor('Foo', of(), of(methodCallingBar, methodBar))
            def bytes = generator.generate(cls)
        when:
            def generatedClass = classFrom(bytes)

        then:
            generatedClass.newInstance().foo() == PI
    }

    def 'should call another private function with arguments'() {
        given:
            Method methodCallingSub = new Method('buzz', Type.number, of(), block([ret(call('sub', num(8), num(5)))]))
            Method methodSub = new Method(
                    'sub',
                    Type.number,
                    of(new Parameter("s1", Type.number), new Parameter("s2", Type.number)),
                    block([ret(sub(ident('s1'), ident('s2')))]))
            ClassDescriptor cls = new ClassDescriptor('Calculator', of(), of(methodCallingSub, methodSub))
            def bytes = generator.generate(cls)
        when:
            def generatedClass = classFrom(bytes)

        then:
            generatedClass.newInstance().buzz() == 3
    }
}
