(ns clojure-ttt.console-ui
  (:require [clojure-ttt.board :as board]))

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

(defn ask-user-question
  [io-channel messages acceptable-responses]
  (loop [message (first messages)]
    (if-let [response (get acceptable-responses (get-user-input io-channel message))]
        response
        (recur ((comp first reverse) messages)))))

(defn get-human-marker
  [io-channel]
  (ask-user-question io-channel ["Which marker would you like to use, \"x\" or \"o\"?" "Sorry I don't understand. Please tell me which marker you would like, \"x\" or \"o\"."] #{"x" "o"}))

(defn do-you-want-to-go-first
  [io-channel]
  (ask-user-question io-channel ["Do you want to go first? Please enter y (yes) or n (no)." "Sorry, I don't understand. Please enter y (yes) or no (n)."] #{"y" "n"}))

(defn get-board-size
  [io-channel]
  (ask-user-question io-channel ["What size board would you like to play on? 3x3 (3) or 4x4 (4)?" "Sorry I don't understand. Please tell me which board you want - 3x3 (3) or 4x4 (4)."] #{"3" "4"}))

(defn get-current-marker-for-console-display
  [board space]
  (if-let [result (board/look-up-space board space)]
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
  [io-channel final-board human-marker]
    (print-board io-channel final-board)
    (condp = (board/get-winner final-board)
      human-marker (io-print-line io-channel "You won!")
      nil (io-print-line io-channel "Cat's game.")
      (io-print-line io-channel "You lost!")))
