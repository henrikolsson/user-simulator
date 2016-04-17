(ns user-simulator.core
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def default-line-regex #"[0-9:]+[^<]+<@?([^>]+)> (.+)" )

(defn safe-trim [s]
  (if s
    (.trim s)
    s))

(defn parse-log-line [line-regex line]
  (let [groups (re-find line-regex line)]
    {:nick (safe-trim (second groups))
     :line (safe-trim (nth groups 2))}))

(defn normalize-nick [nick-map nick]
  (or
   (second
    (first
     (filter (fn [[pattern replacement]]
               (re-matches pattern nick))
             nick-map)))
   nick))

(defn learn-line [state message]
  (let [words (seq (.split (:line message) " "))]
    (second
     (reduce
      (fn [[previous new-state] word]
        [word (update-in new-state [(:nick message) previous] conj word)])
      ["" state] words))))

(defn learn [config data]
  (->> (seq (.split data "\n"))
       (filter (fn [line]
                 (not (some #(.contains line %1) (:filters config)))))
       (map #(parse-log-line (or (:line-regex config)
                                 default-line-regex)
                             %1))
       (filter #(:nick %1))
       (map #(update %1 :nick
                     (fn [nick]
                       (normalize-nick (:nick-map config) nick))))
       (reduce learn-line {})))

(defn ask [memory who]
  (string/join
   " "
   (loop [current ""
          chain []]
     (let [next (rand-nth (get-in memory [who current]))]
       (if next
         (recur next
                (conj chain next))
         chain)))))
