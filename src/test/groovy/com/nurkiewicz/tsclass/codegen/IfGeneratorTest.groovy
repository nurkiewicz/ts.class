package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.parser.ast.If
import com.nurkiewicz.tsclass.parser.ast.Type
import org.objectweb.asm.Label
import spock.lang.Shared
import spock.lang.Specification

import static com.nurkiewicz.tsclass.StatementBuilder.block
import static com.nurkiewicz.tsclass.StatementBuilder.ifs
import static com.nurkiewicz.tsclass.StatementBuilder.ret
import static com.nurkiewicz.tsclass.parser.ast.expr.Identifier.ident
import static com.nurkiewicz.tsclass.parser.ast.expr.Neg.neg
import static com.nurkiewicz.tsclass.parser.ast.expr.NumberLiteral.num
import static com.nurkiewicz.tsclass.parser.ast.expr.Relational.gte
import static org.objectweb.asm.Opcodes.DCMPL
import static org.objectweb.asm.Opcodes.DLOAD
import static org.objectweb.asm.Opcodes.DNEG
import static org.objectweb.asm.Opcodes.DRETURN
import static org.objectweb.asm.Opcodes.GOTO
import static org.objectweb.asm.Opcodes.IFLT
import static org.objectweb.asm.Opcodes.LDC

class IfGeneratorTest extends Specification {

    public static final String X = 'x'
    @Shared
    private StatementGenerator generator = StatementGenerator.build()

    def 'should generate bytecode for if statement without else block'() {
        given:
            If ifs = ifs(gte(ident(X), num(0)), block([ret(ident(X))]))
            MethodParameters symbols = new MethodParameters([
                    (X): new Symbol.MethodParameter(1, Type.number)
            ], new Empty())

        when:
            List<Bytecode> bytecode = generator.generate(ifs, symbols)
        then:
            bytecode[0] == new Bytecode.IntArg(DLOAD, 1)
            bytecode[1] == new Bytecode.DoubleArg(LDC, 0)
            bytecode[2] == new Bytecode.NoArg(DCMPL)
            bytecode[3] == new Bytecode.Jump(IFLT, labelAt(bytecode, 6))
            bytecode[4] == new Bytecode.IntArg(DLOAD, 1)
            bytecode[5] == new Bytecode.NoArg(DRETURN)
    }

    def 'should generate bytecode for if statement with else block'() {
        given:
            If ifs = ifs(gte(ident(X), num(0)), block([ret(ident(X))]), block([ret(neg(ident(X)))]))
            MethodParameters symbols = new MethodParameters([
                    (X): new Symbol.MethodParameter(1, Type.number)
            ], new Empty())

        when:
            List<Bytecode> bytecode = generator.generate(ifs, symbols)
        then:
            bytecode[0] == new Bytecode.IntArg(DLOAD, 1)
            bytecode[1] == new Bytecode.DoubleArg(LDC, 0)
            bytecode[2] == new Bytecode.NoArg(DCMPL)
            bytecode[3] == new Bytecode.Jump(IFLT, labelAt(bytecode, 7))
            bytecode[4] == new Bytecode.IntArg(DLOAD, 1)
            bytecode[5] == new Bytecode.NoArg(DRETURN)
            bytecode[6] == new Bytecode.Jump(GOTO, labelAt(bytecode, 11))
            bytecode[7] instanceof Bytecode.LabelPlace
            bytecode[8] == new Bytecode.IntArg(DLOAD, 1)
            bytecode[9] == new Bytecode.NoArg(DNEG)
            bytecode[10] == new Bytecode.NoArg(DRETURN)
    }

    private static Label labelAt(List<Bytecode> bytecode, Integer idx) {
        return (bytecode[idx] as Bytecode.LabelPlace).label
    }

}
