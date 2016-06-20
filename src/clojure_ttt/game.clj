(ns clojure-ttt.game
  (:require [clojure-ttt.core :refer :all]
    [clojure-ttt.console-ui :refer :all]
    [clojure-ttt.ai-player :refer :all]))

(declare get-starting-marker)

(defn play-game
  []
    (println "Let's play a game of tic tac toe")
    (let [human-marker (get-human-marker)
          human-goes-first (do-you-want-to-go-first)
          starting-marker (get-starting-marker human-marker human-goes-first)
          new-board (generate-new-board (get-board-size))]
          (print-board new-board)
          (loop [current-marker starting-marker
                board new-board
                human-marker human-marker]
            (if (= human-marker current-marker)
              (let [next-board (mark-space board (get-next-human-move board) current-marker)]
                (print-board next-board)
                (if (stop-game? next-board)
                  (println "Game over")
                  (recur (get-other-marker current-marker) next-board human-marker)))
              (let [next-board (mark-space board (get-next-move board current-marker) current-marker)]
                (print-board next-board)
                (if (stop-game? next-board)
                  (println "Game over")
                  (recur (get-other-marker current-marker) next-board human-marker)))))
    )
)

  ; ask for move
          ; make move
          ; check if game is over
              ; IF yes, check if there is a winner
              ; ELSE recur with the other marker, current board, and same human marker

(defn get-starting-marker
  [human-marker human-goes-first]
  (if (= "y" human-goes-first)
    human-marker
    (get-other-marker human-marker)))
