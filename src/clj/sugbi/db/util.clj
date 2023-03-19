(ns sugbi.db.utils)

(defn aggregate-field
  [rows field-name aggregate-field-name]
  (let [aggregated-values (map field-name rows)
        first-row         (first rows)]
    (-> first-row
        (assoc aggregate-field-name aggregated-values)
        (dissoc field-name))))
