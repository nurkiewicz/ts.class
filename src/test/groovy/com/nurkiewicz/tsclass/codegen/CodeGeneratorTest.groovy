package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.CompilationError
import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import com.nurkiewicz.tsclass.parser.ast.Method
import com.nurkiewicz.tsclass.parser.ast.Parameter
import com.nurkiewicz.tsclass.parser.ast.ReturnStatement
import com.nurkiewicz.tsclass.parser.ast.Type
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.google.common.collect.ImmutableList.of
import static com.nurkiewicz.tsclass.parser.ast.expr.AdditiveExpression.add
import static com.nurkiewicz.tsclass.parser.ast.expr.Identifier.ident
import static com.nurkiewicz.tsclass.parser.ast.expr.MultiplicativeExpression.mul
import static com.nurkiewicz.tsclass.parser.ast.expr.NumberLiteral.num

@Unroll
class CodeGeneratorTest extends Specification {

    @Subject
    private CodeGenerator generator = new CodeGenerator(
            new StatementGenerator(new ExpressionGenerator()),
            new AsmEmitter()
    )

    def 'should generate simple class'() {
        given:
            Method methodReturning42 = new Method('answer', new Type('number'), of(), of(new ReturnStatement(num(42))))
            def cls = new ClassDescriptor('Greeter', of(), of(methodReturning42))

        when:
            def bytes = generator.generate(cls)

        then:
            def generatedClass = new ByteArrayClassLoader().loadClass(bytes)
            generatedClass.name == 'Greeter'
            def instance = generatedClass.newInstance()
            generatedClass.getMethod("answer").invoke(instance) == 42.0d
    }

    def 'should generate identity function'() {
        given:
            Method identityFunction = new Method(
                    'identity',
                    new Type('number'),
                    of(new Parameter('input', new Type('number'))),
                    of(new ReturnStatement(ident('input')))
            )
            def cls = new ClassDescriptor('Ident', of(), of(identityFunction))

        when:
            def bytes = generator.generate(cls)

        then:
            def generatedClass = new ByteArrayClassLoader().loadClass(bytes)
            generatedClass.name == 'Ident'
            def instance = generatedClass.newInstance()
            generatedClass.getMethod("identity", double.class).invoke(instance, 17) == 17
    }

    def 'should generate function adding two arguments'() {
        given:
            Method identityFunction = new Method(
                    'addUp',
                    new Type('number'),
                    of(
                            new Parameter('lt', new Type('number')),
                            new Parameter('rt', new Type('number'))
                    ),
                    of(new ReturnStatement(add(ident('lt'), ident('rt'))))  //return lt + rt
            )
            def cls = new ClassDescriptor('Cls', of(), of(identityFunction))

        when:
            def bytes = generator.generate(cls)

        then:
            def generatedClass = new ByteArrayClassLoader().loadClass(bytes)
            def instance = generatedClass.newInstance()
            generatedClass.getMethod("addUp", double.class, double.class).invoke(instance, 2, 3) == 5
    }

    def 'should generate function with expression a + b * (c + d) for: #a + #b * (#c + #d) == #expected'() {
        given:

            def cls = new ClassDescriptor('Cls', of(), of(
                    new Method(
                            'complex',
                            new Type('number'),
                            of(
                                    new Parameter('a', new Type('number')),
                                    new Parameter('b', new Type('number')),
                                    new Parameter('c', new Type('number')),
                                    new Parameter('d', new Type('number')),
                            ),
                            of(new ReturnStatement(add(
                                    ident('a'),
                                    mul(
                                            ident('b'),
                                            add(
                                                    ident('c'),
                                                    ident('d'))))))
                    )))

        when:
            def bytes = generator.generate(cls)

        then:
            def generatedClass = new ByteArrayClassLoader().loadClass(bytes)
            def instance = generatedClass.newInstance()
            java.lang.reflect.Method m = generatedClass.getMethod("complex", double.class, double.class, double.class, double.class)
            m.invoke(instance, a, b, c, d) == expected
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
            Method methodReturning42 = new Method('answer', new Type('number'), of(), of(new ReturnStatement(ident('bogus'))))
            def cls = new ClassDescriptor('Greeter', of(), of(methodReturning42))

        when:
            generator.generate(cls)

        then:
            CompilationError error = thrown(CompilationError)
            error.message.contains('bogus')

    }
}
