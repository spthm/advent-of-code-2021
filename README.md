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

## Benchmarks

| Day | Part 1 | Part 2 |
|:----|-------:|-------:|
| 01 | 1.9 ms | 4.1 ms  |
| 02 | 710 µs | 690 µs  |
| 03 | 3.9 ms | 2.0 ms  |
| 04 | 8.3 ms |  20 ms  |
| 05 | 140 ms | 260 ms  |
| 06 | 170 µs | 240 µs  |
| 07 | 4.6 ms | 8.4 ms  |
| 08 | 2.1 ms | 8.9 ms  |
| 09 |  11 ms |  29 ms  |
| 10 | 1.1 ms | 1.2 ms  |
| 11 | 4.9 ms |  19 ms  |
| 12 | 140 µs | 150 µs  |
| 13 | 6.4 ms |  27 ms  |
| 14 | 1.2 ms | 4.5 ms  |
| 15 |  75 ms | 1.8 sec |
| 16 |  50 ms |  51 ms  |
| 17 |  44 µs | 1.5 ms  |

## Structure

All solutions are in `src/aoc/dXY.clj`.
Each day is in the `aoc.dXY` namespace.
Each day has a `parse` function accepting a filename, and `part-1` and `part-2` functions accepting the output of `parse` and returning that part's answer.

All (my) puzzle inputs are in `inputs/dXY/input`.
