package com.nurkiewicz.tsclass.compiler

import com.nurkiewicz.tsclass.codegen.CodeGenerator
import com.nurkiewicz.tsclass.parser.Parser
import java.io.InputStream

class Compiler(
        private val parser: Parser,
        private val codeGenerator: CodeGenerator) {

    fun compile(code: InputStream): ByteArray {
        val cls = parser.parse(code)
        return codeGenerator.generate(cls)
    }

    companion object {
        @JvmStatic
        fun build() = Compiler(Parser(), CodeGenerator.build())

    }

}

