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

;(defn compare-price [a b]
  ;(compare (get a :close) (get b :close)))

; Send off the actions to the actor, then wait up to 10 seconds for them to return.
(apply max-key :close (pmap #(get-stock-data % year) symbols))
;(-> (send-off highest-price )
;(map #(send-off highest-price compare-price % year) symbols)
;(await-for 10000 highest-price)

;(let [{:keys [close symbol]} @highest-price]
  ;(println (format "The highest stock in %d was %s with closing price %f." year symbol close)))
