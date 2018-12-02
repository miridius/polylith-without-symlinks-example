(defn common-defaults [name]
  {:name        name
   :target-path (str "target/" name)})

(defn source-defaults [name]
  {:source-paths   [(str "src/" name)]
   :resource-paths [(str "resources/" name)]
   :test-paths     [(str "test/" name)]})

(defn system-defaults [name {:keys [version] :or {version "0.0"}}]
  (let [uberjar-name (str name "-" version "-standalone.jar")]
    {:aot           :all
     :uberjar-name  uberjar-name
     :clean-targets [(str "target/" name)
                     (str "target/uberjar/" uberjar-name)]}))

(defn merge-defaults [type name profile]
  (merge
    (common-defaults name)
    (case type
      ("bases" "components") (source-defaults name)
      "systems"              (system-defaults name profile)
      {})
    profile))

(defn update-profile [type name profile]
  (cond
    (vector? profile) (mapv (partial update-profile type name) profile)
    (map? profile)    (merge-defaults type name profile)
    :else profile))

(defn add-defaults [profiles]
  (reduce-kv
    (fn [acc kw profile]
      (let [type (namespace kw)
            name (name kw)]
        (assoc acc kw (update-profile type name profile))))
    {}
    profiles))

(defproject com.example/no-symlinks "1.0"
  :description "An example of how a Polylith workspace might look without symlinks."

  ;; If you share any dependencies for all bases/components/environments/systems,
  ;; you can put them here for convenience
  :dependencies [[org.clojure/clojure "1.9.0"]]

  ;; By default, we should not include any paths
  :source-paths []
  :resource-paths []
  :test-paths []

  ;; This will make each profile use its own target subfolder
  :target-path "target/%s"

  ;; We use profiles instead of individual project.clj files
  :profiles
  ~(add-defaults
     '{
       ;; ============
       ;; Environments
       ;; ============

       :dev
       [:bases/mybase
        :components/mycomponent
        {:description  "The main development environment."
         :global-vars  {*warn-on-reflection* true}
         :plugins      [[com.jakemccrary/lein-test-refresh "0.23.0"]]
         :test-refresh {:changes-only true}}]

       ;; ==========
       ;; Interfaces
       ;; ==========

       ;; Use this profile to build and install the interface jar:
       ;; `lein with-profile interfaces install`
       ;; Or use it directly when compiling a base/component:
       ;; `lein with-profile interfaces,bases/mybase compile`
       :interfaces
       {:version      "1.0"
        :description  "Component interfaces"
        :source-paths ["src/interfaces"]
        :aot          :all}

       ;; Use this profile to use the installed interface jar when compiling:
       ;; `lein with-profile interfaces-jar,bases/mybase compile`
       :interfaces-jar
       {:dependencies [[com.example/interfaces "1.0"]]
        :aot          :all}

       ;; =====
       ;; Bases
       ;; =====

       :bases/mybase
       {:description  "A mybase base."
        :dependencies [[com.taoensso/timbre "4.10.0"]]}

       ;; ==========
       ;; Components
       ;; ==========

       :components/mycomponent
       {:description "A mycomponent component."}

       ;; =======
       ;; Systems
       ;; =======

       ;; Run with: `lein with-profile systems/mysystem run`
       ;; Build with: `lein with-profile systems/mysystem uberjar`
       ;; Test with: `lein with-profile systems/mysystem test`
       :systems/mysystem
       [:bases/mybase
        :components/mycomponent
        {:version     "0.1"
         :description "A mysystem system."
         :main        com.example.mybase.core}]}))
