# Droplet

Droplet is a high level interpreted programming language designed for educational purposes.
It is designed to be minimalistic and easy to understand.

## Table of Contents

- [Specification](#Specification)
    - [Tokens](#Tokens)
    - [Functions](#Functions)
    - [Statements](#Statements)
    - [Expressions](#Expressions)
- [Usage](#Usage)
- [Acknowledgements](#Acknowledgements)
- [Contributing](#Contributing)
    
    

## Specification

A program in Droplet has three main types of structures, [Functions](#Functions), [Statements](#Statements), and [Expressions](#Expressions), which are built out of each other and [Tokens](#Tokens).

A program can either be a single function, or a list of functions.
The program entry point must be a parameterless function.
In the case of a single function program, the single function is treated as the entry point.
Otherwise, there must be a function called ```main``` which is treated as the entry point.

### Tokens

A Token is the smallest lexical unit Droplet. There are seven different types of Tokens in Droplet:

- ```WHITESPACE```: Spaces, tabs, or newline symbols which are ignored during parsing
- ```KEYWORD```: The words ```return```, ```while```,```if```,```let```, and```def```, which introduce certain structures
- ```IDENTIFIER```: Words which are not KEYWORDs which represent the names of variables and functions
- ```LITERAL```: A combination of digits which represent a predetermined numerical value
- ```SPECIAL```: The characters ```{```,```}```,```(```,```)```,```;```, and ```,``` which group and delimit structures
- ```OPERATOR```: The strings ```==```,```!=,```,```||```,```&&```,```!```,```+```,```-```,```*```,```<```,```>```,```=```, and ```~``` which represent arithmetic and boolean operations
- ```TERMINATOR```: Token that marks the end of the program

### Functions
A function in Droplet represents a subroutine that can be called from any part of the program.
Functions must be defined at the top level.

A ```Function``` can be defined by the following context free grammar:
- ```FUNCTION -> def IDENTIFIER( IDENTIFIER* ) STATEMENT```

### Statements

A statement in Droplet represents a syntactic structure that effects the control flow of the program.
There are six types of statements in Droplet. They can be represented by the following context free grammar:

- ```STATEMENT -> e | CompoundStatement | ConditionalStatement | DeclarationStatement | ExpressionStatement | LoopStatement | ReturnStatement```
- ```CompoundStatement -> { STATEMENT* }```
- ```ConditionalStatement -> if ( EXPRESSION ) STATEMENT```
- ```DeclarationStatement -> IDENTIFIER = EXPRESSION```
- ```ExpressionStatement -> EXPRESSION;```
- ```LoopStatement -> while ( EXPRESSION ) STATEMENT```
- ```ReturnStatement -> return EXPRESSION;```

### Expressions
An expression in Droplet represents a syntactic structure that can be evaluated to an integer.
There are ? types of expressions in Droplet. They can be represented by the following context free grammar:

- ```Expression -> BinaryOperationExpression | EnclosedExpression | FunctionCallExpression | IdentifierExpression | LiteralExpression | UnaryOperationExpression```
- ```BinaryOperationExpression -> Expression BinaryOperation Expression```
- ```UnaryOperationExpression -> UnaryOperation Expression```
- ```EnclosedExpression -> ( EXPRESSION )```
- ```FunctionCallExpression -> IDENTIFIER ( EXPRESSION * )```
- ```IdentifierExpression -> IDENTIFIER```
- ```LiteralExpression -> LITERAL```
- ```BinaryOperation -> + | - | * | || | && | == | != | < | >```
- ```UnaryOperationExpression -> - | ! | ++ | --```

## Usage

There are three packages provided by the API, ```interpreter```, ```parser```, and ```token```, which contains classes and methods that run programs, parse programs, and tokenize programs, respectively.

To run a program, the string must be token and then parsed.

```java
import com.moefrumkin.droplet.interpreter.basicInterpreter.BasicInterpreter;
import com.moefrumkin.droplet.token.Token;
import com.moefrumkin.droplet.parser.Parser;

class BareMinimum {
  public static void main(String[] args) {
    String program = "";
    List<Token> tokenization = Token.tokenize(program);
    Parser parser = new Parser(tokenization);
    parser.parse().interpret(new BasicInterpreter());
  }
}
```

### Example Programs

#### Printing a number
```
def main() {
  print(0);
}
```

#### Factorial
```
def main() {
  print(factorial(5));
}

def factorial(n) {
  if(n == 0)
    return 1;
  return n * factorial(n - 1);
}
```

## Acknowledgements

I relied heavily on online resources to teach myself the necessary prerequisites for this project. I was particulary inspired by [this video](https://www.youtube.com/watch?v=eF9qWbuQLuw&t=1s) and based the Droplet grammar off of [this diagram](https://raw.githubusercontent.com/bisqwit/compiler_series/master/ep1/jit-conj-parser1.png). I used both [the wikipedia page](https://en.wikipedia.org/wiki/Shunting_yard_algorithm) and [this page](https://aquarchitect.github.io/swift-algorithm-club/Shunting%20Yard/) to understand and implement the shunting-yard algorithm. I relied particularly heavily on the Java documenation and online examples to implement the lexer using Java's [Matcher class](https://docs.oracle.com/javase/7/docs/api/java/util/regex/Matcher.html).

## Contributing

Pull Requests and Issues are welcome.

