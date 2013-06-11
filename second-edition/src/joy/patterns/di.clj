(ns joy.patterns.di
  (:require [joy.patterns.abstract-factory :as factory]))

(def lofi {:type :sim,
           :descr "Low-fidelity sim",
           :fidelity :low})

(def hifi {:type :sim,
           :descr "High-fidelity sim",
           :fidelity :high,
           :threads 2})

(comment
  
  (factory/construct :lofi lofi)
  
  ;;=> #joy.patterns.abstract_factory.LowFiSim{:name :lofi, :descr "Low-fidelity sim"}

)

(defprotocol Sys
  (start! [sys])
  (stop!  [sys]))

(defprotocol Sim
  (handle [sim msg]))


(comment

  (extend-type joy.patterns.abstract_factory.LowFiSim
    Sys
    (start! [this] (println "Started a lofi simulator."))
    (stop!  [this] (println "Stopped a lofi simulator."))

    Sim
    (handle [this msg] (* (:weight msg) 3.14)))

  (start! (factory/construct :lofi lofi))
  ;; Started a lofi simulator.

  (defn build-system [name config]
    (let [sys (factory/construct name config)]
      (start! sys)
      sys))

  (build-system :sim1 lofi)
  ;; Started a lofi simulator.
  ;;=> #joy.patterns.abstract_factory.LowFiSim{:name :sim1, :descr "Low-fidelity sim"}  
  
  (extend-type joy.patterns.abstract_factory.HiFiSim
    Sys
    (start! [this] (println "Started a hifi simulator."))
    (stop!  [this] (println "Stopped a hifi simulator."))

    Sim
    (handle [this msg]
      (Thread/sleep 2000)
      (* (:weight msg) 3.1415926535897932384626M)))

  (build-system :sim2 hifi)
  ;; Started a lofi simulator.
  ;;=> #joy.patterns.abstract_factory.HiFiSim{:name :sim2, :threads 2, :descr "High-fidelity sim"}
  
  
)




