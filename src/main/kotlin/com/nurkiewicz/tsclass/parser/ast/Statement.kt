package com.nurkiewicz.tsclass.parser.ast

import com.nurkiewicz.tsclass.parser.ast.expr.Expression

interface Statement

data class ReturnStatement(val expression: Expression): Statement
