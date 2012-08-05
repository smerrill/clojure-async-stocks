(ns async-stocks.core
  (:require [clj-http.client :as client])
  (:require [clojure.string :as string]))

(def symbols ["AAPL" "GOOG" "IBM" "MSFT"])
(def year 2008)

(defn stock-price-url [symbol year]
  "Return the URL to grab CSV data about a stock's closing price."
  (format "http://ichart.finance.yahoo.com/table.csv?s=%s&a=11&b=01&c=%d&d=11&e=31&f=%d&g=m" symbol year year))

(defn get-stock-data [symbol year]
  "Given a stock symbol and a year, return a map with keys :symbol and :close (closing stock price) as a float."
  (let [{body :body} (client/get (stock-price-url symbol year))]
    {:symbol symbol :close (Float/parseFloat (get (string/split (last (string/split-lines body)) #",") 2))}))

(def url-requests
  (map #(future (get-stock-data % year)) symbols))

; Let all the futures return and print a result!
(let [{:keys [close symbol]} (last (sort-by :close (map deref url-requests)))]
  (println (format "The highest stock in %d was %s with closing price %f." year symbol close)))
