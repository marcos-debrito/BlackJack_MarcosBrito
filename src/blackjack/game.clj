(ns blackjack.game
  (:require [card-ascii-art.core :as card]))

;Available cards
; A, 2, 3, 4, 5, 6, 7, 8, 9, 10,  J,  Q,  K <=>
;[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13]

;Random cards -> (rand-int 20) less 20!

(defn new-card
  []
  "Generates a card number between 1 and 13"
  (inc (rand-int 13)))
;(println (new-card))

;Calculate points
;J, Q, K = 10 points
;[A 5 7] = 1+5+7 (13 points)  ou ;[A 5 7] = 11+5+5 (23 points)
;Ele deverá escolher a melhor mão para o usuário, portanto
; A = 11, porém, se passar de 21, ele vale A = 1
(defn JQK->10 [card]
  (if (> card 10) 10 card))

(defn A->11 [card]
  (if (= card 1) 11 card))
(defn points-cards [cards]
  (let [cards-without-JQK (map JQK->10 cards)
        cards-with-A11 (map A->11 cards-without-JQK)
        points-with-A-1 (reduce + cards-without-JQK)
        points-with-A-11 (reduce + cards-with-A11)]
    (if (> points-with-A-11 21) points-with-A-1 points-with-A-11)))

;(points-cards [1 10])      21
;(points-cards [1 6 7])     14
;(points-cards [1 8 5])     14

; player's map
;
; (def player
;  {
;   :player "Marcos Brito"
;   :cards [4, 10]
;   })

(defn player [player-name]
  (let [card1 (new-card)
        card2 (new-card)
        cards [card1 card2]
        points (points-cards cards)]
    {
     :player-name player-name
     :cards       cards
     :points      points
     }))

; chamar a funcao new card para gerar a nova carta
; atualizar o vetor cards dentro do player com a nova carta
; calcular os pontos do jogador com o novo vetor de cartas
; retornar esse novo jogador

(defn more-card [player]
  (let [card (new-card)
        cards (conj (:cards player) card)
        new-player (update player :cards conj card)
        points (points-cards cards)
        ]
    (assoc new-player :points points)))

;(card/print-player (player "Marcos Brito"))
;(card/print-player (player "Dealer"))

(defn player-decision-continue? [player]
  (println (:player-name player) ": Mais carta?")
  (= (read-line) "sim"))

(defn dealer-decision-continue? [player-points dealer]
  (let [dealer-points (:points dealer)]
    (if (> player-points 21) false (< dealer-points player-points))))
;funcao game -> irá perguntar se o jogador quer mais cartas!
;caso sim, chama funcao more-card.
; logo é uma funcao recurssiva

(defn game [player fn-decision-continue?]
  (if (fn-decision-continue? player)
    (let [player-with-more-cards (more-card player)]
      (card/print-player player-with-more-cards)
      (recur player-with-more-cards fn-decision-continue?)
      )
    player))

; se ambos passam de 21 -> ambos perdem
; pontos iguais, empata
; se player passou de 21, dealer ganha
; se dealer passou de 21, player ganha
; se player-points > dealer-points, player ganha
; se dealer-points > player-points, dealer ganha
(defn end-game [player dealer]
  (let [player-points (:points player)
        dealer-points (:points dealer)
        player-name (:player-name player)
        dealer-name (:player-name dealer)
        message (cond
                  (and (> player-points 21) (> dealer-points 21)) "Ambos perderam"
                  (= player-points dealer-points) "empatou"
                  (> player-points 21) (str dealer-name " ganhou")
                  (> dealer-points 21) (str player-name " ganhou")
                  (> player-points dealer-points) (str player-name " ganhou")
                  (> dealer-points player-points) (str dealer-name " ganhou"))]
    (card/print-player player)
    (card/print-player dealer)
    (print message)))

(def player-1 (player "Marcos Brito"))
(card/print-player player-1)

(def dealer (player "Dealer"))
(card/print-masked-player dealer)

(def player-after-game (game player-1 player-decision-continue?))

(def partial-dealer-decision-continue? (partial dealer-decision-continue? (:points player-after-game)))

(def dealer-after-game (game dealer partial-dealer-decision-continue?))

(end-game player-after-game dealer-after-game)
