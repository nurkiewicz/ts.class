package com.nurkiewicz.tsclass.parser

import com.google.common.io.Files
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser
import org.antlr.v4.gui.TestRig

import java.awt.*
import java.nio.charset.StandardCharsets

class AstWindow {
    static void open(String sourceCode) {
        File input = File.createTempFile(AstWindow.name + '_', ".ts", new File(System.getProperty("java.io.tmpdir")))
        Files.append(sourceCode, input, StandardCharsets.UTF_8)
        def output = File.createTempFile(AstWindow.name + '_', ".ps", new File(System.getProperty("java.io.tmpdir")))
        def rig = new TestRig(TypeScriptParser.name.replace('Parser', ''),
                "classDeclaration",
                input.path,
                "-ps",
                output.path
        )
        rig.process()
        Desktop.desktop.open(output)
    }
}