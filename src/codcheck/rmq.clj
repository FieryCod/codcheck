(ns codcheck.rmq
  (:require
   [langohr.core :as langohr]
   [langohr.channel :as langohr-chan]
   [codcheck.envs :refer [envs]]))

(def exchanges
  {:gh-pr-code-check "gh_pr_code_check_ex"})

(def queues
  {:gh-pr-code-check "gh_pr_code_check_q"})

(def routing-keys
  {:gh-pr-code-check ""})

(def conn (atom nil))

(def chan (atom nil))

(defn connect!
  []
  (let [{:keys [rmq-user rmq-pass rmq-host]} envs]
    (when (nil? @conn)
      (reset! conn (langohr/connect {:host rmq-host
                                     :username rmq-user
                                     :password rmq-pass})))))

(defn open-chan!
  []
  (when (nil? @conn)
    (throw (Exception. "Connection does not exists")))
  (when (nil? @chan)
    (reset! chan (langohr-chan/open @conn))))
