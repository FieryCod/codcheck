(ns codcheck.logging
  (:require
   [taoensso.timbre :as timbre]
   [taoensso.timbre.appenders.core :as timbre-appenders]
   [clojure.string :as string])
  (:import
   [java.text SimpleDateFormat]
   [java.util.regex Pattern Matcher]
   [java.util Date]))

(def banned-words
  ["signed-token" "token" "password"])

(defn- replace-banned-word
  [regexp astring]
  (string/replace astring regexp "$1 \"****\""))

(defn- replace-all-banned-words
  [regexps astring]
  (if-not (seq regexps)
    astring
    (recur (rest regexps)
           (replace-banned-word (first regexps) astring))))

(defn- remove-logging-ns-info
  [init-ns]
  (fn [data]
   (let [ns-str (:?ns-str data)]
     (if (or (= ns-str "codcheck.logging")
             (= ns-str (str init-ns ".logging"))
             (= ns-str (str init-ns ".logging.middleware")))
       (-> data
          (assoc :?ns-str "")
          (assoc :?line ""))
       data))))

(defn- remove-sensitive-info
  [data]
  (let [vargs (get data :vargs "")
        start-part "(?:("
        match-part "\"([a-zA-Z0-9_\\-.]+)\")"
        all-regex-canditates (map #(str start-part % ") " match-part) banned-words)
        regexps (map #(Pattern/compile %1) all-regex-canditates)]
    (assoc data :vargs (map #(replace-all-banned-words regexps %) vargs))))

(defn- log-to-file
  "Creates the name of .log file"
  [filename date]
  (let [formatter (SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss")]
    (str "logs/" filename "-" (.format formatter date) ".log")))

(defn setup-logging
  "Setups the timbre logger"
  [filename]
  (timbre/merge-config!
   {:appenders {:spit (timbre-appenders/spit-appender {:fname (log-to-file filename (Date.))})}
    :middleware [(remove-logging-ns-info filename) remove-sensitive-info]}))
