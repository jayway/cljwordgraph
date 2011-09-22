(ns cljwordgraph.test.core
  (:use [cljwordgraph.core])
  (:use [clojure.test]))

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

(deftest test-count-words
  (is (= {"mary" 2 "why" 3 } 
        (count-words ["why" "mary" "why" "mary" "why"]))
    "counts words into a map"))

(deftest test-sort-counted-words
  (is 
    (= [["a" 1] ["c" 2] ["b" 3]]
      (sort-counted-words {"b" 3 "c" 2 "a" 1}))
    "sorts and returns a list of word/count pairs"))

(deftest test-repeat-str
  (is (= "" 
        (repeat-str "*" 0))
    "returns the empty string if count is zero")
  (is (= "xxxxx" 
        (repeat-str "x" 5))
    "repeats the input string n times"))

(deftest test-histogram-entry
  (is (= "betty   ######" 
        (histogram-entry ["betty" 6] 7))
    "can generate a single histogram entry"))

(deftest test-histogram
  (is (= "mary ##\nwhy  ###\n" 
        (histogram [["mary" 2] ["why" 3]]))
    "can generate a histogram from word counts"))
