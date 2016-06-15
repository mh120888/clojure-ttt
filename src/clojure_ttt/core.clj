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

(defn mark-space
  "Marks a board at the given space and returns the new board"
  [board space mark]
  (if (space-free? board space)
    (assoc board space {:marked mark})
    board))

(def not-empty? (complement empty?))

(defn is-there-a-win?
  "Checks the board for a wins"
  [board num-of-rows spaces-on-board coords-to-check]
    (loop [coords-to-check coords-to-check
           row-to-check (first coords-to-check)
           found-win false]
      (if (or found-win (empty? coords-to-check))
        (if found-win
          true
          false)
          (let [this-row-as-set (into #{} (vals (select-keys board (into [] (first coords-to-check)))))
                is-this-a-win (and (not-empty? (first this-row-as-set))
                                   (= 1 (count this-row-as-set)))]
            (recur (rest coords-to-check) (first (rest coords-to-check)) is-this-a-win)))))

(defn top-left-to-bottom-right-coords
  "Generates a lazy sequence of top left to bottom right coords"
  ([num-of-rows] (top-left-to-bottom-right-coords 0 num-of-rows))
  ([sum num-of-rows]
    (let [new-sum (+ 1 sum num-of-rows)]
      (cons sum (lazy-seq (top-left-to-bottom-right-coords new-sum (+ num-of-rows)))))))

(defn generate-diagonal-coords
  "Returns the winning coordinates for a diagonal win"
  [num-of-rows]
  (take num-of-rows (top-left-to-bottom-right-coords num-of-rows)))

(defn has-won?
  [board]
  (let [num-of-rows (int (java.lang.Math/sqrt (count board)))
        spaces-on-board (range (count board))
        horizontal-coords (partition num-of-rows spaces-on-board)
        vertical-coords (apply map list horizontal-coords)
        diagonal-coords (generate-diagonal-coords num-of-rows)
        coords-to-check (concat horizontal-coords vertical-coords (list diagonal-coords))]
    (is-there-a-win? board num-of-rows spaces-on-board coords-to-check)))
