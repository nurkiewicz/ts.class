package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.parser.ast.Statement
import com.nurkiewicz.tsclass.parser.ast.Type
import spock.lang.Shared
import spock.lang.Specification

import static com.nurkiewicz.tsclass.ExpressionBuilder.add
import static com.nurkiewicz.tsclass.ExpressionBuilder.ident
import static com.nurkiewicz.tsclass.ExpressionBuilder.num
import static com.nurkiewicz.tsclass.StatementBuilder.assign
import static com.nurkiewicz.tsclass.StatementBuilder.block
import static com.nurkiewicz.tsclass.StatementBuilder.ret
import static org.objectweb.asm.Opcodes.DADD
import static org.objectweb.asm.Opcodes.DLOAD
import static org.objectweb.asm.Opcodes.DRETURN
import static org.objectweb.asm.Opcodes.DSTORE
import static org.objectweb.asm.Opcodes.LDC

class AssignmentTest extends Specification {

    @Shared
    private StatementGenerator generator = StatementGenerator.build()

    def 'should generate local variable and be able to reference it'() {
        given:
            Statement statement = block([
                    assign('x', num(7)),
                    ret(ident('x'))
            ])
        and:
            MethodParameters symbols = new MethodParameters([:], new Empty())

        when:
            List<Bytecode> bytecode = generator.generate(statement, symbols).bytecode
        then:
            bytecode == [
                    new Bytecode.DoubleArg(LDC, 7.0),
                    new Bytecode.IntArg(DSTORE, 1),
                    new Bytecode.IntArg(DLOAD, 1),
                    new Bytecode.NoArg(DRETURN),
            ]
    }

    def 'should generate return two local variables'() {
        given:
            Statement statement = block([
                    assign('x', num(7)),
                    assign('y', num(-4)),
                    ret(add(ident('x'), ident('y')))
            ])
        and:
            MethodParameters symbols = new MethodParameters([:], new Empty())

        when:
            List<Bytecode> bytecode = generator.generate(statement, symbols).bytecode
        then:
            bytecode == [
                    new Bytecode.DoubleArg(LDC, 7.0),
                    new Bytecode.IntArg(DSTORE, 1),
                    new Bytecode.DoubleArg(LDC, -4.0),
                    new Bytecode.IntArg(DSTORE, 3),
                    new Bytecode.IntArg(DLOAD, 1),
                    new Bytecode.IntArg(DLOAD, 3),
                    new Bytecode.NoArg(DADD),
                    new Bytecode.NoArg(DRETURN),
            ]
    }

    def 'should create local variable based on method parameter'() {
        given:
            Statement statement = block([
                    assign('x', ident('p1')),
                    assign('y', ident('p2')),
                    ret(add(ident('x'), ident('y')))
            ])
        and:
            MethodParameters symbols = new MethodParameters([
                    'p1': new Symbol.MethodParameter(1, Type.number),
                    'p2': new Symbol.MethodParameter(3, Type.number)
            ], new Empty())

        when:
            List<Bytecode> bytecode = generator.generate(statement, symbols).bytecode
        then:
            bytecode == [
                    new Bytecode.IntArg(DLOAD, 1),
                    new Bytecode.IntArg(DSTORE, 5),
                    new Bytecode.IntArg(DLOAD, 3),
                    new Bytecode.IntArg(DSTORE, 7),
                    new Bytecode.IntArg(DLOAD, 5),
                    new Bytecode.IntArg(DLOAD, 7),
                    new Bytecode.NoArg(DADD),
                    new Bytecode.NoArg(DRETURN),
            ]
    }

}
