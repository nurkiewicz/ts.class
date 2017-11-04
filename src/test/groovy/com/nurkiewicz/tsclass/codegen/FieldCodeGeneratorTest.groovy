package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import com.nurkiewicz.tsclass.parser.ast.Field
import com.nurkiewicz.tsclass.parser.ast.Type
import spock.lang.Specification
import spock.lang.Subject

import static com.nurkiewicz.tsclass.codegen.ByteArrayClassLoader.classFrom

class FieldCodeGeneratorTest extends Specification {

    @Subject
    private CodeGenerator generator = CodeGenerator.build()

    def 'should generate double field in class'() {
        given:
            List<Field> fields = [
                    new Field('state', Type.number)
            ]
            def cls = new ClassDescriptor('Greeter', fields, [])

        when:
            def clsObj = classFrom(generator.generate(cls))

        then:
            def field = clsObj.getDeclaredField('state')
            !field.accessible
            field.type == double.class
            def instance = clsObj.newInstance()
            instance.state == 0.0
    }

    def 'should generate String field in class'() {
        given:
            List<Field> fields = [
                    new Field('str', new Type('string'))
            ]
            def cls = new ClassDescriptor('Greeter', fields, [])

        when:
            def clsObj = classFrom(generator.generate(cls))

        then:
            def field = clsObj.getDeclaredField('str')
            !field.accessible
            field.type == String.class
            def instance = clsObj.newInstance()
            instance.str == null
    }

}
