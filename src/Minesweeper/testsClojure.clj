(ns Minesweeper.testsClojure
(:require [clojure.core.async :as async :refer [<! >!]]
  [clojure.string :as str]))
;AQUI ESTÃO CONCENTRADOS TODOS OS TESTES PARA CRIAÇÃO DO JOGO!

;sempre que formos fazer uma função -> defn == def function
; (defn nome-da-funcao
;   "documentacao opcional"
;   [parametro1, parametro2]
;   (corpo da funcao))

(defn soma
  "uma função que somará três entradas"
  [number1, number2, number3]
  (+ number1 number2 number3))
(println (soma 10 40 50))

;funcao que cria o tabuleiro genérico do jogo
(defn cria-tabuleiro
  [linhas colunas]
  (vec (repeat linhas (vec (repeat colunas "x")))))
;(println (cria-tabuleiro 4 5))

;função que irá exibir no terminal o tabuleiro do jogo
; let -> define "variáveis" locais no código
