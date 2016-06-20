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

(defn do-you-want-to-go-first
  "Asks a user if they would like to go first"
  ([] (do-you-want-to-go-first "Do you want to go first? Please enter y (yes) or n (no)."))
  ([message]
  (let [response (get-user-input message)]
    (if (or (= response "y") (= response "n"))
      response
      (recur "Sorry, I don't understand. Please enter y (yes) or no (n).")))))

(defn get-board-size
  "Asks a user what size board they would like"
  ([] (get-board-size "What size board would you like to play on? 3x3 (3) or 4x4 (4)?"))
  ([message]
    (let [response (get-user-input message)]
      (if (or (= response "3") (= response "4"))
        (Integer/parseInt response)
        (recur "Sorry I don't understand. Please tell me which board you want - 3x3 (3) or 4x4 (4).")))))

(defn get-human-marker
  "Asks a user which marker they would like to use"
  ([] (get-human-marker "Which marker would you like to use, \"x\" or \"o\"?"))
  ([message]
    (let [response (get-user-input message)]
      (if (or (= response "x") (= response "o"))
        response
        (recur "Sorry I don't understand. Please tell me which marker you would like, \"x\" or \"o\".")))))

(defn get-next-human-move
  "Asks a user where they would like tp play and checks if the move is valid"
  ([board] (get-next-human-move (str "Where would you like to play? Please enter a number between 0 and " (int (dec (count board)))) board))
  ([message board]
    (let [response (get-user-input message)]
      (if (valid-move? board response)
        (Integer/parseInt response)
        (recur (str "Sorry that's not a valid move. Please enter a number between 0 and " (int (dec (count board)))" that isn't already taken.") board)))))
