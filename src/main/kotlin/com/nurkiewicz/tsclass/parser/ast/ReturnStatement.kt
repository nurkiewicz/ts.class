package com.nurkiewicz.tsclass.parser.ast

import com.nurkiewicz.tsclass.parser.ast.expr.Expression

data class ReturnStatement(val expression: Expression): Statement
