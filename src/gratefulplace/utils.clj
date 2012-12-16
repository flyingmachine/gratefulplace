(ns gratefulplace.utils)

(defn str->int
  ([str]
     (Integer.  str))

  ([m & keys]
     (reduce
      (fn [m k]
        (if-let [val (k m)]
          (assoc m k (str->int val))
          m))
      m keys)))

(defn deserialize
  [m & ks]
  (reduce #(assoc % %2 (read-string (%2 %))) m ks))

(defmacro self-unless-fn
  [self fn otherwise]
  `(let [self# ~self]
     (if (~fn self#)
       ~otherwise
       self#)))

;; TODO I bet there's something in OnLisp about doing this without
;; iterating over key/vals without needing to
;;
;; maybe find the intersection of keys, then merge
(defn transform-when-key-exists
  "(transform-when-key-exists
 {:a 1
  :b 2}
 {:a #(inc %)
  :c #(inc %)})
=> {:a 2 :b 2}"
  [source transformations]
  (reduce
   (fn [m x]
     (merge m
            (let [[key value] x]
              (if-let [transform (get transformations key)]
                {key (transform value)}
                x))))
   {}
   source))