(ns sugbi.catalog.db
 (:require
  [conman.core :as conman]
  [sugbi.db.core :as db]))

(conman/bind-connection db/*db* "sql/catalog.sql")


(defn matching-books
  [title]
  (search {:title (str "%" title "%")}))
