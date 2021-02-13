/**
 * This package provides classes that parse and represent a program in droplet.
 * The grammar of droplet has the following production rules:
 * <ul>
 *     <li> Program -> List[Function] </li>
 *     <li> Function -> Identifier(List[Identifier]){Statement} </li>
 *     <li> Statement -> e | CompoundStatement | ConditionalStatement | DeclarationStatement | ExpressionStatement | LoopStatement | ReturnStatement </li>
 *     <li> CompoundStatement -> { List[Statement] } </li>
 *     <li> ConditionalStatement -> if (Expression) Statement </li>
 *     <li> DeclarationStatement -> Identifier = Expression </li>
 *     <li> ExpressionStatement -> Expression; </li>
 *     <li> LoopStatement -> while(Expression) Statement </li>
 *     <li> ReturnStatement -> return Expression; </li>
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
 *
 * <i> Notes: </i>
 * <ul>
 *     <li> 'e' represents the empty string </li>
 *     <li> An identifier is a single word and cannot be a control flow statement eg: [A-Za-z]+ </li>
 *     <li> I use List[Type] as a shorthand for the Kleene closure for the variable Type, eg. TypeList -> e | Type TypeList</li>
 * </ul>
 */
package com.moefrumkin.droplet.parser;