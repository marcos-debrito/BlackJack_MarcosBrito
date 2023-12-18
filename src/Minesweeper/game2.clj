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
  (vec (repeat lines (vec (repeat columns "x"))))
  )

(defn place-bombs
  "place bombs randomly on the map"
  [board bombs]
  (loop [board board bombs bombs]
    (if (zero? bombs)
      board
      (let [line (rand-int (count board))
            column (rand-int (count (first board)))]
        (recur (assoc-in board [line column] :bomb)
               (dec bombs))))))



(defn start-game
  []
  (let [lines   5
        columns 5
        bombs   5
        board   (-> (board-creator lines columns)
                    (place-bombs bombs))]

    (println "Board created:")
    (println board)))

(start-game)
