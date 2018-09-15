(ns codcheck.github)

(def api-url "https://api.github.com/")

(defn installation-token-url
  [installation-id]
  (str api-url "app/installations/" installation-id "/access_tokens"))
