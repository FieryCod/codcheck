(ns codcheck.envs
  (:require [clojure.walk])
  (:import [java.util HashMap]))

(defn- read-envs-from-edn
  []
  (let [current-env (or (System/getenv "ENVIRONMENT") "development")
        edn-filename (str ".env." current-env ".edn")]
    (try
      (read-string (slurp edn-filename))
      (catch Exception err
        (println (str "There is no " edn-filename "!!"))))))

(def envs
  (let [system-envs (clojure.walk/keywordize-keys (into {} (HashMap. (System/getenv))))
        edn-envs (or (read-envs-from-edn) {})]

    (merge edn-envs system-envs)))
