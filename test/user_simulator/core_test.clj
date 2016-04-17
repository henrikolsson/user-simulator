(ns user-simulator.core-test
  (:require [clojure.test :refer :all]
            [user-simulator.core :refer :all]))

(def test-config {:nick-map [[#"(?i).*FOO.*" "baz"]]
                  :line-regex #"[0-9:]+[^<]+\(@?([^>]+)\) (.+)"
                  :filters ["http"]})
(def test-data (str "12:04 @(someone) one two three\n"
                    "11:22 @(someone) one sx http://example.com\n"
                    "14:05 @(foobar) three two one"))

(deftest can-learn-and-ask
  (let [memory (learn test-config
                      test-data)]
  (is (= "one two three"
         (ask memory "someone")))))

(deftest can-ask
  (is (= "one two"
         (ask {"someone" {"" '("one")
                          "one" '("two")}} "someone"))))

(deftest can-learn
  (is (= {"someone" {"" '("one")
                     "one" '("two")
                     "two" '("three")}
          "baz" {"" '("three")
                 "three" '("two")
                 "two" '("one")}}
         (learn test-config
                test-data))))

(deftest can-parse-log-line
  (is (= {:nick "foobar" :line "one two three"}
         (parse-log-line default-line-regex "12:04 @<foobar> one two three")))
  (is (= {:nick "foobar" :line "one two three"}
         (parse-log-line default-line-regex "12:04 <@foobar> one two three")))
  (is (= {:nick "foobar" :line "one two three"}
         (parse-log-line default-line-regex "12:04:11 <foobar> one two three"))))

(deftest can-safe-trim
  (is (= nil (safe-trim nil)))
  (is (= "abc" (safe-trim " abc "))))

(deftest can-normalize-nick
  (is (= "test" (normalize-nick {} "test")))
  (is (= "moo" (normalize-nick [[#"test" "moo"]] "test"))))

(deftest can-learn-line
  (is (= {"someone"
          {"" '("the")
           "quick" '("brown")
           "the" '("lazy" "quick")
           "brown" '("fox")
           "fox" '("jumps")
           "jumps" '("over")
           "over" '("the")
           "lazy" '("dog")}}
         (learn-line {} {:nick "someone" :line "the quick brown fox jumps over the lazy dog"}))))
