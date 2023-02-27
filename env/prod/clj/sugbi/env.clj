(ns sugbi.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[sugbi started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[sugbi has shut down successfully]=-"))
   :middleware identity})
