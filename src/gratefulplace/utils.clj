(ns gratefulplace.utils)

(defn str->int
  ([str]
     (Integer.  str))

  ([x & keys]
     (reduce
      (fn [x k]
        (if-let [val (k x)]
          (assoc x k (str->int val))
          x))
      x keys)))
