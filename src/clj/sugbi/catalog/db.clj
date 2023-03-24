(ns sugbi.catalog.db
 (:require
  [conman.core :as conman]
  [sugbi.db.core :as db]
  [sugbi.db.util :as db.util]))

(conman/bind-connection db/*db* "sql/catalog.sql")

(defn search-book
  [title]
  (let [result (search {:title     (str "%" title "%")
                        :full-name true})]
    (db.util/aggregate-field result :full_name :authors-full-name)))

(defn get-books
  []
  (->> (get-books {})
       (group-by :title)
       vals
       (map #(db.util/aggregate-field % :full_name :authors-full-name))))
