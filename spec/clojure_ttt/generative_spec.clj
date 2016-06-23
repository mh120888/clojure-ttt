(ns clojure-ttt.generative-spec
  (:require [speclj.core :refer :all]
            [clojure-ttt.core :as core]
            [clojure-ttt.game :as game]
            [clojure-ttt.player :as player])
  (:import [clojure_ttt.player ComputerPlayer]))

(def human-marker "x")

(def ai-marker "o")
(def computer-player (ComputerPlayer.))

(defn simulate-all-possible-human-moves
  [boards human-marker]
  ((comp vec flatten) (map (fn [board] (map #(core/mark-space board % human-marker) (keys (core/find-free-spaces board)))) boards)))

(defn simulate-all-computer-moves
  [boards ai-marker]
  ((comp vec flatten) (map (fn [board] (core/mark-space board (player/get-move computer-player board ai-marker) ai-marker)) boards)))

(defn simulate-all-possible-games
  ([] (simulate-all-possible-games human-marker [(core/generate-new-board 3)] []))
  ([current-marker
    in-progress-boards
    completed-boards]
  (if (empty? in-progress-boards)
    completed-boards
    (if (= human-marker current-marker)
      (let [new-in-progress-boards (simulate-all-possible-human-moves in-progress-boards human-marker)]
        (recur ai-marker (vec (remove #(core/stop-game? %) new-in-progress-boards)) (vec (filter #(core/stop-game? %) new-in-progress-boards))))
      (let [new-in-progress-boards (simulate-all-computer-moves in-progress-boards ai-marker)]
        (recur human-marker (vec (remove #(core/stop-game? %) new-in-progress-boards)) (vec (filter #(core/stop-game? %) new-in-progress-boards))))))))

; (describe "testing simulate-all-possible-games"
;   (it "runs"
;     (should-not= false (println (simulate-all-possible-games ai-marker [{0 {:marked "x"}, 1 {}, 2 {}, 3 {}, 4 {}, 5 {}, 6 {}, 7 {}, 8 {}} {0 {}, 1 {}, 2 {}, 3 {}, 4 {}, 5 {}, 6 {}, 7 {:marked "x"}, 8 {}} {0 {}, 1 {:marked "x"}, 2 {}, 3 {}, 4 {}, 5 {}, 6 {}, 7 {}, 8 {}} {0 {}, 1 {}, 2 {}, 3 {}, 4 {:marked "x"}, 5 {}, 6 {}, 7 {}, 8 {}} {0 {}, 1 {}, 2 {}, 3 {}, 4 {}, 5 {}, 6 {:marked "x"}, 7 {}, 8 {}} {0 {}, 1 {}, 2 {}, 3 {:marked "x"}, 4 {}, 5 {}, 6 {}, 7 {}, 8 {}} {0 {}, 1 {}, 2 {:marked "x"}, 3 {}, 4 {}, 5 {}, 6 {}, 7 {}, 8 {}} {0 {}, 1 {}, 2 {}, 3 {}, 4 {}, 5 {:marked "x"}, 6 {}, 7 {}, 8 {}} {0 {}, 1 {}, 2 {}, 3 {}, 4 {}, 5 {}, 6 {}, 7 {}, 8 {:marked "x"}}] [])))))

(describe "testing simulate-all-possible-games"
  (it "runs"
    (should-not= false (println (simulate-all-possible-games)))))

; ([] (simulate-all-possible-games human-marker (core/generate-new-board 3) []))

; IF human's turn
;   human-player moves by mapping over array of in-progress boards and playing on every available space
; ELSE
;   computer-player moves by calling get-move with ComputerPlayer
; IF in-progress-boards is empty, return finished-boards
; ELSE recur with updated boards
    ; for every board in in-progress-boards, check if there is a winner
;   IF yes, move that board from in-progress-boards to finished-boards

(run-specs)
