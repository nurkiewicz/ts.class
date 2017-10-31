package com.nurkiewicz.tsclass.parser

import com.nurkiewicz.tsclass.parser.ast.Block
import com.nurkiewicz.tsclass.parser.ast.ClassDescriptor
import com.nurkiewicz.tsclass.parser.ast.Method

class MethodParser {

    static Block parse(String value, boolean showAst) {
        String code = """
                class Test {
                    fun(): number {
                        $value;
                    }
                }
            """
        if (showAst) {
            AstWindow.open(code)
        }
        ClassDescriptor cls = new Parser().parse(code)
        Method method = cls.methods[0]
        return method.body

    }

}
