(ns clojure-ttt.core)

(declare space-free? look-up-space generate-diagonal-coords is-there-a-win? not-empty? top-left-to-bottom-right-coords top-right-to-bottom-left-coords)

(defn generate-new-board
  "Creates a new game board with the specified number of rows"
  [num-of-rows]
  (let [num-of-spaces (* num-of-rows num-of-rows)]
    (reduce #(assoc %1 %2 {}) {} (range num-of-spaces))))

(defn mark-space
  "Marks a board at the given space and returns the new board"
  [board space mark]
  (if (space-free? board space)
    (merge board {space {:marked mark}})
    board))

(defn space-free?
  "Tests whether a particular space is free"
  [board space]
  (nil? (look-up-space board space)))

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

(defn has-won?
  [board]
  (let [num-of-rows (int (java.lang.Math/sqrt (count board)))
        spaces-on-board (range (count board))
        horizontal-coords (partition num-of-rows spaces-on-board)
        vertical-coords (apply map list horizontal-coords)
        diagonal-coords (generate-diagonal-coords num-of-rows)
        coords-to-check (concat horizontal-coords vertical-coords diagonal-coords)]
    (is-there-a-win? board num-of-rows spaces-on-board coords-to-check)))

(defn is-there-a-win?
  "Checks the board for a win"
  [board num-of-rows spaces-on-board coords-to-check]
    (loop [coords-to-check coords-to-check
           row-to-check (first coords-to-check)
           found-win false]
      (if (or found-win (empty? coords-to-check))
        found-win
        (let [this-row-as-set (into #{} (vals (select-keys board (into [] (first coords-to-check)))))
              is-this-a-win (and (not-empty? (first this-row-as-set))
                                  (= 1 (count this-row-as-set)))
              winning-marker (and is-this-a-win (:marked (first this-row-as-set)))]
          (recur (rest coords-to-check) (first (rest coords-to-check)) winning-marker)))))

(def not-empty? (complement empty?))

(defn stop-game?
  "Returns true either if there is a winner or if the board is full, otherwise returns false"
  [board]
  (or (board-full? board) (and (has-won? board) true)))

(defn generate-diagonal-coords
  "Returns the winning coordinates for a diagonal win"
  [num-of-rows]
  (concat (list (take num-of-rows (top-left-to-bottom-right-coords num-of-rows)))
          (list (take num-of-rows (top-right-to-bottom-left-coords num-of-rows)))))

(defn top-left-to-bottom-right-coords
  "Generates a lazy sequence of top left to bottom right coords"
  ([num-of-rows] (top-left-to-bottom-right-coords 0 num-of-rows))
  ([current incrementer]
    (let [next-num (+ (inc current) incrementer)]
      (cons current (lazy-seq (top-left-to-bottom-right-coords next-num incrementer))))))

(defn top-right-to-bottom-left-coords
    "Generates a lazy sequence of top right to bottom left coords"
    ([num-of-rows] (top-right-to-bottom-left-coords (dec num-of-rows) (dec num-of-rows)))
    ([current incrementer]
      (let [next-num (+ current incrementer)]
        (cons current (lazy-seq (top-right-to-bottom-left-coords next-num incrementer))))))

(defn get-other-marker
  "Returns the other marker"
  [marker]
  (if (= "x" marker)
    "o"
    "x"))
