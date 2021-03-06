package com.nurkiewicz.tsclass.parser.visitors

import com.nurkiewicz.tsclass.parser.MethodParser
import com.nurkiewicz.tsclass.parser.ast.Block
import spock.lang.Specification

import static com.nurkiewicz.tsclass.ExpressionBuilder.ident
import static com.nurkiewicz.tsclass.StatementBuilder.block
import static com.nurkiewicz.tsclass.StatementBuilder.ifs
import static com.nurkiewicz.tsclass.StatementBuilder.ret

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
            Block blk = parse(code)
        then:
            blk.statements.size() == 2
            blk.statements[0] == ifs(
                    ident('alpha'),
                    block([
                            ifs(ident('beta'), block([ret(ident('gamma'))])),
                            ifs(ident('delta'), ret(ident('epsilon'))),
                            ifs(ident('eta'),
                                    block([ret(ident('theta'))]),
                                    block([ret(ident('iota'))])),
                    ])
            )
            blk.statements[1] == ret(ident('eta'))

    }

    private static Block parse(String body, boolean showAst = false) {
        return MethodParser.parse(body, showAst)
    }


}
