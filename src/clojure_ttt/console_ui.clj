(ns clojure-ttt.console-ui
  (:require [clojure-ttt.core :refer :all]))

(declare get-current-marker-for-console-display)

(defn print-board
  "Prints out a board in a nice format"
  [board]
  (let [num-of-rows (int (java.lang.Math/sqrt (count board)))]
  (doseq [n (range (count board))]
    (let [current-marker (get-current-marker-for-console-display board n)]
    (if (zero? (mod (inc n) num-of-rows))
      (print (str current-marker "\n"))
      (print (str current-marker " ")))))))

(defn get-current-marker-for-console-display
  "Returns correct character to print to console"
  [board space]
  (if (look-up-space board space)
    (look-up-space board space)
    "_"))

(defn get-user-input
  "Gets input from the user after displaying a message"
  [message]
  (do
    (println message)
    (read-line)))
