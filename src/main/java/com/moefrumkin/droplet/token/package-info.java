/**
 * This package handles the lexical analysis of droplet tokens.
 * There are six types of tokens:
 * <ul>
 *     <li>Whitespace - Spaces, Tabs, Newlines, etc</li>
 *     <li>Keywords - Control flow and assignments tokens such as {@code while} and {@code let}</li>
 *     <li>Identifiers - Names for variables and functions</li>
 *     <li>Literals - Integer values</li>
 *     <li>Special Tokens - Tokens that help structure the program such as '{', ')', and ','</li>
 *     <li>Operators - Binary or unary operations that can be performed on literals and variables</li>
 * </ul>
 */
package com.moefrumkin.droplet.token;