package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.parser.MethodParser
import com.nurkiewicz.tsclass.parser.ast.Statement
import spock.lang.Specification

import static com.nurkiewicz.tsclass.ExpressionBuilder.add
import static com.nurkiewicz.tsclass.ExpressionBuilder.ident
import static com.nurkiewicz.tsclass.ExpressionBuilder.mul
import static com.nurkiewicz.tsclass.ExpressionBuilder.num
import static com.nurkiewicz.tsclass.StatementBuilder.assign
import static com.nurkiewicz.tsclass.StatementBuilder.ret

class AssignmentTest extends Specification {

    def 'should allow defining local variable and returning it'() {
        given:
            String code = """
                             let a: number = 3;
                             let b: number = a + 2 * 3;
                             return b;
                          """
        when:
            List<Statement> statements = parse(code)
        then:
            statements == [
                    assign('a', num(3)),
                    assign('b', add(ident('a'), mul(num(2), num(3)))),
                    ret(ident('b'))
            ]
    }

    private static List<Statement> parse(String code, boolean showAst = false) {
        return MethodParser.parse(code, showAst).statements
    }


}
