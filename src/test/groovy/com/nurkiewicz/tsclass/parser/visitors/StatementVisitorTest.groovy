package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.parser.AstWindow
import com.nurkiewicz.tsclass.parser.Parser
import com.nurkiewicz.tsclass.parser.ast.Block
import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import spock.lang.Specification

import static com.nurkiewicz.tsclass.StatementBuilder.ifs
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
                    ifs(ident('alpha'), ret(ident('beta'))),
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
                    ifs(ident('alpha'), ret(ident('beta'))),
                    ifs(ident('gamma'), ret(ident('delta'))),
                    ifs(ident('epsilon'), ret(ident('zeta'))),
                    ret(ident('eta'))
            ]
    }

    def 'should parse if with multiple "then" statements'() {
        given:
            String code = """
                            if(alpha) {
                                if(beta) {
                                    return gamma;
                                }
                                if(delta)
                                    return epsilon;
                                if(eta) {
                                    return theta; 
                                } else {
                                    return iota;
                                }
                            }
                            return eta;
                          """
        when:
            Block block = parse(code)
        then:
            block.statements.size() == 2
            block.statements[0] == ifs(
                    ident('alpha'),
                    Block.block([
                            ifs(ident('beta'), Block.block([ret(ident('gamma'))])),
                            ifs(ident('delta'), ret(ident('epsilon'))),
                            ifs(ident('eta'),
                                    Block.block([ret(ident('theta'))]),
                                    Block.block([ret(ident('iota'))])),
                    ])
            )
            block.statements[1] == ret(ident('eta'))

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
