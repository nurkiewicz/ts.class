package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import com.nurkiewicz.tsclass.parser.ast.Method
import com.nurkiewicz.tsclass.parser.ast.Parameter
import com.nurkiewicz.tsclass.parser.ast.ReturnStatement
import com.nurkiewicz.tsclass.parser.ast.Type
import com.nurkiewicz.tsclass.parser.ast.expr.MethodCall
import org.objectweb.asm.Opcodes
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static com.nurkiewicz.tsclass.parser.ast.ReturnStatement.ret
import static com.nurkiewicz.tsclass.parser.ast.expr.Identifier.ident
import static org.objectweb.asm.Opcodes.DLOAD
import static org.objectweb.asm.Opcodes.DRETURN
import static org.objectweb.asm.Opcodes.INVOKESPECIAL

@Unroll
class StatementGeneratorTest extends Specification {

    public static final String CLASS_NAME = "Foo"
    @Shared
    private StatementGenerator generator = new StatementGenerator(new ExpressionGenerator())

    private static ClassSymbols outerClass() {
        List<Method> methods = [
                new Method('foo', Type.number, [], []),
                new Method('bar', Type.number, [
                        new Parameter('a', Type.number),
                        new Parameter('b', Type.number)
                ], []),
        ]
        return new ClassSymbols(new ClassDescriptor(CLASS_NAME, [], methods), new Empty())
    }

    def 'should generate return statement calling another private method without arguments'() {
        given:
            ReturnStatement statement = ret(MethodCall.call("foo"))
        when:
            List<Bytecode> bytecode = generator.generate(statement, outerClass())
        then:
            bytecode == [
                    new Bytecode.IntArg(Opcodes.ALOAD, 0),
                    new Bytecode.Call(INVOKESPECIAL, CLASS_NAME, new Method('foo', Type.number, [], []), false),
                    new Bytecode.NoArg(DRETURN)
            ]
    }

    private static final String ONE = 'x'
    private static final String TWO = 'y'

    def 'should generate return statement calling another private method using two of its arguments'() {
        given:
            MethodCall callToBar = MethodCall.call("bar", ident(ONE), ident(TWO))
            ReturnStatement statement = ret(callToBar)
            MethodParameters methodParameters = new MethodParameters([
                    (ONE): new Symbol.MethodParameter(1, Type.number),
                    (TWO): new Symbol.MethodParameter(3, Type.number)
            ], outerClass())
        when:
            List<Bytecode> bytecode = generator.generate(statement, methodParameters)
        then:
                    bytecode[0] == new Bytecode.IntArg(Opcodes.ALOAD, 0)
                    bytecode[1] == new Bytecode.IntArg(DLOAD, 1)
                    bytecode[2] == new Bytecode.IntArg(DLOAD, 3)
                    bytecode[3] == new Bytecode.Call(INVOKESPECIAL, CLASS_NAME, new Method('bar', Type.number, [new Parameter('a', Type.number), new Parameter('b', Type.number)], []), false)
                    bytecode[4] == new Bytecode.NoArg(DRETURN)
    }

}
