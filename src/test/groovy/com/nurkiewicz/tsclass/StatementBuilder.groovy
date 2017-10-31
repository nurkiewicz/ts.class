package com.nurkiewicz.tsclass

import com.nurkiewicz.tsclass.parser.ast.Block
import com.nurkiewicz.tsclass.parser.ast.Expression
import com.nurkiewicz.tsclass.parser.ast.If
import com.nurkiewicz.tsclass.parser.ast.Return
import com.nurkiewicz.tsclass.parser.ast.Statement

class StatementBuilder {

    static If ifs(Expression condition, Statement block, Statement elseBlock = null) {
        return new If(condition, block, elseBlock)
    }

    static Block block(List<Statement> statements = []) {
        return new Block(statements)
    }

    static Return ret(Expression expression) {
        return new Return(expression)
    }

}
