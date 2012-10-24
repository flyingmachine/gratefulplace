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