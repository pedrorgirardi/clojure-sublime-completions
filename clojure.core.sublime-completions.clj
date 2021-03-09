(require '[clojure.data.json :as json])
(require '[clojure.string :as str])

(defn ns-symbols-meta [ns]
  (map #(select-keys (meta (eval `(var ~%))) [:name :arglists :doc]) (clojure.repl/dir-fn ns)))



;; Sublime Text Completions
;; https://www.sublimetext.com/docs/completions.html#ver-dev

(defn completions [dir]
  (let [completion (fn [{:keys [name doc arglists]}]
                     (merge {:trigger name
                             :contents name
                             :kind (if (seq arglists) "function" "variable")}
                            (when (seq arglists)
                              {:details (str "<code>" (str/join " " arglists) "</code>")})))]
    (map completion dir)))


;; -- clojure.core

(def clojure-core-completions
  (completions (ns-symbols-meta 'clojure.core)))

(def clojure-core-completions-path (str (System/getProperty "user.home") "/Desktop/clojure.core.sublime-completions"))

(spit clojure-core-completions-path (json/write-str {:scope "source.clojure" :completions clojure-core-completions}))
