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

(def conn
  (atom
   (let [{:keys [rmq-user rmq-pass rmq-host]} envs]
     (langohr/connect {:host rmq-host
                       :username rmq-user
                       :password rmq-pass}))))

(def chan
  (atom (langohr-chan/open @conn)))
