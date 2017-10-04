// Initially cloned from https://github.com/vandermore/Randori-Jackalope/tree/master/antlr4


lexer grammar CommonLexerRules;

@header{
    package com.nurkiewicz.tsclass.antlr.parser;
}

ID
    : [a-zA-Z]+ ;

INT
    : [0-9]+ ;

NEWLINE
    : '\r'? '\n' -> channel(HIDDEN) //NOTE: Does not have '\u2028' Line separator. OR '\u2029' Paragraph separator. Not sure how to insert these.
    ;

WS
    : [ \r\t\u000C\n]+ -> channel(HIDDEN) // u000C is Form Feed
    ;

MULTI_LINE_COMMENT
    : '/*' .*? '*/' -> skip //-> channel(HIDDEN)
    ;

LINE_COMMENT
    : '//' .*? NEWLINE -> skip //-> channel(HIDDEN) //(tl;dr - can't do the ANTLR3 version in ANTLR4) Had to do this instead of LT since the ~ is a NOT operator (characters in the lexer, or tokens in the parser). Having a reference to the LT lexer rule is not currently suppored in ANTLR4, so need to inline the rule reference.
    ;

OPEN_PAREN
    : '('
    ;

CLOSE_PAREN
    : ')'
    ;

COMMA
    : ','
    ;

OPEN_BRACE
    : '{'
    ;

CLOSE_BRACE
    : '}'
    ;