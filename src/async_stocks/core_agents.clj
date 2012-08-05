(ns async-stocks.core-agents
  (:require [clj-http.client :as client])
  (:require [clojure.string :as string]))

(def symbols ["AAPL" "GOOG" "IBM" "MSFT"])
(def year 2008)
(def highest-price (agent {}))

(defn stock-price-url [symbol year]
  "Return the URL to grab CSV data about a stock's closing price."
  (format "http://ichart.finance.yahoo.com/table.csv?s=%s&a=11&b=01&c=%d&d=11&e=31&f=%d&g=m" symbol year year))

(defn get-stock-data [symbol year]
  "Given a stock symbol and a year, return a map with keys :symbol and :close (closing stock price) as a float."
  (let [{body :body} (client/get (stock-price-url symbol year))]
    {:symbol symbol :close (Float/parseFloat (get (string/split (last (string/split-lines body)) #",") 2))}))

(defn compare-price [current-high]
  (println current-high))

;(defn compare-price [current-high symbol year]
  ;(println "1: " symbol year)
  ;"Send a request for the high price of symbol in year. Then increment the current high."
  ;(let [{:keys [close symbol]} (get-stock-data symbol year)]
    ;(println "2: " symbol year)
    ;(if (empty? current-high)
      ;{:symbol symbol :close close}
      ;current-high)))

;(send highest-price compare-price)

;(await-for 500 highest-price)
;(println @highest-price)

(def a (agent 1))
(send a inc)
(await a)
@a

; For the REPL's benefit.
(shutdown-agents)
