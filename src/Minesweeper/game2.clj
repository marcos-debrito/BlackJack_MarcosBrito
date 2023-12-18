(ns Minesweeper.game2
  (:require [clojure.string :as str]))
; require -> indicates the dependencies that we will use in the project!
; clojure.core.async -> library that handles asynchronous programming

; clojure.string -> ;is optimizing and giving an alias "str" to simplify the code.

;let use vectors to create our board -> vec -> 2D arrays
(defn board-creator
  "create the minesweeper board"
  [lines columns]
  (vec (repeat lines (vec (repeat columns :0))))
  )

(defn place-bombs
  "place bombs randomly on the board"
  [board bombs]
  (loop [board board bombs bombs]
    (if (zero? bombs)
      board
      (let [line (rand-int (count board))
            column (rand-int (count (first board)))]
        (recur (assoc-in board [line column] :bomb)
               (dec bombs))))))

(defn show-board
  "render all the board on terminal"
  [board]
  (let [lines (count board)
        columns (count (first board))]
    (println "   " (str/join "   " (map #(format "%2s" %) (range columns))))
    (doseq [line (map vector (range lines) board)]
      (println (format "%2d |" (first line))
               (str/join " | "
                         (map #(if (= % :bomb) "xx" (if (= % :bomb-exploded) "*" (format "%2s" %))) (second line)))))))



;fn -> anonimous function (fn [parameter] body's function)
(defn counting-neighboring-bombs
  "checks how many bombs there are around the player's choice"
  [board line column]
  (let [lines (count board)
        columns (count (first board))
        position [[-1 -1] [-1 0] [-1 1]
                  [0 -1]         [0 1]
                  [1 -1]  [1 0]  [1 1]]]
    (->> position
         (map (fn [[dx dy]] [(+ line dx) (+ column dy)]))
         (filter (fn [[x y]]
                   (and (>= x 0) (< x lines)
                        (>= y 0) (< y columns))))
         (map (fn [[x y]] (get-in board [x y])))
         (filter #(= :bomb %))
         count)))

(defn update-board
  "Update the board after the player's choice [ line  column ]"
  [board line column]
  (if (= :0 (get-in board [line column]))
    (update-in board [line column] (constantly (counting-neighboring-bombs board line column)))
    board))

(defn player-move
  [board line column]
  (if (= :bomb (get-in board [line column]))
    (assoc-in board [line column] :bomb-exploded)
    (update-board board line column)))

(defn lost?
  "function that verify if player lost the game"
  [board]
  (some #(= :bomb-exploded %) (flatten board)))

(defn start-game
  "responsible for managing the game"
  []
  (let [lines   5
        columns 5
        bombs   5
        board   (-> (board-creator lines columns)
                    (place-bombs bombs))]
    (loop [board board]
      (show-board board)
      (if (lost? board) (println "Você perdeu!")
                        (do
                          (println "Informe a linha: ")
                          (let [line (read)
                                column (do
                                         (println "Informe a coluna: ")
                                         (read))]
                            (recur (player-move board line column)))))
      )
    ))

(start-game)
