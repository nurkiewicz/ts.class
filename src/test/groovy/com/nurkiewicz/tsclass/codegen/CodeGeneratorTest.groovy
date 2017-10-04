package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import com.nurkiewicz.tsclass.parser.ast.Method
import com.nurkiewicz.tsclass.parser.ast.Parameter
import com.nurkiewicz.tsclass.parser.ast.ReturnStatement
import com.nurkiewicz.tsclass.parser.ast.Type
import com.nurkiewicz.tsclass.parser.ast.expr.AdditiveExpression
import spock.lang.Specification
import spock.lang.Subject

import static com.google.common.collect.ImmutableList.of
import static com.nurkiewicz.tsclass.parser.ast.expr.Identifier.ident
import static com.nurkiewicz.tsclass.parser.ast.expr.NumberLiteral.num

class CodeGeneratorTest extends Specification {

    @Subject
    private CodeGenerator generator = new CodeGenerator()

    def 'should generate simple class'() {
        Method methodReturning42 = new Method('answer', new Type('number'), of(), of(new ReturnStatement(num(42))))
        given:
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
                    of(new ReturnStatement(AdditiveExpression.add(ident('lt'), ident('rt'))))  //return lt + rt
            )
            def cls = new ClassDescriptor('Cls', of(), of(identityFunction))

        when:
            def bytes = generator.generate(cls)

        then:
            def generatedClass = new ByteArrayClassLoader().loadClass(bytes)
            def instance = generatedClass.newInstance()
            generatedClass.getMethod("addUp", double.class, double.class).invoke(instance, 2, 3) == 5
    }

}
