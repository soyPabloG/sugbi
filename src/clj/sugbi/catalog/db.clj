(ns sugbi.catalog.db
 (:require
  [conman.core :as conman]
  [sugbi.db.core :as db]
  [sugbi.db.utils :as db.utils]))

(conman/bind-connection db/*db* "sql/catalog.sql")

(defn search-book
  [title]
  (let [result (search {:title     title
                        :full-name true})]
    (db.utils/aggregate-field result :full_name :authors-full-name)))

(defn get-books
  []
  (->> (get-books {})
       (group-by :title)
       vals
       (map #(db.utils/aggregate-field % :full_name :authors-full-name))))
