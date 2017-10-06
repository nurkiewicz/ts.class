package com.nurkiewicz.tsclass

import com.nurkiewicz.tsclass.parser.Parser
import java.io.IOException

object Compiler {

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val programStream = Compiler::class.java.getResourceAsStream("Greeter.ts")
        val cls = Parser().parse(programStream)
        println(cls)
    }
}
