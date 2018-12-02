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

  {;; ============
   ;; Environments
   ;; ============

   ;; We could instead write `:environments/development`, but calling it
   ;; `:dev` means lein turns it on by default (and so does Cursive)

   :dev
   [;; Specifying a vector of profiles causes them to be merged with this one
    :bases/mybase
    :components/mycomponent
    ;; And of course we can still include our own settings as a map literal
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
   {:name         "interfaces"
    :version      "1.0"
    :description  "Component interfaces"
    :source-paths ["src/interfaces"]
    :aot          :all}

   ;; =============================
   ;; Using pre-compiled interfaces
   ;; =============================

   ;; Use this profile to use the installed interface jar when compiling:
   ;; `lein with-profile interfaces-jar,bases/mybase compile`
   ;; This way you only need to compile the interfaces once.
   :interfaces-jar
   {:dependencies [[com.example/interfaces "1.0"]]
    :aot          :all}

   ;; =====
   ;; Bases
   ;; =====

   :bases/mybase
   {:description    "A mybase base."
    ;; deps specified in a base/component are automatically merged into any
    ;; environment or system that uses it.
    :dependencies   [[com.taoensso/timbre "4.10.0"]]
    :source-paths   ["src/mybase"]
    :resource-paths ["resources/mybase"]
    :test-paths     ["test/mybase"]}

   ;; ==========
   ;; Components
   ;; ==========

   :components/mycomponent
   {:description    "A mycomponent component."
    :source-paths   ["src/mycomponent"]
    :resource-paths ["resources/mycomponent"]
    :test-paths     ["test/mycomponent"]}

   ;; =======
   ;; Systems
   ;; =======

   ;; Run with: `lein with-profile systems/mysystem run`
   ;; Build with: `lein with-profile systems/mysystem uberjar`
   :systems/mysystem
   [;; Specifying a vector of profiles causes them to be merged
    :bases/mybase
    :components/mycomponent
    {:name         "mysystem"
     :version      "0.1"
     :description  "A mysystem system."
     :aot          :all
     :main         com.example.mybase.core
     ;; The below settings are necessary for the uberjar task only.
     ;; If we don't set the uberjar name it will use the project name
     :uberjar-name "mysystem-0.1-standalone.jar"
     ;; If we don't specify the clean paths it will clean everything
     :target-path "target/mysystem"
     :clean-targets ["target/mysystem"
                     "target/uberjar/mysystem-0.1-standalone.jar"]}]})
