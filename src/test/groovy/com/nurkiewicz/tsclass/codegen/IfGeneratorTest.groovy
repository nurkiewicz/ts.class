package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.parser.ast.Return
import com.nurkiewicz.tsclass.parser.ast.expr.MethodCall
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

import static com.nurkiewicz.tsclass.parser.ast.Return.ret

class IfGeneratorTest extends Specification {

    @Shared
    private StatementGenerator generator = StatementGenerator.build()

    @Ignore
    def 'should generate bytecode for if statement without else block'() {
        given:
            Return statement = ret(MethodCall.call("foo"))
        when:
            List<Bytecode> bytecode = generator.generate(statement, new Empty())
        then:
            bytecode == [
            ]

    }

}
