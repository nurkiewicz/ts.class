package com.nurkiewicz.tsclass.compiler

import com.nurkiewicz.tsclass.codegen.ByteArrayClassLoader
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class CompilerE2eTest extends Specification {

    @Shared
    Compiler compiler = Compiler.build()

    def 'should compile sample file'() {
        given:
            byte[] bytes = compiler.compile(getClass().getResourceAsStream('HelloWorld.ts'))
            Class<?> cls = new ByteArrayClassLoader().loadClass(bytes)
        expect:
            cls.newInstance().abs(x) == abs
        where:
            x  || abs
            13 || 13
            -3 || 3
    }

}
