package com.nurkiewicz.tsclass.parser.visitors;

import com.nurkiewicz.tsclass.antlr.parser.TypeScriptBaseVisitor;
import com.nurkiewicz.tsclass.antlr.parser.TypeScriptParser;
import com.nurkiewicz.tsclass.parser.ast.expr.Expression;
import com.nurkiewicz.tsclass.parser.ast.expr.Identifier;
import com.nurkiewicz.tsclass.parser.ast.expr.NumberLiteral;

class ExpressionVisitor extends TypeScriptBaseVisitor<Expression> {
        @Override
        public Expression visitAssignmentExpression(TypeScriptParser.AssignmentExpressionContext ctx) {
            return ctx.accept(new TypeScriptBaseVisitor<Expression>() {
                @Override
                public Expression visitLogicalORExpression(TypeScriptParser.LogicalORExpressionContext ctx) {
                    return ctx.accept(new TypeScriptBaseVisitor<Expression>() {
                        @Override
                        public Expression visitBitwiseORExpression(TypeScriptParser.BitwiseORExpressionContext ctx) {
                            return ctx.accept(new TypeScriptBaseVisitor<Expression>() {
                                @Override
                                public Expression visitBitwiseXORExpression(TypeScriptParser.BitwiseXORExpressionContext ctx) {
                                    return ctx.accept(new TypeScriptBaseVisitor<Expression>() {
                                        @Override
                                        public Expression visitBitwiseANDExpression(TypeScriptParser.BitwiseANDExpressionContext ctx) {
                                            return ctx.accept(new TypeScriptBaseVisitor<Expression>() {
                                                @Override
                                                public Expression visitEqualityExpression(TypeScriptParser.EqualityExpressionContext ctx) {
                                                    return ctx.accept(new TypeScriptBaseVisitor<Expression>() {
                                                        @Override
                                                        public Expression visitRelationalExpression(TypeScriptParser.RelationalExpressionContext ctx) {
                                                            return ctx.accept(new TypeScriptBaseVisitor<Expression>() {
                                                                @Override
                                                                public Expression visitShiftExpression(TypeScriptParser.ShiftExpressionContext ctx) {
                                                                    return ctx.accept(new TypeScriptBaseVisitor<Expression>() {
                                                                        @Override
                                                                        public Expression visitAdditiveExpression(TypeScriptParser.AdditiveExpressionContext ctx) {
                                                                            return ctx.accept(new TypeScriptBaseVisitor<Expression>() {
                                                                                @Override
                                                                                public Expression visitMultiplicativeExpression(TypeScriptParser.MultiplicativeExpressionContext ctx) {
                                                                                    return ctx.accept(new TypeScriptBaseVisitor<Expression>() {
                                                                                        @Override
                                                                                        public Expression visitUnaryExpression(TypeScriptParser.UnaryExpressionContext ctx) {
                                                                                            return ctx.accept(new TypeScriptBaseVisitor<Expression>() {
                                                                                                @Override
                                                                                                public Expression visitPostfixExpression(TypeScriptParser.PostfixExpressionContext ctx) {
                                                                                                    return ctx.accept(new TypeScriptBaseVisitor<Expression>() {
                                                                                                        @Override
                                                                                                        public Expression visitLeftHandSideExpression(TypeScriptParser.LeftHandSideExpressionContext ctx) {
                                                                                                            return ctx.accept(new TypeScriptBaseVisitor<Expression>() {
                                                                                                                @Override
                                                                                                                public Expression visitNewExpression(TypeScriptParser.NewExpressionContext ctx) {
                                                                                                                    return ctx.accept(new TypeScriptBaseVisitor<Expression>() {
                                                                                                                        @Override
                                                                                                                        public Expression visitMemberExpression(TypeScriptParser.MemberExpressionContext ctx) {
                                                                                                                            return ctx.accept(new TypeScriptBaseVisitor<Expression>() {
                                                                                                                                @Override
                                                                                                                                public Expression visitPrimaryExpression(TypeScriptParser.PrimaryExpressionContext ctx) {
                                                                                                                                    if (ctx.IDENT() != null) {
                                                                                                                                        return new Identifier(ctx.IDENT().getText());
                                                                                                                                    }
                                                                                                                                    return ctx.accept(new TypeScriptBaseVisitor<Expression>() {
                                                                                                                                        @Override
                                                                                                                                        public Expression visitLiteral(TypeScriptParser.LiteralContext ctx) {
                                                                                                                                            return new NumberLiteral(Double.valueOf(ctx.NUMERIC_LITERAL().getText()));
                                                                                                                                        }
                                                                                                                                    });
                                                                                                                                }
                                                                                                                            });
                                                                                                                        }
                                                                                                                    });
                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    });
                                                                                }
                                                                            });
                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            });
        }
    }