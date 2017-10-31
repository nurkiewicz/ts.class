package com.nurkiewicz.tsclass

import com.nurkiewicz.tsclass.parser.ast.If
import com.nurkiewicz.tsclass.parser.ast.Statement
import com.nurkiewicz.tsclass.parser.ast.expr.Expression

class StatementBuilder {

    static If ifs(Expression condition, Statement block, Statement elseBlock = null) {
        new If(condition, block, elseBlock)
    }

}
