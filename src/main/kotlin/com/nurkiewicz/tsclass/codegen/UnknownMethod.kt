package com.nurkiewicz.tsclass.codegen

import com.nurkiewicz.tsclass.CompilationError
import com.nurkiewicz.tsclass.parser.ast.MethodCall

class UnknownMethod(call: MethodCall):
        CompilationError("Can't find method for name ${call.name} with ${call.parameters.size} parameters")