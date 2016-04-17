# user-simulator

Experimentation with markov chains. Uses a chat log to generate a model per user/nickname in it.

[![Build Status](https://travis-ci.org/henrikolsson/user-simulator.svg)](https://travis-ci.org/henrikolsson/user-simulator)

## Usage

Example:
```clojure
(def memory (learn
             {:line-regex #"[^ ]+ ([^:]+): (.+)"}
             (slurp "/tmp/test.log")))

(ask memory "nickname")
```

See test/user_simulator/core_test.clj

## License

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
