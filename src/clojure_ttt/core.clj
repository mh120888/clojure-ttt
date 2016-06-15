(ns clojure-ttt.core)

(defn generate-new-board
  "Creates a new game board with the specified number of rows"
  [num-of-rows]
  (let [num-of-spaces (* num-of-rows num-of-rows)]
    (reduce #(assoc %1 %2 {}) {} (take num-of-spaces (range)))))

(defn look-up-space
  "Returns the marker with which a space is marked, or nil if it is unmarked"
  [board space]
  (get-in board [space :marked]))

(defn mark-space
  "Marks a board at the given space and returns the new board"
  [board space mark]
  (assoc board space {:marked mark}))

(defn find-free-spaces
  "Takes a board and returns a new board with only the free spaces"
  [board]
  (into {} (filter #(nil? (look-up-space board (first %))) board)))

(defn find-taken-spaces
  "Takes a board and returns a new board with only the taken spaces"
  [board]
  (into {} (filter #(identity (look-up-space board (first %))) board)))

(defn find-spaces-taken-by
  "Takes a board and returns a new board with only the spaces taken by the given marker"
  [board marker]
  (into {} (filter #(= marker (look-up-space board (first %))) board)))

(defn board-full?
  "Tests whether a given board is full"
  [board]
  (empty? (find-free-spaces board)))

(defn space-free?
  "Tests whether a particular space is free"
  [board space]
  (nil? (look-up-space board space)))