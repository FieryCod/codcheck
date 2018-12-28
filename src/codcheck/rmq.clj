(ns codcheck.rmq
  (:require
   [langohr.core :as langohr]
   [langohr.channel :as langohr-chan]
   [codcheck.envs :refer [envs]]))

(def exchanges
  {:pr-code-check "pr_code_check_ex"
   :pr-code-checked "pr_code_checked_ex"})

(def queues
  {:pr-code-check "pr_code_check_q"
   :pr-code-checked "pr_code_checked_q"})

(def routing-keys
  {:pr-code-check ""
   :pr-code-checked ""})

(def conn (atom nil))

(def chan (atom nil))

(defn connect!
  []
  (when (nil? @conn)
    (reset! conn (langohr/connect {:host (:RMQ_HOST envs)
                                   :username (:RMQ_USER envs)
                                   :password (:RMQ_PASS envs)}))))


(defn open-chan!
  []
  (when (nil? @conn)
    (throw (Exception. "Connection for RabbitMQ does not exists. @Codcheck")))
  (when (nil? @chan)
    (reset! chan (langohr-chan/open @conn))))
