# Lab: Word Count Histogram

## What is this about?
We will, step-by-step and using unit tests to guide us, build an application that can take a text file, parse it, and
create an ASCII histogram over the frequencies of each word in the file. Running it from the `cljwordgraph.core` namespace will look something like this:

    cljwordgraph.core=> (run "mary.txt")
    against    #
    but        #
    lingered   #
    snow       #
    near       #
    ...
    it         #########
    and        ##########
    lamb       ############
    mary       #############
    the        ##############

## Introduction
Be sure to read the [`INTRODUCTION.md`](INTRODUCTION.md) before you start the lab. In that file, there are instructions on how to find
and install the tools required. It also contains a fairly thorough overview of Clojure as a language. If you're new
to Clojure, you should read that.

## Create project
The solution to the lab is in the `src` folder. The tests used to drive the implementation are in the `test` folder. You
should avoid peeking there. Instead follow the instructions here to incrementally build the tests and the source.

Create a new project somewhere else:

    % lein new cljwordgraph
    % cd cljwordgraph

You should see something like this:

    .
    ├── README
    ├── project.clj
    ├── src
    │   └── cljwordgraph
    │       └── core.clj
    └── test
        └── cljwordgraph
            └── test
                └── core.clj

Note how the namespace in our (otherwise empty) source code file matches its physical location:

    $ cat src/cljwordgraph/core.clj
    (ns cljwordgraph.core)

The namespace of the test file also matches its physical location. The `:use` directive to `ns` makes all functions
available from the namespaces `cljwordgraph.core` and `clojure.test`:

    $ cat test/cljwordgraph/test/core.clj
    (ns cljwordgraph.test.core
      (:use [cljwordgraph.core])
      (:use [clojure.test]))

    (deftest replace-me ;; FIXME: write
      (is false "No tests have been written."))

## Make test work
You should choose between using the psuedo-IDE Clooj or the command line and a text editor. If you choose any other
option, like Emacs, Eclipse, NetBeans, IntelliJ, or whatever, you're on your own. They all have Clojure support, but
there is no chance that I can cater for them all in my instructions.

See the [`INTRODUCTION.md`](INTRODUCTION.md) file for instructions on downloading and getting started with Clooj and Leiningen.

### Using Clooj
1. Open Clooj by double-clicking the downloaded clooj jar.
2. Open your newly created `cljwordgraph` project.
3. Select the `cljwordgraph.test.core.clj` file (not `cljwordgraph.core.clj`).
4. In the menu `REPL`, select `Evaluate entire file`.
5. In the REPL window (lower right), enter `(run-tests)` and hit `Enter`. You should see:

		Testing cljwordgraph.test.core
		FAIL in (replace-me) (NO_SOURCE_FILE:6)
		No tests have been written.
		expected: false
		  actual: false
		Ran 1 tests containing 1 assertions.
		1 failures, 0 errors.
		{:type :summary, :test 1, :pass 0, :fail 1, :error 0}
6. In the test in the source file, change 'false' to 'true'.
7. In the menu `REPL`, select `Evaluate entire file`.
8. In the REPL window (lower right), enter (run-tests) and hit Enter. You should see:

		Testing cljwordgraph.test.core
		Ran 1 tests containing 1 assertions.
		0 failures, 0 errors.

### Using command line and text editor
1. Go to your newly created `cljwordgraph` project.
2. Use Leiningen to run the tests:

		% lein test
3. You should get this result:

		Testing cljwordgraph.test.core
		FAIL in (replace-me) (core.clj:6)
		No tests have been written.
		expected: false
		  actual: false
		Ran 1 tests containing 1 assertions.
		1 failures, 0 errors.
4. Edit the `test/cljwordgraph/test/core.clj` file.
5. Find the test and change `false` to `true`.
6. From the command line, run 'lein test' again. You should see:

		Testing cljwordgraph.test.core
		Ran 1 tests containing 1 assertions.
		0 failures, 0 errors.

## test-gather-words (part 1)
Replace dummy test with a real test; a test for a function `gather-words` that takes a string `s` and splits it on
whitespace and returns a sequence of the words as strings.

    user=> (gather-words "   mary had a\tlittle\n   lamb    ")
    ["mary" "had" "a" "little" "lamb"]

Here is the test:

    (deftest test-gather-words
      (is (= ["mary" "had" "a" "little" "lamb"]
            (gather-words "   mary had a\tlittle\n   lamb    "))
        "splits words on whitespace"))

* If in Clooj: `REPL | Evaluate entire file`
* If on command line: Run `lein test`.

