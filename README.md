# user-simulator

Experimentation with markov chains. Uses a chat log to generate a model per user/nickname in it.

[![Build Status](https://travis-ci.org/henrikolsson/user-simulator.svg)](https://travis-ci.org/henrikolsson/user-simulator)

## Usage

Full example:

```
$ wget -O /tmp/test.log http://www.raynes.me/logs/irc.freenode.net/clojure/2010-10-29.txt

$ lein repl
...
user=> (use 'user-simulator.core)
nil
user=> (def memory (learn
                    {:line-regex #"[^ ]+ ([^:]+): (.+)"}
                    (slurp "/tmp/test.log")))
#'user/memory
user=> (ask memory "rhickey")
"this case is there was neutered right before 1.2 release as with-blah does"
user=> (ask memory "rhickey")
"the name for binding is neither an int nor a ticket please"
```

See test/user_simulator/core_test.clj

## License

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
