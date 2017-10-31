# ts.class - TypeScript to Java bytecode compiler

[![Build Status](https://travis-ci.org/nurkiewicz/ts.class.svg?branch=master)](https://travis-ci.org/nurkiewicz/ts.class)
[![codecov](https://codecov.io/gh/nurkiewicz/ts.class/branch/master/graph/badge.svg)](https://codecov.io/gh/nurkiewicz/ts.class)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

**ts.class** compiles [TypeScript](https://www.typescriptlang.org/) source into JVM bytecode (`.class` files) that can interoperate both ways with Java ecosystem.
This is a work-in-progress with very little features so far.

## Rationale

* use the same code for front-end and back-end (which is not JavaScript)
* server-side performance by avoiding transpilation to dynamically typed JavaScript

It's truly polyglot: TypeScript compiler, written in Kotlin, tested with Groovy, targeting Java VM.