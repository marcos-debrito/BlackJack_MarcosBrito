(ns Minesweeper.game2
  (:require [clojure.core.async :as async :refer [<! >!]]
            [clojure.string :as str]))
; require -> indicates the dependencies that we will use in the project!
; clojure.core.async -> library that handles asynchronous programming

; clojure.string -> ;is optimizing and giving an alias "str" to simplify the code.

;let use vectors to create our board -> vec -> 2D arrays
(defn board-creator
  "create the minesweeper board"
  [lines columns]
  (vec (repeat lines (vec (repeat columns "."))))
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
  [board]
  (let [lines (count board)
        columns (count (first board))]
    (println "   " (str/join "   " (map #(format "%2s" %) (range columns))))
    (doseq [line (map vector (range lines) board)]
      (println (format "%2d |" (first line))
               (str/join " | "
                         (map #(if (= % :bomb) "xx" (if (= % :bomb-exploded) "*" (format "%2s" %))) (second line)))))))

(defn start-game
  []
  (let [lines   5
        columns 5
        bombs   5
        board   (-> (board-creator lines columns)
                    (place-bombs bombs))]
    (loop
      [board board]
      (show-board board)
      )
    ))

(start-game)