In both cases, you will get a compile error because the function doesn't exist yet.

Add an empty function `gather-words` in `src/cljwordgraph/core.clj`:

    (defn gather-words [s] )

Now try to compile and test again:

* If in Clooj: `REPL | Evaluate entire file`. Then enter `(run-tests)` in the REPL in the lower right, it Enter.
* If on command line: Run `lein test`.

It should compile, but the tests now fail. So, now you should implement `gather-words` to fulfil the test. Make the
function split the string into words.

### Useful functions
* .split (java.lang.String#split)
* [seq](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/seq)

You can use `(doc <function>)` to get brief---but precise---documentation on Clojure functions:

    user=> (doc seq)
    -------------------------
    clojure.core/seq
    ([coll])
      Returns a seq on the collection. If the collection is
        empty, returns nil.  (seq nil) returns nil. seq also works on
        Strings, native Java arrays (of reference types) and any objects
        that implement Iterable.

Use (clojure.java.javadoc/javadoc <class>) to get javadoc of a Java class.

    user=> (clojure.java.javadoc/javadoc String)
    <browser opens with javadoc for java.lang.String>

### Suggested design
Use `.split` on the given string with a regex like `"[ \n]"`. Then call `seq` on it.

### Tips
Clojure strings are Java strings, and any method on a Java string can be called on it, like:

    user=> (.length "apa")
    3

## test-gather-words (part 2)
Add a punctuation scenario to `test-gather-words`, so that words are split on blanks and punctuation.

    user=> (gather-words "., mary, had... a little; lamb!")
    ["mary" "had" "a" "little" "lamb"]

Here is the test:

    (deftest test-gather-words
      (is (= ["mary" "had" "a" "little" "lamb"]
            (gather-words "   mary had a\tlittle\n   lamb    "))
        "splits words on whitespace")
      (is (= ["mary" "had" "a" "little" "lamb"]
            (gather-words "., mary, had... a little; lamb!"))
        "removes punctuation"))

### Useful functions
* [re-seq](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/re-seq)

### Suggested design
Think of it backwards. Instead of splitting on certain non-word characters, try to get a sequence of matches of word
characters.

### Tips
Rather than splitting on a boundary that you don't want, you can get the matches that you _do_ want:

    user=> (re-seq #"a[bc]" "abcdeacde")
    ("ab" "ac")

## test-gather-words (part 3)
Add a lowercase scenario to `test-gather-words`, so that words are converted to lowercase.

    user=> (gather-words "., MaRy, hAd... A liTTle; lAmb!")
    ["mary" "had" "a" "little" "lamb"]

Here is the test:

    (deftest test-gather-words
      (is (= ["mary" "had" "a" "little" "lamb"]
            (gather-words "   mary had a\tlittle\n   lamb    "))
        "splits words on whitespace")
      (is (= ["mary" "had" "a" "little" "lamb"]
            (gather-words "., mary, had... a little; lamb!"))
        "removes punctuation")
      (is (= ["mary" "had" "a" "little" "lamb"]
            (gather-words "., MaRy, hAd... A liTTle; lAmb!"))
        "converts words to lower case"))

### Useful functions
* .toLowerCase (java.lang.String#toLowerCase)
* [fn](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/fn)
* [map](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/map)

### Suggested design
Write an anonymous function that performs the Java method `.toLowerCase` on its argument, then `map` this function over
the sequence that you got from making your previous test work.

### Tips
Again, Clojure strings are Java strings. Any operation you can perform on a Java string can be mapped over a sequence
of strings. However, Java methods are not functions, so you must wrap them in a function.

    user=> (map (fn [x] (.length x)) ["apa" "kalle"])
    (3 5)

Note that the short function syntax can be used if you like. These two function declarations are equivalent:

    (fn [x] (.length x))
    #(.length %)

## test-count-words
Add a test for a function `count-words` that takes a collection `coll` and counts occurrences of elements and returns a
map of element to count.

    user=> (count-words ["why" "mary" "why" "mary" "why"])
    {"mary" 2 "why" 3 }

Here is the test:

    (deftest test-count-words
      (is (= {"mary" 2 "why" 3}
            (count-words ["why" "mary" "why" "mary" "why"]))
        "counts words into a map"))

### Useful functions
* [get](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/get)
* [inc](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/inc)
* [assoc](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/assoc)
* [fn](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/fn)
* [reduce](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/reduce)

### Suggested design
Use reduce with a function, an empty map as the start value, and coll as its collection. The function should take a
map (m) and an element from coll (x) as arguments, and it should do the following:

* get the value corresponding to element x, use zero as default
* increase it
* associate, in the map m, the key x to the new increased value

### Tips
Clojure's reduce function also doubles as a fold function.

In its reduce role, the `reduce` function takes a function `f` and a collection `coll`, initially calls `f` with the 
first two values of `coll`, and then subsequently calls `f` with the previous result from calling `f` and the next 
element in `coll`, until there are no more elements in `coll`, returning the last result of calling `f`.

    user=> (reduce + [1 2 3])
    6

In its fold role, however, `reduce` takes a function `f`, an initial value `val`, and a collection `coll`. It initially 
calls `f` with `val` and the first element in `coll`, then subsequently calls `f` with the previous result from calling
`f` and the next element in `coll`, until there are no more elements in `coll`, returning the last result of `f`. It can 
be thought of like "folding" the collection `coll` around the initial value `val`.

A trivial example of a folding `reduce` is to give a start value 5 to a summing operation:

    user=> (reduce + 5 [1 2 3])
    11

However, `val` can be anything, for example a collection. Here we reduce all elements in a vector using `conj` into an empty list:

    user=> (reduce conj () [1 2 3])
    (3 2 1)

If you pass your own function, it should expect the type of `val` (a map, in this case) and any element of `coll`:

    user=> (reduce (fn [m x] (assoc m x 6)) {} ["a" "b" "b"])
    {"a" 6, "b" 6, "b" 6}

Now, what would happen if we instead of just setting 6 as the value, we retrieve the value corresponding to the given
key from the collection we get passed, increase it and put it back? And if there is no value, we use zero as the
default?

    user=> (reduce (fn [m x] (assoc m x (inc (get m x 0)))) {} ["a" "b" "b"])
    {"b" 2, "a" 1}

We get a function that counts the number of occurrences in coll!

To understand how this works, start from the inside. The `get` function takes a map, a key and a default value:

    user=> (get {} "a" 0)
    0

    user=> (get {"a" 1} "a" 0)
    1

Increasing that will increase the count for that particular word:

    user=> (inc (get {} "a" 0))
    1

Associating the new count to the key in the map will store it for the next round. Remember that reduce will make the
previous result of calling `f` be the first argument to `f` in the next round.

    user=> (assoc {} "a" (inc (get {} "a" 0)))
    {"a" 1}

Simulating the next round in reduce, but now with the element `"b"`, gives:

    user=> (assoc {"a" 1} "b" (inc (get {"a" 1} "b" 0)))
    {"b" 1, "a" 1}

And again with `"b"`:

    user=> (assoc {"b" 1 "a" 1} "b" (inc (get {"b" 1 "a" 1} "b" 0)))
    {"b" 2, "a" 1}

Voilà: a counting function. But it can be simplified further. There is no need to call `get`, since a map is a function
of its keys:

    user=> (reduce (fn [m x] (assoc m x (inc (m x 0)))) {} ["a" "b" "b"])
    {"b" 2, "a" 1}

The short function syntax can be used instead of `fn`, replacing the first argument `m` with `%1` and `x` with
`%2`:

    user=> (reduce #(assoc %1 %2 (inc (%1 %2 0))) {} ["a" "b" "b"])
    {"b" 2, "a" 1}

### Tip for the confused
If this is totally confusing and you simply cannot get it to work, just use the function `frequencies`. It does the same thing as the above code.

    user=> (frequencies ["a" "b" "b"])
    {"a" 1, "b" 2}

## test-sort-counted-words
Add a test for a function `sort-counted-words` that takes a word count map `words` and returns a vector of word count
pairs sorted by count.

    user=> (sort-counted-words {"b" 3 "c" 2 "a" 1})
    (["a" 1] ["c" 2] ["b" 3])

Here is the test:

    (deftest test-sort-counted-words
      (is
        (= [["a" 1] ["c" 2] ["b" 3]]
          (sort-counted-words {"b" 3 "c" 2 "a" 1}))
        "sorts and returns a list of word/count pairs"))

### Useful functions
* [sort-by](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/sort-by)

### Suggested design
Use `sort-by` and pass it a function that picks out the second element of the pair (now, which function could that 
be?). The result will be a sequence sorted by the second element.

### Tips
The `seq` of a map is a sequence of vectors of key-value pairs:

    user=> (seq {:a 1 :b 2})
    ([:a 1] [:b 2])

## test-repeat-str
Add a test for a function `repeat-str` that takes a string `s` and a number `n` and returns a string that repeats `s` 
for `n` times.

    user=> (repeat-str "*" 0)
    ""

    user=> (repeat-str "x" 5)
    "xxxxx"

Here are the tests:

    (deftest test-repeat-str
      (is (= ""
            (repeat-str "*" 0))
        "returns the empty string if count is zero")
      (is (= "xxxxx"
            (repeat-str "x" 5))
        "repeats the input string n times"))

### Useful functions
* [repeat](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/repeat)
* [str](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/str)
* [apply](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/apply)

### Suggested design
Use `repeat` with a count and a string, then `apply` the `str` function to the result.

### Tips
If you have a collection of values that you want to send to a function that expects a varargs, you can use `apply`:

    user=> (apply str (repeat 4 "x"))
    "xxxx"

## test-histogram-entry
Add a test for the function `histogram-entry` that takes a vector containing a `word-count` pair and a maximum `width`,
returning a string with the word of the `word-count` pair, fitting within `width`, plus an extra blank, followed by
a number of hash characters corresponding to the count of the `word-count` pair.

    user=> (histogram-entry ["betty" 6] 7)
    "betty   ######"

     ------- ------
     7 chars 6 chars

Here is the test:

    (deftest test-histogram-entry
      (is (= "betty   ######"
            (histogram-entry ["betty" 6] 7))
        "can generate a single histogram entry"))

### Useful functions
* [with-out-str](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/with-out-str)
* [printf](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/printf)
* [str](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/str)
* [first](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/first)
* [second](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/second)

### Suggested design
Use `printf` to format the string. Call `printf` with a format string for a left-justified string of a certain width
(the word), a blank and another string (the hashes):

    "%-5.5 %s"

The second argument to `printf` should be the first element of the `word-count` parameter. The third argument should be
the result of calling `repeat-str` with a hash character and the second element of the `word-count` parameter.

### Tips
You can use destructuring within the argument list, instead of calling `first` and `second`.

## test-histogram
Add a test for the function histogram that takes a collection of word-count pairs, word-counts, returning a
string of histogram entries, each ended by a newline.

    user=> (histogram [["mary" 2] ["why" 3]])
    "mary ##\nwhy  ###\n"

Here is the test:

    (deftest test-histogram
      (is (= "mary ##\nwhy  ###\n"
            (histogram [["mary" 2] ["why" 3]]))
        "can generate a histogram from word counts"))

### Useful functions
* [let](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/let)
* [max](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/max)
* [apply](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/apply)
* [comp](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/comp)
* [str](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/str)
* [map](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/map)
* [fn](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/fn)

### Suggested design
The function should first find the longest word, which would be the max width. Use 'let' to hold that value for you.
Then map for all elements in word-counts a call to a function that appends a \newline to the result of calling
histogram-entry with the current word-count and the max width.

### Tips
If you have a collection of values that you want to send to a function that expects a varargs, you can use 'apply':

    user=> (max [1 2 3])
    [1 2 3]

    user=> (apply max [1 2 3])
    3

If you want to perform more than one function on an argument, you can use 'comp' to compose functions. Say that you
want to get the first string in a pair of strings, and then count the number of characters in that element, you can
do this:

    user=> ((comp count first) ["apa" "whatever"])
    3

And if you want to do this for a collection of pairs, just pass the composed function to map:

    user=> (map (comp count first) [["apa" "whatever"] ["kalle" "ignore"]])
    (3 5)

And if you want to get the max value of these, use 'apply max':

    user=> (apply max (map (comp count first) [["apa" "whatever"] ["kalle" "ignore"]]))
    5

If you need to use the resulting value in another function call, you can 'let' it:

    user=> (let [max-width (apply max (map (comp count first) coll))]
             (println "max is" max-width))
    max is 5
    nil

## Run the application
Add to `cljwordgraph.core.clj` a function called `run` that takes one argument `file`. The function should read the given file, then gather the words, count them, sort them, create a histogram, and print it.

### Useful functions
* [slurp](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/slurp)
* gather-words
* count-words
* sort-counted-words
* histogram
* [println](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/println)

### Suggested design
#### Traditional
First do it the traditional LISP way, inside out:

    (println (histogram (sort-counted-words (count-words (gather-words (slurp file))))))

#### Advanced
Then look at the arrow macro: [->](http://clojure.github.com/clojure/clojure.core-api.html#clojure.core/->) and see if that can help make it more readable:

    user=> (doc ->)
    ...

Hint:

	user=> (println (inc (count [:a :b :c])))
	"4"
	nil

    user=> (-> [:a :b :c] count inc println)
    "4"
	nil
