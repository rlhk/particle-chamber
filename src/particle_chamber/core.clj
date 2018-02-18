(ns particle-chamber.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def test-cases
  [
   [2  "R..L"]
   [2  "R..R..L"]
   [2  "LRLR.LRLR"]
   [10 "RLRLRLRLRL"]
   [1  "..."]
   [1  "LRRL.LR.LRR.R.LRRL."]])

(defn read-init-state [test-case]
  ; usage: (initial-particle-states [2 "R..L"])
  ; sample output: ([0 "R" 4 2] [1 "." 4 2] [2 "." 4 2] [3 "L" 4 2])
  "
  sample input: [<step> <state-string>]
  output: ([<position> <char> <slot-count> <step>] ...)
  "
  (let [
        [step input] test-case
        slots (vec input)
        slot-count (count slots)]
    (->> slots
         (map-indexed (fn [idx char] [idx char slot-count step])))))

(defn roll [state]
  ; usage: (roll [0 \R 4 2])
  (let [[pos dir slot-count step] state]
    (when (and (< pos slot-count) (>= pos 0) (not= dir \.))
      (case dir
            \R (conj (roll [(+ pos step) dir slot-count step]) state)
            \L (conj (roll [(- pos step) dir slot-count step]) state)))))

; (defn result []
;   (map #(roll %) (read-init-state (nth test-cases 1))))
  ; '(([0 "R" 7 2] [2 "R" 7 2] [4 "R" 7 2] [6 "R" 7 2])
  ;   ([3 "R" 7 2] [5 "R" 7 2])
  ;   ([6 "L" 7 2] [4 "L" 7 2] [2 "L" 7 2] [0 "L" 7 2])))

(defn draw-particle [[pos _ size]]
  ; [0 "R" 7 2] -> ["x" "." "." "." "." "." "."]
  (assoc (vec (repeat size nil)) pos \x))

(defn max-row-length [result]
  "max row length of an irregular matrix"
  (apply max (map count result)))

; built-in transpose only good for rectangular matrix
; transpose irrigular/ragged matrix
; use case: group particle states by time
(defn transpose-irregular [result]
  (for [i (range (max-row-length result))]
    (map #(get (vec %) i) result)))

; draw all particles in the chamber at a point in time
(defn draw-chamber [result-instant]
  (map #(some identity %) (transpose-irregular (map draw-particle result-instant))))

; clean up transposed matrix. e.g.
; (([0 "R" 7 2] [3 "R" 7 2] [6 "L" 7 2])
;  ([2 "R" 7 2] [5 "R" 7 2] [4 "L" 7 2])
;  ([4 "R" 7 2] nil [2 "L" 7 2])
;  ([6 "R" 7 2] nil [0 "L" 7 2]))
; ==>
; (([0 "R" 7 2] [3 "R" 7 2] [6 "L" 7 2])
;  ([2 "R" 7 2] [5 "R" 7 2] [4 "L" 7 2])
;  ([4 "R" 7 2] [2 "L" 7 2])
;  ([6 "R" 7 2] [0 "L" 7 2]))
(defn remove-nil-in-row [result]
  (for [g result]
     (filter #(not= nil %) g)))

(defn run [test-case]
  (let [
        slot-count (count (vec (second test-case)))
        final-state (apply str (repeat slot-count \.))]
    (concat
      (->> test-case
           (read-init-state) ; parse test case to data model
           (map #(roll %)) ; simulate all particle activities till falling out chamber
           (transpose-irregular) ; group history by time
           (remove-nil-in-row) ; clean up before rendering
           (map #(draw-chamber %))
           (map (fn [state] (map #(if (nil? %) \. %) state))) ; replace nil with "." in data
           (map #(apply str %))) ; convert data arrays to strings
      [final-state])))

; run all test cases
(map #(run %) test-cases)
