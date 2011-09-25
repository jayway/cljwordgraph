# Getting started
The [Clojure web site](http://clojure.org) is the canonical source for Clojure. It provides a rationale for creating
the language. There's a
[Clojure cheat sheet](http://clojure.org/cheatsheet) which can be handy.

The [4Clojure](http://4clojure.com/) website provides simple "fill-in-the-blanks" exercises that provide a gentle
introduction to the language. And if you find the first few ones dead simple, don't worry. The exercises get harder.

The [labrepl](https://github.com/relevance/labrepl) is also a nice way to get started. Just clone the labrepl
repo, run script/repl to get a REPL, and browse to localhost:8080 for some exercises. See the
[labrepl wiki](https://github.com/relevance/labrepl/wiki) for more instructions.

# Preparations
## Leiningen
* [Download and install Leiningen](https://github.com/technomancy/leiningen)
* [Getting Started with Leiningen](http://dev.clojure.org/display/doc/Getting+Started+with+Leiningen)

## Choose and setup text editor
Here are some guides for various text editors:

* [Getting Started with JEdit](http://dev.clojure.org/display/doc/Getting+Started+with+JEdit)
* [Getting Started with Textmate](http://dev.clojure.org/display/doc/Getting+Started+with+Textmate)
* [Getting Started with Vim](http://dev.clojure.org/display/doc/Getting+Started+with+Vim)
* [Getting Started with Emacs](http://dev.clojure.org/display/doc/Getting+Started+with+Emacs)

# Prologue
Clojure is just [a jar file](http://search.maven.org/remotecontent?filepath=org/clojure/clojure/1.2.1/clojure-1.2.1.jar).
It's not a package with executables, compilers, and stuff that need to be installed and available in your path. This
makes it a bit different to install and use, compared to say Ruby, Python, Groovy, and Scala.

You can load Clojure source files like they are scripts:

    % java -cp lib/clojure.jar clojure.main mysource.clj

It's common to incrementally grow a program by interacting with a running instance of Clojure using a REPL
(Read-Eval-Print-Loop), which gives you a prompt that might look like this: `user=>`:

    $ java -cp lib/clojure.jar clojure.main
    Clojure 1.2.1
    user=>

At this prompt, you can directly call functions and evaluate things:

    user=> (+ 1 2 3)
    6

In practice, getting the classpath correct using nothing but the command line quickly becomes too hard. We need some
dependency management tool. One such tool is [Leiningen](https://github.com/technomancy/leiningen).
Download the latest stable [`lein`](https://raw.github.com/technomancy/leiningen/stable/bin/lein) script, install
it in your path, make sure it's executable, and run:

    % lein self-install

That's all you need to do in order to have Leiningen working. You can use Leiningen in an ad-hoc fashion, if you just
want try some Clojure code without having to create a project:

    % lein repl
    REPL started; server listening on localhost port 17304
    user=> (+ 1 2 3)
    6

However, often we need some external dependencies, or we want our code encapsulated in some kind of project. Leiningen
can create a project for us, with a README file, a project file, plus skeleton source and test files:

    $ lein new asdf
    Created new project in: /tmp/asdf

    $ tree asdf
    asdf/
    ├── README
    ├── project.clj
    ├── src
    │   └── asdf
    │       └── core.clj
    └── test
        └── asdf
            └── test
                └── core.clj

The `project.clj` file contains the project definitions:

    (defproject asdf "1.0.0-SNAPSHOT"
      :description "FIXME: write description"
      :dependencies [[org.clojure/clojure "1.2.1"]])

This is how we add a dependency to, say, Spring LDAP:

    (defproject asdf "1.0.0-SNAPSHOT"
      :description "FIXME: write description"
      :dependencies [[org.clojure/clojure "1.2.1"]
                     [org.springframework.ldap/spring-ldap-core "1.3.1.RELEASE"]])

We tell Leiningen to retrieve the dependencies:

    $ lein deps
    Copying 12 files to /tmp/asdf/lib

Our directory now looks like this:

    .
    ├── README
    ├── classes
    ├── lib
    │   ├── aopalliance-1.0.jar
    │   ├── clojure-1.2.1.jar
    │   ├── commons-lang-2.5.jar
    │   ├── commons-logging-1.0.4.jar
    │   ├── spring-aop-3.0.5.RELEASE.jar
    │   ├── spring-asm-3.0.5.RELEASE.jar
    │   ├── spring-beans-3.0.5.RELEASE.jar
    │   ├── spring-context-3.0.5.RELEASE.jar
    │   ├── spring-core-3.0.5.RELEASE.jar
    │   ├── spring-expression-3.0.5.RELEASE.jar
    │   ├── spring-ldap-core-1.3.1.RELEASE.jar
    │   └── spring-tx-3.0.5.RELEASE.jar
    ├── project.clj
    ├── src
    │   └── asdf
    │       └── core.clj
    └── test
        └── asdf
            └── test
                └── core.clj

# REPL

Clojure is a LISP and is therefore Homoiconic (from _homo_ meaning _the same_ and _icon_ meaning _representation_).
Code is written in the data structures of the language itself. There is no shoving a file of text through a compiler
and getting something executable in return. The Clojure compiler only sees proper data structures, not text. It works
like this: Text is read by the [Reader](http://clojure.org/reader), which converts the text into
[data structures](http://clojure.org/data_structures). The data structures (lists, vectors,
maps and sets) are passed to the [Evaluator](http://clojure.org/evaluation), which [compiles](http://clojure.org/compilation)
them into byte code. The byte code is loaded into the JVM and executed, and the return value is Printed. This repeats
in a Loop. Read-Eval-Print-Loop = REPL.

One important consequence of separating the Reader from the Evaluator like this, is that it enables us to write code
that writes code, ie [macros](http://clojure.org/macros).

# Clojure syntax

## Numbers
Clojure numbers are Java's Number. However, Clojure integers provide automatic promotion to BigInteger when needed.
There is no silent wrapping when they grow beyond a MAX_VALUE. Numbers can be as big as your memory allows:

    user=> (def a Long/MAX_VALUE)

    user=> a
    9223372036854775807

    user=> (* a a)
    85070591730234615847396907784232501249

Clojure also provides Ratio, which is what you get when dividing two integers that don't yield another integer:

    user=> (/ 22 7)
    22/7

Any further operations on a Ratio will try to simplify it:

    user=> (- 22/7 1/7)
    3

Any operation on a Ratio that involved a double will bring us back to double land:

    user=> (* 22/7 1.0)
    3.142857142857143

## Symbols
Symbols are names. Unlike most other languages, a symbol is not a reference to some storage. However, symbols can be
bound to various references using the [special form](http://clojure.org/special_forms) `def`. Here we bind the symbol
`x` to the number 42:

    user=> (def x 42)
    #'user/x

The result of binding the symbol to a value is indeed a reference to some storage (in this case a [var](http://clojure.org/vars))
with the name `x` and the namespace `user`. Evaluating the symbol will resolve the binding and print the underlying
value:

    user=> x
    42

## Keywords
A keyword is like a Java enum or a Ruby symbol. It starts with a colon and is followed by its name, like `:foo`. A
keyword always evaluates to itself:

    user=> :foo
    :foo

Multiple keywords with the same name are not only equal, but in fact identical. They are the same reference. Equality
checks on keywords are very fast, which make keywords useful as keys in maps.

Keywords also implement clojure.lang.IFn, which make them callable as a function. More about that later.

## Data structures
Clojure has four compound data structures: list, vector, map, and set. They are all heterogeneous and can store any
values or other data structures, both as keys and as values. Since all data structures are immutable, hash codes will
be calculated once and are then cached. Any modifications of a data structure will return a new version of it. This is
made highly performant using structural sharing. No copying of elements is done.

### List
A literal list is written as space-separated entries within parentheses:

    (1 2 3)

Lists are treated specially by Clojure. Unless quoted, they will be treated as function calls. The first element is
the function that will be called, while the remaining elements will be the arguments to the function. The above list
will fail, since `1` is not a function:

    user=> (1 2 3)
    java.lang.Integer cannot be cast to clojure.lang.IFn

In order to get the literal list, quoting is required:

    user=> (quote (1 2 3))
    (1 2 3)

or shorter:

    user=> '(1 2 3)
    (1 2 3)

Lists are singly-linked. New elements will be added (conjoined) at the front:

    user=> (conj '(1 2 3) :x)
    (:x 1 2 3)

Anything that implements the interface `clojure.lang.IFn` can be used as the first element in a list, ie as the function
to be called. Here is an example of calling the function `+` with some numbers:

    user=> (+ 1 2 3)
    6

Here we call `count` to get the number of elements in a list:

    user=> (count '(1 2 3))
    3

### Vector
A literal vector is written as space-separated entries within square brackets:

    [1 2 3]

Contrary to lists, there is no special treatment of vectors. A vector evaluates to itself:

    user=> [1 2 3]
    [1 2 3]

Elements of a vector can be accessed by index. Vectors are functions of their indices, so we can call a vector with an
index and get the corresponding value:

    user=> (def v [1 2 3])
    user=> (v 0)
    1

New elements will be added at the end.

    user=> (conj [1 2 3] :x)
    [1 2 3 :x]

Vectors are used in many places in Clojure, for example as argument lists when defining functions.

### Map
Clojure has several types of maps. Most commonly used is the hash-map. A literal hash-map is written as space-separated
key-value pairs within curly braces:

    {:a 1 :b 2 :c 3}

A map evaluates to itself:

    user=> {:a 1 :b 2 :c 3}
    {:a 1, :b 2, :c 3}

Note that maps print with commas between the key-value pairs. Commas are whitespace and can be used or not used, as
one pleases.

Logically, a map could be thought of as a function of keys to values. In fact, maps implement `clojure.lang.IFn` and you can call a map with a key. If the key exists, you'll get the corresponding element. If not, you'll get `nil`.

    user=> (def m {:a 1 :b 2 :c 3})
    user=> (m :b)
    2
    user=> (m :x)
    nil

Maps can be conjoined to, just like lists and vectors. You conjoin a map to a map:

    user=> (conj m {:x "a string value"})
    {:x "a string value", :a 1, :b 2, :c 3}

It's a bit awkward to have to create a map from keys and values that you want to add or update, so in practise `assoc`
is more commonly used. It takes a map and one or more key-value pairs:

    user=> (assoc m [:b] 2 :x ["a" "vector"])
    {:x ["a" "vector"], [:b] 2, :a 1, :b 2, :c 3}

Above you see that other data structures can easily be used, both as values and keys. Note that our original `m` hasn't
changed in all this mocking around:

    user=> m
    {:a 1, :b 2, :c 3}

#### Maps and keywords
As we mentioned earlier, keywords are also "functionable things". You can actually call a keyword with a map as the
argument. The keyword will look itself up in the map:

    user=> (:b m)
    2
    user=> (:x m)
    nil

This makes for concise yet readable code. This snippet returns red if the ant has food, otherwise black.

    (if (:food ant) Color/red Color/black)

### Set
Clojure has several types of sets. A literal hash-set is written as a hash sign followed by space-separated entries
within curly braces:

    #{:a :b :c}

A set makes sure there are no duplicates:

    user=> (conj #{:a :b :c} :a)
    #{:a :c :b}

As with maps, sets are in fact functions of their keys. You can ask a set whether a key is present or not:

    user=> (def s {:a :b :c})
    user=> (s :b)
    :b
    user=> (s :x)
    nil

#### Sets and keywords
As for maps, you can call a keyword with a set as the argument. The keyword will look itself up in the set:

    user=> (:b s)
    :b
    user=> (:x s)
    nil

## Sequences
Clojure abstracts the data structures just mentioned into something called [sequences](http://clojure.org/sequences) or
_seqs_ for short. The `seq` function is used to create a seq of something:

    user=> (seq [1 2 3])
    (1 2 3)

    user=> (seq {:a 1 :b 2})
    ([:a 1] [:b 2])

Seqs support two functions:

* first
* rest

The `first` function returns the first element of the sequence:


    user=> (first (seq [1 2 3]))
    1

The `rest` function returns _a new sequence_ containing all but the first element:

    user=> (rest (seq [1 2 3]))
    (2 3)

Seqs differ from iterators in that they are not stateful cursors into a collection. They are also persistent and
immutable. While iterators are designed for use in a "foreach" scenario only, seqs are much more useful:

* functions can consume and produce seqs
* seqs are thread safe
* seqs can share structure
* seqs can be lazy

One cool thing is that seqs also work with Strings, native Java arrays and objects that implement Iterable.

    user=> (seq "abc")
    (\a \b \c)

    user=> (seq (.getBytes "abc"))
    (97 98 99)

The Clojure sequence library is a multitude of functions that work on sequences. Programming in Clojure is very much
about mastering [the sequence library](http://clojure.org/sequences#Sequences-The%20Seq%20library).

## Functions
You can define your own [functions](http://clojure.org/functional_programming) like this:

    user=> (defn square [x] (* x x))

If you look at the definition, you see that it is in fact a list, containing:

* a function call: `defn`
* a symbol representing the name of the function: `square`
* a vector of symbols signifying the argument list (in this a single argument named `x`): `[x]`
* the function implementation (in this case a multiplication of `x` and `x`): `(* x x)`

A function implementation may be empty, whereas the function will return nil:

    user=> (defn ignore [x])
    user=> (ignore 3)
    nil

If the implementation consists of a single value, like `42` or `:foo` or `[1 2 3]`, that value will be the return value:

    user=> (defn identity [x] x)
    user=> (identity 3)
    3

Finally, if it consists of one or more lists, these will be function calls, and the return value will be the result of
the last one:

    user=> (defn square [x] (* x x))
    user=> (square 3)
    9

This is coding in the data structures of the language itself. After the initial shock, it usually dawns upon the coder
that it's actually quite convenient to be able to program using nothing but lists, vectors, maps and sets.

Custom functions are called like any other function:

    user=> (square 3)
    9

## Useful constructs
### let
Local variables are of course needed to eliminate repetition and to divide the code into parts. The
[`let`](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/let)
construct takes a vector containing pairs of the symbol to bind to and the expression to bind. The vector is followed
by the code that should be performed within the scope of the let:

    user=> (let [m (max 1 2 3)]
             (str "max is " m))
    "max is 3"

The "variables" within a `let` are really immutable, so there is no risk of accidentally changing a local variable.

### Destructuring bind
In all binding expressions, like argument lists or `let` bindings, destructuring can be used to get at the pieces of
the expression, right at the time of the binding. Without destructuring, you are forced to first bind the expression,
then split it. Below we first bind the pair to `p` and then split it using `first` and `second`:

    user=> (defn print-point [p]
             (str "x is " (first p) ", y is " (second p)))

    user=> (print-point [3 4])
    "x is 3, y is 4"

Using destructuring, you can instead split right at the time of binding, by mirroring the expression with a similar
structure containing symbols. A vector with pairs can be destructured using `[[x y]]` instead of just `[p]` as argument
list:

    user=> (defn print-point [[x y]]
             (str "x is " x ", y is " y))

If you need a reference to the complete structure, you can use `:as` to name the original:

    user=> (defn print-point [[x y :as p]]
             (str "x is " x ", y is " y ", p is " p))

    user=> (print-point [3 4])
    "x is 3, y is 4, p is [3 4]"

Not only vectors can be destructured, but also maps or any sequences of known or unknown (or even infinite) length.
However, destructuring a simple vector of pairs is enough for now.

## Namespaces

Clojure supports namespaces. They group functions and definitions together. By convention, files are named as the last
part of the namespace. For example, for the namespace `asdf.core`, the file is named `core.clj`, is placed in the folder
`src/asdf`, and contains the following namespace declaration:

    (ns asdf.core)

The ns macro takes "directives", like `:import` for importing Java classes:

    (ns asdf.core
      (:import [javax.swing JFrame]))

Or `:use` for importing functions from Clojure namespaces:

    (ns asdf.test.core
      (:use asdf.core))

## Tests
Tests are defined using the `deftest` macro found in the `clojure.test` namespace.

    user=> (deftest test-count
             (is (= 4
                    (count ["a" "s" "d" "f"]))
               "count returns the number of elements"))

The functions under test in `asdf.core`, as well as the `clojure.test` functions and macros, are made available with the
`:use` directive to the `ns` namespace macro:

    (ns asdf.test.core
      (:use [asdf.core])
      (:use [clojure.test]))

Tests can be run easily from the command line using Leiningen:

    % lein test

The result might look something like this:

    Testing asdf.test.core
    FAIL in (replace-me) (core.clj:6)
    No tests have been written.
    expected: false
      actual: false
    Ran 1 tests containing 1 assertions.
    1 failures, 0 errors.

They can also be run from the REPL using run-tests. We first load the file with the test code, to make sure all
definitions are evaluated:

    user=> (load-file "/src/asdf/test/asdf/test/core.clj")
    user=> (run-tests 'asdf.test.core)
    Testing asdf.test.core
    Ran 1 tests containing 1 assertions.
    1 failures, 0 errors.
    {:type :summary, :test 1, :pass 0, :fail 1, :error 0}

# Misc
Create a Maven pom file:

    % lein pom

The pom file can be used for importing the project into Eclipse, NetBeans, or IntelliJ. Beware that once using the pom,
you're leaving the Leiningen world. Changes in `project.clj` will not be picked up when using these tools.

The pom is not needed if using Leiningen with a text editor.
