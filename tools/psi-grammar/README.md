# `psi-grammar`

This is the grammar used in the `ideabuck` plugin.

## Overview

This source uses [JFlex](http://www.jflex.de/) to generate a lexer and
[the JetBrains Grammar-Kit plugin](https://github.com/JetBrains/Grammar-Kit)
to generate a parser.  The lexer and parser definitions are in `*.flex` and
`*.bnf` files, respectively.

The lexer is fairly straightforward, but the parser is built using two-pass
generation, and its source is kept separately from the rest of `ideabuck`
to make the build easier to understand and less error-prone to work on.

## Getting started in IntelliJ

To develop the grammar in IntelliJ, you will need to generate the lexers
and parsers using:

    buck build //tools/psi-grammar:lexers //tools/psi-grammar:parsers

And regenerate them whenever the `bnf` or `flex` files change.  (See
the "Tips" section, below, for instructions on how to automate this.)

## What is two-pass parser generation?

Two pass generation solves a chicken-and-egg problem in parser construction.

This module's `BUCK` and  `.buckconfig` grammars delegate some implementation
details to user-supplied ["mixin" classes](https://en.wikipedia.org/wiki/Mixin).

To delegate functionality to a Java class, Grammar-Kit requires a compiled
version of that class.  However, compiling the mixin class often requires code
generated by parser.  :-(

A solution to this problem is to generate an incomplete parser without
the user-supplied classes (pass 1), and use the incomplete parser to compile
the mixin classes.  The compiled mixin classes are then used to generate
a complete parser (pass 2), which in turn is used to compile the rest of
the grammar.

## Tips for working with this code

* Consider installing [the JetBrains Grammar-Kit plugin](https://github.com/JetBrains/Grammar-Kit)
  from `tools/java/grammar-kit`, but **do not use the Grammar-Kit context menu
  items to generate lexers/parsers**, as it does single-pass generation that
  may or may not match the Buck build.

* Beware of relying on stale artifacts for IDE autocompletion!  After making
  changes to `*.flex` or `*.bnf` files, immediately recompile the
  `//tools/psi-grammar:lexers` or `//tools/psi-grammar:parsers` targets,
  respectively, to avoid developing against stale code.  (If you have the 
  [File Watchers](https://plugins.jetbrains.com/plugin/7177-file-watchers) plugin
  installed, you can copy the `watcherTasks.xml` file from this directory into
  the buck project's `.idea` directory to automate this.)
  
* Be aware that first-pass failures do not invalidate second-pass artifacts!  If
  the `//tools/psi-grammar:parsers` fails at an early step, 
  
* Be aware of the chicken-and-egg nature of two-pass parser generation.  When adding
  a mixin class that must be present for the second pass, be sure to include it in
  the `MIXIN_SRCS`.  If your class has parts that are necessary for a mixin, and other
  parts that cannot be compiled that way, often the situation can be remedied by splitting
  the functionality into separate classes and only declaring one of them to be a mixin.

