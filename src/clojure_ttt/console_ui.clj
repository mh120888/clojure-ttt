(ns clojure-ttt.console-ui
  (:require [clojure-ttt.core :as core]))

(defprotocol IOProtocol
  (io-print-line [type message])
  (io-read [type]))

(deftype ConsoleIO []
  IOProtocol
  (io-print-line [type message]
    (println message))
  (io-read [type]
    (read-line)))

(defn get-user-input
  [io-channel message]
  (do
    (io-print-line io-channel message)
    (io-read io-channel)))

(defn do-you-want-to-go-first
  ([io-channel]
    (do-you-want-to-go-first io-channel "Do you want to go first? Please enter y (yes) or n (no)."))
  ([io-channel message]
    (if-let [response (get #{"y" "n"} (get-user-input io-channel message))]
      response
      (recur io-channel "Sorry, I don't understand. Please enter y (yes) or no (n)."))))

(defn get-board-size
  ([io-channel]
    (get-board-size io-channel "What size board would you like to play on? 3x3 (3) or 4x4 (4)?"))
  ([io-channel message]
    (if-let [response (get #{"3" "4"} (get-user-input io-channel message))]
      (Integer/parseInt response)
      (recur io-channel "Sorry I don't understand. Please tell me which board you want - 3x3 (3) or 4x4 (4)."))))

(defn get-human-marker
  ([io-channel]
    (get-human-marker io-channel "Which marker would you like to use, \"x\" or \"o\"?"))
  ([io-channel message]
    (if-let [response (get #{"x" "y"} (get-user-input io-channel message))]
      response
      (recur io-channel "Sorry I don't understand. Please tell me which marker you would like, \"x\" or \"o\"."))))

(defn get-current-marker-for-console-display
  [board space]
  (if-let [result (core/look-up-space board space)]
    result
    "_"))

(defn print-board
  [io-channel board]
  (let [num-of-rows (int (java.lang.Math/sqrt (count board)))]
    (io-print-line io-channel
      (clojure.string/join "" (map (fn [current-space]
              (if (zero? (mod (inc current-space) num-of-rows))
                (str (get-current-marker-for-console-display board current-space) "\n")
                (str (get-current-marker-for-console-display board current-space) " ")))
        (range (count board)))))))

(defn show-final-result
  [io-channel final-board winner]
    (print-board io-channel final-board)
    (if winner
      (io-print-line io-channel (str winner " won the game."))
      (io-print-line io-channel "Cat's game.")))
