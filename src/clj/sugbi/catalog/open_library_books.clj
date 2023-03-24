(ns sugbi.catalog.open-library-books
 (:require
  [clj-http.client :as client]))


(def open-library-url
  "https://openlibrary.org")


(defn book-by-isbn-url
  [isbn]
  (str open-library-url "/isbn/" isbn ".json"))


(defn raw-book-info
  [isbn]
  (let [book-url (book-by-isbn-url isbn)]
    (-> book-url
        (client/get {:as :json})
        :body)))


(defn book-info
  [isbn requested-fields]
  (let [relevant-fields #{:title               :full_title
                          :subtitle            :publishers
                          :publish_date        :weight
                          :physical_dimensions :genre
                          :subjects            :number_of_pages}
        raw-info        (raw-book-info isbn)]
    (-> raw-info
        (select-keys relevant-fields)
        (select-keys requested-fields)
        (assoc :isbn isbn))))


(defn multiple-book-info
  [isbns requested-fields]
  (doall
   (map
    #(book-info % requested-fields)
    isbns)))
