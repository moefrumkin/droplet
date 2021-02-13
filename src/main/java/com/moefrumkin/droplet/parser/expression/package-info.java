/**
 * This packages contains the classes that represent an expression in the droplet language. An expression is a groups of tokens that can be evaluated to an integer.
 * A droplet statement can be represented by the following context free grammar:
 * <ul>
 *     <li> Expression -> BinaryOperationExpression | EnclosedExpression | FunctionCallExpression | IdentifierExpression | LiteralExpression | UnaryOperationExpression </li>
 *     <li> BinaryOperationExpression -> Expression BinaryOperation Expression </li>
 *     <li> EnclosedExpression -> ( Expression ) </li>
 *     <li> FunctionCallExpression -> Identifier(List[Expression]) </li>
 *     <li> IdentifierExpression -> Identifier </li>
 *     <li> LiteralExpression -> Literal </li>
 *     <li> UnaryOperationExpression -> UnaryOperation Expression </li>
 *     <li> BinaryOperation -> = | + | - | * | '||' | && | == | != | < | > </li>
 *     <li> UnaryOperation -> - | ! | ++ | -- </li>
 * </ul>
 */
package com.moefrumkin.droplet.parser.expression;