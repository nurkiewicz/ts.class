package com.nurkiewicz.tsclass.parser.ast

import com.google.common.collect.ImmutableList

data class ClassDescriptor(
    val name: String,
    val fields: ImmutableList<Field>,
    val methods: ImmutableList<Method>)
