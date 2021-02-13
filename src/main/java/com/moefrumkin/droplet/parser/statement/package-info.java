/**
 * A package that contains the classes to represent and parse statements.
 * In droplet a statement can be represented by the following context free grammar:
 * <ul>
 * 		<li> Statement -> e | CompoundStatement | ConditionalStatement | DeclarationStatement | ExpressionStatement | LoopStatement | ReturnStatement </li>
 *  	<li> CompoundStatement -> { List[Statement] } </li>
 *		<li> ConditionalStatement -> if (Expression) Statement </li>
 *      <li> DeclarationStatement -> Identifier = Expression </li>
 *      <li> ExpressionStatement -> Expression; </li>
 *      <li> LoopStatement -> while(Expression) Statement </li>
 *		<li> ReturnStatement -> return Expression; </li>
 * </ul>
 */
package com.moefrumkin.droplet.parser.statement;