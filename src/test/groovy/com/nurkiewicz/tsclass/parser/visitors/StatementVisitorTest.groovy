package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.parser.AstWindow
import com.nurkiewicz.tsclass.parser.Parser
import com.nurkiewicz.tsclass.parser.ast.Block
import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import spock.lang.Specification

import static com.nurkiewicz.tsclass.parser.ast.If.ifs
import static com.nurkiewicz.tsclass.parser.ast.Return.ret
import static com.nurkiewicz.tsclass.parser.ast.expr.Identifier.ident

class StatementVisitorTest extends Specification {

    def 'should parse function with two statements'() {
        given:
            String code = """
                            if(alpha)
                                return beta;
                            return gamma;
                          """
        when:
            Block block = parse(code)
        then:
            block.statements == [
                    ifs(ident('alpha'), Block.block([ret(ident('beta'))])),
                    ret(ident('gamma'))
            ]
    }

    def 'should parse function with four statements'() {
        given:
            String code = """
                            if(alpha)
                                return beta;
                            if(gamma)
                                return delta;
                            if(epsilon)
                                return zeta;
                            return eta;
                          """
        when:
            Block block = parse(code)
        then:
            block.statements == [
                    ifs(ident('alpha'), Block.block([ret(ident('beta'))])),
                    ifs(ident('gamma'), Block.block([ret(ident('delta'))])),
                    ifs(ident('epsilon'), Block.block([ret(ident('zeta'))])),
                    ret(ident('eta'))
            ]
    }

    private static Block parse(String body, boolean showAst = false) {
        String code = """
                class Test {
                    fun(): number {
                        $body;
                    }
                }
            """
        if (showAst) {
            AstWindow.open(code)
        }
        ClassDescriptor cls = new Parser().parse(code)
        return cls.methods[0].body
    }


}
