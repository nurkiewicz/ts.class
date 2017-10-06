package com.nurkiewicz.tsclass.parser.ast

data class ClassDescriptor(
    val name: String,
    val fields: List<Field>,
    val methods: List<Method>)

data class Field(val name: String, val type: String)
