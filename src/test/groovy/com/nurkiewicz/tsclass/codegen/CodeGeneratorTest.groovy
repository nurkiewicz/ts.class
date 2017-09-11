package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import com.nurkiewicz.tsclass.parser.ast.Method
import spock.lang.Specification
import spock.lang.Subject

import static com.google.common.collect.ImmutableList.of

class CodeGeneratorTest extends Specification {

    @Subject
    private CodeGenerator generator = new CodeGenerator()

    def 'should generate empty class'() {
        given:
        def cls = new ClassDescriptor('Greeter', of(), of(new Method("answer", "number", of())))

        when:
        def bytes = generator.generate(cls)

        then:
        def generatedClass = new ByteArrayClassLoader().loadClass(bytes)
        generatedClass.name == 'Greeter'
        def instance = generatedClass.newInstance()
        generatedClass.getMethod("answer").invoke(instance) == 42.0d
    }

}
