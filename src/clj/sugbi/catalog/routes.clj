(ns sugbi.catalog.routes
  (:require
   [spec-tools.data-spec :as ds]
   [sugbi.catalog.handlers :as catalog.handlers]))


(def routes
  ["/catalog" {:swagger {:tags ["Catalog"]}}
   ["/books"
    ["" {:get  {:summary    "gets the catalog. Optionally, accepts a search criteria"
                :parameters {:query {(ds/opt :q) string?}}
                :responses  {200 {:body some?}}
                :handler    catalog.handlers/search-books}
         :post {:summary    "add a book title to the catalog"
                :parameters {:body {:isbn  string?
                                    :title string?}}
                :responses  {200 {:body some?}}
                :handler    catalog.handlers/insert-book!}}]
    ["/:isbn" {:get    {:summary    "get a book info by its isbn"
                        :parameters {:path {:isbn string?}}
                        :responses  {200 {:body some?}}
                        :handler    catalog.handlers/get-book}
               :delete {:summary    "delete a book title of the catalog"
                        :parameters {:path {:isbn string?}}
                        :responses  {200 {:body some?}}
                        :handler    catalog.handlers/delete-book!}}]]])
