# AoC 2021 in Clojure

Disclaimer: first time using Clojure or anything ~like it.

## Quickstart

Run all solutions:

```console
clj -M -m run
```

Benchmark all solutions (this is slow!):

```console
clj -M -m run --bench
```

To run a specific day, use `-d <n>`.
To run a range of days, use `-d <n>-<m>`.
To run a specific part, use `-p <n>`.

For example, to benchmark part one for days three, four and five,

```console
clj -M -m run -d 3-5 -p 1 --bench
```

## Structure

All solutions are in `src/aoc/dXY.clj`.
Each day is in the `aoc.dXY` namespace.
Each day has a `parse` function accepting a filename, and `part-1` and `part-2` functions accepting the output of `parse` and returning that part's answer.

All (my) puzzle inputs are in `inputs/dXY/input`.
