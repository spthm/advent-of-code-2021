# AoC 2021 in Clojure

Disclaimer: first time using Clojure or anything ~like it.

## Quickstart

Run all solutions:

```console
clj -M -m run
```

or a specific solution, e.g. day 4,

```console
clj -M -m run -d 4
```

or a subset of solutions, e.g. days 2 through 5,

```console
clj -M -m run -d 2-5
```

## Structure

All solutions are in `src/aoc/dXY.clj`.
Each day is in the `aoc.dXY` namespace.
Each day has a `parse` function accepting a filename, and `part-1` and `part-2` functions accepting the output of `parse` and returning that part's answer.

All (my) puzzle inputs are in `inputs/dXY/input`.
