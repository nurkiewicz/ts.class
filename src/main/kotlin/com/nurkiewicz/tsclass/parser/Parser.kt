package com.nurkiewicz.tsclass.parser

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptLexer
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import com.nurkiewicz.tsclass.parser.visitors.ClassVisitor
import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

class Parser {

    @Throws(IOException::class)
    fun parse(sourceCode: String): ClassDescriptor {
        return parse(ByteArrayInputStream(sourceCode.toByteArray(StandardCharsets.UTF_8)))
    }

    @Throws(IOException::class)
    fun parse(sourceCode: InputStream): ClassDescriptor {
        val charStream = CharStreams.fromStream(sourceCode)
        val lexer = TypeScriptLexer(charStream)
        val tokens = CommonTokenStream(lexer)
        val parser = TypeScriptParser(tokens)
        parser.errorListeners.clear()
        parser.addErrorListener(throwingErrorListener())
        return parser.classDeclaration().accept(ClassVisitor())
    }

    private fun throwingErrorListener(): BaseErrorListener {
        return object : BaseErrorListener() {
            override fun syntaxError(recognizer: Recognizer<*, *>?, offendingSymbol: Any?, line: Int, charPositionInLine: Int, msg: String?, e: RecognitionException?) {
                throw RuntimeException("Syntax error at line $line:$charPositionInLine $msg")
            }
        }
    }

}
