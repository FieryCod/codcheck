(ns codcheck.logging
  (:require
   [taoensso.timbre :as timbre]
   [taoensso.timbre.appenders.core :as timbre-appenders])
  (:import
   [java.text SimpleDateFormat]
   [java.util Date]))

(defn- remove-logging-ns-info
  [data]
  (if (= (:?ns-str data) "codcheck.logging")
    (-> data
       (assoc :?ns-str "")
       (assoc :?line ""))
    data))

(defn- log-to-file
  "Creates the name of .log file"
  [filename date]
  (let [formatter (SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss")]
    (str "logs/" filename "-" (.format formatter date) ".log")))

(defn setup-timbre
  "Setups the timbre logger"
  [filename]
  (timbre/merge-config!
   {:appenders {:spit (timbre-appenders/spit-appender {:fname (log-to-file filename (Date.))})}
    :middleware [remove-logging-ns-info]}))
