# Tuning Options

| Parameter                    | Value                        |
|------------------------------|------------------------------|
| adjustK                      | true                         |
| delta                        | 1.0E-8                       |
| evalParamSet                 | CURRENT                      |
| geneticParams                | null                         |
| inputFiles                   | [C:\projekte\cygwin_home\mla\jackyChessDockerTesting\tuningdata\lichess-big3-resolved.book]|
| multiThreading               | true                         |
| name                         | lichess test2                |
| optimizeRecalcOnlyDependendFens| false                        |
| outputdir                    | null                         |
| progressUpdatesInMinutes     | 0                            |
| removeDuplicateFens          | true                         |
| resetParametersBeforeTuning  | true                         |
| shuffleTuningParameter       | false                        |
| stepGranularity              | [10, 5, 3, 1]                |
| threadCount                  | 6                            |
| tuneAdjustments              | true                         |
| tuneAdjustmentsFactors       | false                        |
| tuneComplexity               | true                         |
| tuneKingAttack               | true                         |
| tuneKingSafety               | true                         |
| tuneMaterial                 | false                        |
| tuneMobility                 | true                         |
| tuneMobilityTropism          | true                         |
| tunePassedPawnEval           | true                         |
| tunePawnEval                 | true                         |
| tunePositional               | true                         |
| tunePst                      | true                         |
| tuneThreats                  | true                         |


# Dataset Information

|                |         |
|----------------|---------|
| Num Fens       | 6819087 |
| MATE White     | 2801909 |
| MATE Black     | 2605257 |
| Draws          | 1411921 |
| Duplicate Fens | 0       |


## Number of fens by Game Phase

| Phase | Count  | %  |
|-------|--------|----|
| 0     | 593109 | 8  |
| 1     | 923895 | 13 |
| 2     | 736133 | 10 |
| 3     | 619828 | 9  |
| 4     | 561207 | 8  |
| 5     | 463708 | 6  |
| 6     | 437031 | 6  |
| 7     | 530586 | 7  |
| 8     | 654432 | 9  |
| 9     | 692383 | 10 |
| 10    | 606775 | 8  |


## Number of fens by having figures

| FigureType | Count   | %   |
|------------|---------|-----|
| Pawn       | 6819087 | 100 |
| Queen      | 3817373 | 55  |
| Rook       | 5988879 | 87  |
| Bishop     | 4966199 | 72  |
| Knight     | 4456890 | 65  |


## Number of fens by Material

| Material        | Count  | % |
|-----------------|--------|---|
|                 | 33454  | 0 |
| P               | 76606  | 1 |
| PP              | 122515 | 1 |
| PPP             | 132276 | 1 |
| PPPP            | 103981 | 1 |
| PPPPP           | 68109  | 0 |
| PPPPPP          | 32877  | 0 |
| PPPPPPP         | 8662   | 0 |
| PPPPPPPP        | 323    | 0 |
| N               | 12616  | 0 |
| NP              | 33814  | 0 |
| NPP             | 55129  | 0 |
| NPPP            | 68974  | 1 |
| NPPPP           | 64043  | 0 |
| NPPPPP          | 49168  | 0 |
| NPPPPPP         | 28688  | 0 |
| NPPPPPPP        | 9006   | 0 |
| NPPPPPPPP       | 209    | 0 |
| NN              | 530    | 0 |
| NNP             | 1841   | 0 |
| NNPP            | 3202   | 0 |
| NNPPP           | 5025   | 0 |
| NNPPPP          | 5922   | 0 |
| NNPPPPP         | 6029   | 0 |
| NNPPPPPP        | 4408   | 0 |
| NNPPPPPPP       | 2177   | 0 |
| NNPPPPPPPP      | 20     | 0 |
| NNN             | 3      | 0 |
| NNNP            | 4      | 0 |
| NNNPP           | 13     | 0 |
| NNNPPP          | 4      | 0 |
| NNNPPPP         | 3      | 0 |
| NNNPPPPP        | 1      | 0 |
| NNNNPPP         | 2      | 0 |
| B               | 16616  | 0 |
| BP              | 51273  | 0 |
| BPP             | 87515  | 1 |
| BPPP            | 103420 | 1 |
| BPPPP           | 100360 | 1 |
| BPPPPP          | 76023  | 1 |
| BPPPPPP         | 42486  | 0 |
| BPPPPPPP        | 11779  | 0 |
| BPPPPPPPP       | 401    | 0 |
| BN              | 2812   | 0 |
| BNP             | 9913   | 0 |
| BNPP            | 19555  | 0 |
| BNPPP           | 27490  | 0 |
| BNPPPP          | 32087  | 0 |
| BNPPPPP         | 29573  | 0 |
| BNPPPPPP        | 21544  | 0 |
| BNPPPPPPP       | 8004   | 0 |
| BNPPPPPPPP      | 155    | 0 |
| BNN             | 49     | 0 |
| BNNP            | 275    | 0 |
| BNNPP           | 873    | 0 |
| BNNPPP          | 1789   | 0 |
| BNNPPPP         | 2884   | 0 |
| BNNPPPPP        | 3467   | 0 |
| BNNPPPPPP       | 3189   | 0 |
| BNNPPPPPPP      | 1840   | 0 |
| BNNPPPPPPPP     | 14     | 0 |
| BNNNP           | 1      | 0 |
| BNNNPPPP        | 1      | 0 |
| BNNNNNP         | 1      | 0 |
| BNNNNNPP        | 1      | 0 |
| BB              | 1971   | 0 |
| BBP             | 4075   | 0 |
| BBPP            | 7041   | 0 |
| BBPPP           | 11143  | 0 |
| BBPPPP          | 11437  | 0 |
| BBPPPPP         | 11128  | 0 |
| BBPPPPPP        | 8216   | 0 |
| BBPPPPPPP       | 2873   | 0 |
| BBPPPPPPPP      | 131    | 0 |
| BBN             | 93     | 0 |
| BBNP            | 299    | 0 |
| BBNPP           | 1039   | 0 |
| BBNPPP          | 2299   | 0 |
| BBNPPPP         | 3735   | 0 |
| BBNPPPPP        | 4460   | 0 |
| BBNPPPPPP       | 4140   | 0 |
| BBNPPPPPPP      | 2009   | 0 |
| BBNPPPPPPPP     | 23     | 0 |
| BBNNP           | 20     | 0 |
| BBNNPP          | 79     | 0 |
| BBNNPPP         | 301    | 0 |
| BBNNPPPP        | 636    | 0 |
| BBNNPPPPP       | 738    | 0 |
| BBNNPPPPPP      | 880    | 0 |
| BBNNPPPPPPP     | 633    | 0 |
| BBNNPPPPPPPP    | 5      | 0 |
| BBNNNP          | 3      | 0 |
| BBBP            | 4      | 0 |
| BBBPP           | 4      | 0 |
| BBBPPP          | 3      | 0 |
| BBBPPPP         | 4      | 0 |
| BBBNP           | 1      | 0 |
| BBBNPPP         | 1      | 0 |
| BBBNNP          | 1      | 0 |
| BBBBP           | 3      | 0 |
| BBBBPPP         | 2      | 0 |
| BBBBPPPP        | 1      | 0 |
| BBBBNPP         | 1      | 0 |
| BBBBBPPP        | 1      | 0 |
| BBBBBBPP        | 1      | 0 |
| R               | 70970  | 1 |
| RP              | 192547 | 2 |
| RPP             | 252064 | 3 |
| RPPP            | 268348 | 3 |
| RPPPP           | 221962 | 3 |
| RPPPPP          | 158007 | 2 |
| RPPPPPP         | 84534  | 1 |
| RPPPPPPP        | 22633  | 0 |
| RPPPPPPPP       | 1060   | 0 |
| RN              | 14423  | 0 |
| RNP             | 32920  | 0 |
| RNPP            | 60144  | 0 |
| RNPPP           | 96059  | 1 |
| RNPPPP          | 107436 | 1 |
| RNPPPPP         | 104563 | 1 |
| RNPPPPPP        | 69062  | 1 |
| RNPPPPPPP       | 20732  | 0 |
| RNPPPPPPPP      | 1028   | 0 |
| RNN             | 203    | 0 |
| RNNP            | 1056   | 0 |
| RNNPP           | 3050   | 0 |
| RNNPPP          | 6878   | 0 |
| RNNPPPP         | 11631  | 0 |
| RNNPPPPP        | 15220  | 0 |
| RNNPPPPPP       | 12681  | 0 |
| RNNPPPPPPP      | 5002   | 0 |
| RNNPPPPPPPP     | 254    | 0 |
| RNNN            | 1      | 0 |
| RNNNP           | 4      | 0 |
| RNNNPP          | 1      | 0 |
| RNNNPPP         | 5      | 0 |
| RNNNPPPP        | 2      | 0 |
| RNNNPPPPP       | 1      | 0 |
| RNNNNP          | 1      | 0 |
| RNNNNPP         | 2      | 0 |
| RB              | 15708  | 0 |
| RBP             | 44306  | 0 |
| RBPP            | 82180  | 1 |
| RBPPP           | 125978 | 1 |
| RBPPPP          | 151618 | 2 |
| RBPPPPP         | 144476 | 2 |
| RBPPPPPP        | 94000  | 1 |
| RBPPPPPPP       | 27932  | 0 |
| RBPPPPPPPP      | 1485   | 0 |
| RBN             | 1368   | 0 |
| RBNP            | 5362   | 0 |
| RBNPP           | 15999  | 0 |
| RBNPPP          | 36004  | 0 |
| RBNPPPP         | 62303  | 0 |
| RBNPPPPP        | 79905  | 1 |
| RBNPPPPPP       | 64804  | 0 |
| RBNPPPPPPP      | 23491  | 0 |
| RBNPPPPPPPP     | 1280   | 0 |
| RBNN            | 34     | 0 |
| RBNNP           | 233    | 0 |
| RBNNPP          | 1117   | 0 |
| RBNNPPP         | 3910   | 0 |
| RBNNPPPP        | 8980   | 0 |
| RBNNPPPPP       | 14328  | 0 |
| RBNNPPPPPP      | 14249  | 0 |
| RBNNPPPPPPP     | 6298   | 0 |
| RBNNPPPPPPPP    | 410    | 0 |
| RBNNNP          | 6      | 0 |
| RBNNNPP         | 2      | 0 |
| RBNNNPPP        | 3      | 0 |
| RBNNNPPPP       | 2      | 0 |
| RBNNNPPPPP      | 1      | 0 |
| RBNNNNPP        | 1      | 0 |
| RBNNNNPPP       | 1      | 0 |
| RBB             | 763    | 0 |
| RBBP            | 1433   | 0 |
| RBBPP           | 4738   | 0 |
| RBBPPP          | 11059  | 0 |
| RBBPPPP         | 20282  | 0 |
| RBBPPPPP        | 26105  | 0 |
| RBBPPPPPP       | 21218  | 0 |
| RBBPPPPPPP      | 7519   | 0 |
| RBBPPPPPPPP     | 453    | 0 |
| RBBN            | 40     | 0 |
| RBBNP           | 304    | 0 |
| RBBNPP          | 1287   | 0 |
| RBBNPPP         | 4610   | 0 |
| RBBNPPPP        | 10988  | 0 |
| RBBNPPPPP       | 18234  | 0 |
| RBBNPPPPPP      | 17827  | 0 |
| RBBNPPPPPPP     | 7268   | 0 |
| RBBNPPPPPPPP    | 402    | 0 |
| RBBNN           | 2      | 0 |
| RBBNNP          | 39     | 0 |
| RBBNNPP         | 168    | 0 |
| RBBNNPPP        | 789    | 0 |
| RBBNNPPPP       | 2327   | 0 |
| RBBNNPPPPP      | 4686   | 0 |
| RBBNNPPPPPP     | 5215   | 0 |
| RBBNNPPPPPPP    | 2659   | 0 |
| RBBNNPPPPPPPP   | 150    | 0 |
| RBBNNNPPPP      | 1      | 0 |
| RBBNNNPPPPP     | 1      | 0 |
| RBBNNNPPPPPP    | 1      | 0 |
| RBBBP           | 1      | 0 |
| RBBBPP          | 1      | 0 |
| RBBBPPP         | 2      | 0 |
| RBBBNP          | 1      | 0 |
| RBBBNPP         | 2      | 0 |
| RBBBNPPP        | 1      | 0 |
| RBBBBP          | 2      | 0 |
| RBBBBPP         | 2      | 0 |
| RR              | 8637   | 0 |
| RRP             | 22399  | 0 |
| RRPP            | 43620  | 0 |
| RRPPP           | 71470  | 1 |
| RRPPPP          | 91824  | 1 |
| RRPPPPP         | 99571  | 1 |
| RRPPPPPP        | 78830  | 1 |
| RRPPPPPPP       | 35657  | 0 |
| RRPPPPPPPP      | 4122   | 0 |
| RRN             | 1368   | 0 |
| RRNP            | 4107   | 0 |
| RRNPP           | 11489  | 0 |
| RRNPPP          | 27864  | 0 |
| RRNPPPP         | 53679  | 0 |
| RRNPPPPP        | 83375  | 1 |
| RRNPPPPPP       | 88799  | 1 |
| RRNPPPPPPP      | 50063  | 0 |
| RRNPPPPPPPP     | 7135   | 0 |
| RRNN            | 35     | 0 |
| RRNNP           | 186    | 0 |
| RRNNPP          | 841    | 0 |
| RRNNPPP         | 3292   | 0 |
| RRNNPPPP        | 8797   | 0 |
| RRNNPPPPP       | 17939  | 0 |
| RRNNPPPPPP      | 24327  | 0 |
| RRNNPPPPPPP     | 17522  | 0 |
| RRNNPPPPPPPP    | 3029   | 0 |
| RRNNNP          | 1      | 0 |
| RRNNNPP         | 1      | 0 |
| RRNNNPPPP       | 2      | 0 |
| RRNNNPPPPPP     | 1      | 0 |
| RRNNNNPP        | 1      | 0 |
| RRB             | 1118   | 0 |
| RRBP            | 5361   | 0 |
| RRBPP           | 15376  | 0 |
| RRBPPP          | 38038  | 0 |
| RRBPPPP         | 74578  | 1 |
| RRBPPPPP        | 115257 | 1 |
| RRBPPPPPP       | 120731 | 1 |
| RRBPPPPPPP      | 65672  | 0 |
| RRBPPPPPPPP     | 8391   | 0 |
| RRBN            | 392    | 0 |
| RRBNP           | 1445   | 0 |
| RRBNPP          | 4662   | 0 |
| RRBNPPP         | 16718  | 0 |
| RRBNPPPP        | 46521  | 0 |
| RRBNPPPPP       | 96948  | 1 |
| RRBNPPPPPP      | 136001 | 1 |
| RRBNPPPPPPP     | 93586  | 1 |
| RRBNPPPPPPPP    | 14421  | 0 |
| RRBNN           | 6      | 0 |
| RRBNNP          | 78     | 0 |
| RRBNNPP         | 503    | 0 |
| RRBNNPPP        | 2559   | 0 |
| RRBNNPPPP       | 9221   | 0 |
| RRBNNPPPPP      | 24702  | 0 |
| RRBNNPPPPPP     | 44521  | 0 |
| RRBNNPPPPPPP    | 37629  | 0 |
| RRBNNPPPPPPPP   | 6990   | 0 |
| RRBNNNPP        | 1      | 0 |
| RRBNNNPPP       | 1      | 0 |
| RRBNNNPPPP      | 2      | 0 |
| RRBB            | 221    | 0 |
| RRBBP           | 336    | 0 |
| RRBBPP          | 1394   | 0 |
| RRBBPPP         | 5139   | 0 |
| RRBBPPPP        | 14214  | 0 |
| RRBBPPPPP       | 30328  | 0 |
| RRBBPPPPPP      | 42966  | 0 |
| RRBBPPPPPPP     | 27875  | 0 |
| RRBBPPPPPPPP    | 3618   | 0 |
| RRBBN           | 11     | 0 |
| RRBBNP          | 90     | 0 |
| RRBBNPP         | 672    | 0 |
| RRBBNPPP        | 3055   | 0 |
| RRBBNPPPP       | 11591  | 0 |
| RRBBNPPPPP      | 31919  | 0 |
| RRBBNPPPPPP     | 58179  | 0 |
| RRBBNPPPPPPP    | 45224  | 0 |
| RRBBNPPPPPPPP   | 6508   | 0 |
| RRBBNN          | 1      | 0 |
| RRBBNNP         | 9      | 0 |
| RRBBNNPP        | 117    | 0 |
| RRBBNNPPP       | 673    | 0 |
| RRBBNNPPPP      | 3139   | 0 |
| RRBBNNPPPPP     | 10137  | 0 |
| RRBBNNPPPPPP    | 21645  | 0 |
| RRBBNNPPPPPPP   | 19947  | 0 |
| RRBBNNPPPPPPPP  | 3797   | 0 |
| RRBBNNNPPPPPP   | 1      | 0 |
| RRBBBPPPPP      | 1      | 0 |
| RRBBBNPPPP      | 1      | 0 |
| RRR             | 3      | 0 |
| RRRP            | 5      | 0 |
| RRRPP           | 11     | 0 |
| RRRPPP          | 6      | 0 |
| RRRPPPP         | 5      | 0 |
| RRRPPPPP        | 6      | 0 |
| RRRPPPPPP       | 1      | 0 |
| RRRN            | 2      | 0 |
| RRRNP           | 2      | 0 |
| RRRNPP          | 4      | 0 |
| RRRNPPP         | 3      | 0 |
| RRRNPPPP        | 1      | 0 |
| RRRNPPPPP       | 2      | 0 |
| RRRNPPPPPP      | 1      | 0 |
| RRRBP           | 6      | 0 |
| RRRBPP          | 2      | 0 |
| RRRBPPP         | 4      | 0 |
| RRRBPPPP        | 5      | 0 |
| RRRBPPPPP       | 1      | 0 |
| RRRBNP          | 1      | 0 |
| RRRBNPPPP       | 1      | 0 |
| RRRBNPPPPP      | 1      | 0 |
| RRRBNPPPPPP     | 1      | 0 |
| RRRBNNPPPPPP    | 1      | 0 |
| RRRBBPPPPP      | 1      | 0 |
| RRRBBPPPPPP     | 1      | 0 |
| RRRBBNNPPPPPPP  | 1      | 0 |
| RRRRP           | 1      | 0 |
| RRRRPP          | 1      | 0 |
| Q               | 15540  | 0 |
| QP              | 51155  | 0 |
| QPP             | 68458  | 1 |
| QPPP            | 77494  | 1 |
| QPPPP           | 64778  | 0 |
| QPPPPP          | 44757  | 0 |
| QPPPPPP         | 23088  | 0 |
| QPPPPPPP        | 6359   | 0 |
| QPPPPPPPP       | 270    | 0 |
| QN              | 2746   | 0 |
| QNP             | 6954   | 0 |
| QNPP            | 13793  | 0 |
| QNPPP           | 20458  | 0 |
| QNPPPP          | 25540  | 0 |
| QNPPPPP         | 24736  | 0 |
| QNPPPPPP        | 18145  | 0 |
| QNPPPPPPP       | 6473   | 0 |
| QNPPPPPPPP      | 245    | 0 |
| QNN             | 61     | 0 |
| QNNP            | 296    | 0 |
| QNNPP           | 697    | 0 |
| QNNPPP          | 1484   | 0 |
| QNNPPPP         | 2682   | 0 |
| QNNPPPPP        | 3733   | 0 |
| QNNPPPPPP       | 3487   | 0 |
| QNNPPPPPPP      | 2003   | 0 |
| QNNPPPPPPPP     | 50     | 0 |
| QNNNP           | 4      | 0 |
| QNNNPP          | 5      | 0 |
| QNNNPPPP        | 1      | 0 |
| QNNNNP          | 1      | 0 |
| QB              | 4408   | 0 |
| QBP             | 11633  | 0 |
| QBPP            | 21929  | 0 |
| QBPPP           | 34278  | 0 |
| QBPPPP          | 40675  | 0 |
| QBPPPPP         | 36856  | 0 |
| QBPPPPPP        | 24905  | 0 |
| QBPPPPPPP       | 8455   | 0 |
| QBPPPPPPPP      | 396    | 0 |
| QBN             | 755    | 0 |
| QBNP            | 1421   | 0 |
| QBNPP           | 4293   | 0 |
| QBNPPP          | 8699   | 0 |
| QBNPPPP         | 14521  | 0 |
| QBNPPPPP        | 19293  | 0 |
| QBNPPPPPP       | 17503  | 0 |
| QBNPPPPPPP      | 7705   | 0 |
| QBNPPPPPPPP     | 208    | 0 |
| QBNN            | 13     | 0 |
| QBNNP           | 79     | 0 |
| QBNNPP          | 360    | 0 |
| QBNNPPP         | 899    | 0 |
| QBNNPPPP        | 1984   | 0 |
| QBNNPPPPP       | 3177   | 0 |
| QBNNPPPPPP      | 3491   | 0 |
| QBNNPPPPPPP     | 2358   | 0 |
| QBNNPPPPPPPP    | 43     | 0 |
| QBNNNP          | 1      | 0 |
| QBB             | 165    | 0 |
| QBBP            | 500    | 0 |
| QBBPP           | 1001   | 0 |
| QBBPPP          | 2524   | 0 |
| QBBPPPP         | 4818   | 0 |
| QBBPPPPP        | 6532   | 0 |
| QBBPPPPPP       | 6361   | 0 |
| QBBPPPPPPP      | 2767   | 0 |
| QBBPPPPPPPP     | 121    | 0 |
| QBBN            | 44     | 0 |
| QBBNP           | 90     | 0 |
| QBBNPP          | 301    | 0 |
| QBBNPPP         | 1125   | 0 |
| QBBNPPPP        | 2591   | 0 |
| QBBNPPPPP       | 4205   | 0 |
| QBBNPPPPPP      | 4813   | 0 |
| QBBNPPPPPPP     | 2669   | 0 |
| QBBNPPPPPPPP    | 38     | 0 |
| QBBNN           | 1      | 0 |
| QBBNNP          | 9      | 0 |
| QBBNNPP         | 63     | 0 |
| QBBNNPPP        | 224    | 0 |
| QBBNNPPPP       | 535    | 0 |
| QBBNNPPPPP      | 978    | 0 |
| QBBNNPPPPPP     | 1352   | 0 |
| QBBNNPPPPPPP    | 1097   | 0 |
| QBBNNPPPPPPPP   | 13     | 0 |
| QBBBPP          | 2      | 0 |
| QBBBNP          | 1      | 0 |
| QR              | 8135   | 0 |
| QRP             | 19397  | 0 |
| QRPP            | 33772  | 0 |
| QRPPP           | 56460  | 0 |
| QRPPPP          | 77376  | 1 |
| QRPPPPP         | 83151  | 1 |
| QRPPPPPP        | 60315  | 0 |
| QRPPPPPPP       | 20569  | 0 |
| QRPPPPPPPP      | 1368   | 0 |
| QRN             | 610    | 0 |
| QRNP            | 2750   | 0 |
| QRNPP           | 8114   | 0 |
| QRNPPP          | 22551  | 0 |
| QRNPPPP         | 45224  | 0 |
| QRNPPPPP        | 66331  | 0 |
| QRNPPPPPP       | 62510  | 0 |
| QRNPPPPPPP      | 25129  | 0 |
| QRNPPPPPPPP     | 1897   | 0 |
| QRNN            | 21     | 0 |
| QRNNP           | 151    | 0 |
| QRNNPP          | 787    | 0 |
| QRNNPPP         | 2934   | 0 |
| QRNNPPPP        | 7800   | 0 |
| QRNNPPPPP       | 14529  | 0 |
| QRNNPPPPPP      | 16596  | 0 |
| QRNNPPPPPPP     | 8776   | 0 |
| QRNNPPPPPPPP    | 848    | 0 |
| QRNNN           | 1      | 0 |
| QRNNNP          | 1      | 0 |
| QRNNNPPPPP      | 2      | 0 |
| QRB             | 1022   | 0 |
| QRBP            | 4388   | 0 |
| QRBPP           | 12945  | 0 |
| QRBPPP          | 34504  | 0 |
| QRBPPPP         | 69752  | 1 |
| QRBPPPPP        | 97024  | 1 |
| QRBPPPPPP       | 86071  | 1 |
| QRBPPPPPPP      | 33524  | 0 |
| QRBPPPPPPPP     | 2542   | 0 |
| QRBN            | 107    | 0 |
| QRBNP           | 951    | 0 |
| QRBNPP          | 4455   | 0 |
| QRBNPPP         | 16649  | 0 |
| QRBNPPPP        | 43838  | 0 |
| QRBNPPPPP       | 79109  | 1 |
| QRBNPPPPPP      | 88259  | 1 |
| QRBNPPPPPPP     | 42053  | 0 |
| QRBNPPPPPPPP    | 3639   | 0 |
| QRBNN           | 10     | 0 |
| QRBNNP          | 90     | 0 |
| QRBNNPP         | 631    | 0 |
| QRBNNPPP        | 2988   | 0 |
| QRBNNPPPP       | 9621   | 0 |
| QRBNNPPPPP      | 20764  | 0 |
| QRBNNPPPPPP     | 28260  | 0 |
| QRBNNPPPPPPP    | 17215  | 0 |
| QRBNNPPPPPPPP   | 1735   | 0 |
| QRBNNNPP        | 1      | 0 |
| QRBNNNPPP       | 2      | 0 |
| QRBNNNPPPP      | 1      | 0 |
| QRBNNNPPPPP     | 1      | 0 |
| QRBB            | 58     | 0 |
| QRBBP           | 361    | 0 |
| QRBBPP          | 1403   | 0 |
| QRBBPPP         | 5452   | 0 |
| QRBBPPPP        | 15278  | 0 |
| QRBBPPPPP       | 27067  | 0 |
| QRBBPPPPPP      | 30000  | 0 |
| QRBBPPPPPPP     | 13859  | 0 |
| QRBBPPPPPPPP    | 1160   | 0 |
| QRBBN           | 12     | 0 |
| QRBBNP          | 108    | 0 |
| QRBBNPP         | 715    | 0 |
| QRBBNPPP        | 3966   | 0 |
| QRBBNPPPP       | 12792  | 0 |
| QRBBNPPPPP      | 28466  | 0 |
| QRBBNPPPPPP     | 38009  | 0 |
| QRBBNPPPPPPP    | 20590  | 0 |
| QRBBNPPPPPPPP   | 1634   | 0 |
| QRBBNN          | 1      | 0 |
| QRBBNNP         | 17     | 0 |
| QRBBNNPP        | 167    | 0 |
| QRBBNNPPP       | 920    | 0 |
| QRBBNNPPPP      | 3767   | 0 |
| QRBBNNPPPPP     | 10300  | 0 |
| QRBBNNPPPPPP    | 16430  | 0 |
| QRBBNNPPPPPPP   | 11821  | 0 |
| QRBBNNPPPPPPPP  | 895    | 0 |
| QRBBNNNPP       | 1      | 0 |
| QRBBNNNPPPP     | 2      | 0 |
| QRBBBPPP        | 1      | 0 |
| QRBBBNPPP       | 1      | 0 |
| QRR             | 535    | 0 |
| QRRP            | 1915   | 0 |
| QRRPP           | 7284   | 0 |
| QRRPPP          | 22398  | 0 |
| QRRPPPP         | 49281  | 0 |
| QRRPPPPP        | 81831  | 1 |
| QRRPPPPPP       | 93493  | 1 |
| QRRPPPPPPP      | 58578  | 0 |
| QRRPPPPPPPP     | 9976   | 0 |
| QRRN            | 162    | 0 |
| QRRNP           | 581    | 0 |
| QRRNPP          | 3045   | 0 |
| QRRNPPP         | 13209  | 0 |
| QRRNPPPP        | 39899  | 0 |
| QRRNPPPPP       | 91339  | 1 |
| QRRNPPPPPP      | 140852 | 2 |
| QRRNPPPPPPP     | 115463 | 1 |
| QRRNPPPPPPPP    | 25549  | 0 |
| QRRNN           | 5      | 0 |
| QRRNNP          | 50     | 0 |
| QRRNNPP         | 376    | 0 |
| QRRNNPPP        | 2262   | 0 |
| QRRNNPPPP       | 9353   | 0 |
| QRRNNPPPPP      | 27216  | 0 |
| QRRNNPPPPPP     | 54708  | 0 |
| QRRNNPPPPPPP    | 62012  | 0 |
| QRRNNPPPPPPPP   | 19238  | 0 |
| QRRNNNPP        | 1      | 0 |
| QRRNNNNPPP      | 1      | 0 |
| QRRB            | 117    | 0 |
| QRRBP           | 861    | 0 |
| QRRBPP          | 4531   | 0 |
| QRRBPPP         | 19234  | 0 |
| QRRBPPPP        | 58562  | 0 |
| QRRBPPPPP       | 130154 | 1 |
| QRRBPPPPPP      | 192878 | 2 |
| QRRBPPPPPPP     | 151621 | 2 |
| QRRBPPPPPPPP    | 30336  | 0 |
| QRRBN           | 28     | 0 |
| QRRBNP          | 287    | 0 |
| QRRBNPP         | 2378   | 0 |
| QRRBNPPP        | 12972  | 0 |
| QRRBNPPPP       | 51618  | 0 |
| QRRBNPPPPP      | 152706 | 2 |
| QRRBNPPPPPP     | 308837 | 4 |
| QRRBNPPPPPPP    | 323797 | 4 |
| QRRBNPPPPPPPP   | 81017  | 1 |
| QRRBNN          | 2      | 0 |
| QRRBNNP         | 40     | 0 |
| QRRBNNPP        | 461    | 0 |
| QRRBNNPPP       | 2993   | 0 |
| QRRBNNPPPP      | 14792  | 0 |
| QRRBNNPPPPP     | 55554  | 0 |
| QRRBNNPPPPPP    | 145142 | 2 |
| QRRBNNPPPPPPP   | 205470 | 3 |
| QRRBNNPPPPPPPP  | 68644  | 1 |
| QRRBNNNNPPP     | 1      | 0 |
| QRRBB           | 10     | 0 |
| QRRBBP          | 91     | 0 |
| QRRBBPP         | 793    | 0 |
| QRRBBPPP        | 4389   | 0 |
| QRRBBPPPP       | 17515  | 0 |
| QRRBBPPPPP      | 51745  | 0 |
| QRRBBPPPPPP     | 102524 | 1 |
| QRRBBPPPPPPP    | 100268 | 1 |
| QRRBBPPPPPPPP   | 23359  | 0 |
| QRRBBN          | 1      | 0 |
| QRRBBNP         | 54     | 0 |
| QRRBBNPP        | 597    | 0 |
| QRRBBNPPP       | 3975   | 0 |
| QRRBBNPPPP      | 20278  | 0 |
| QRRBBNPPPPP     | 77645  | 1 |
| QRRBBNPPPPPP    | 207543 | 3 |
| QRRBBNPPPPPPP   | 263394 | 3 |
| QRRBBNPPPPPPPP  | 71456  | 1 |
| QRRBBNN         | 1      | 0 |
| QRRBBNNP        | 12     | 0 |
| QRRBBNNPP       | 146    | 0 |
| QRRBBNNPPP      | 1270   | 0 |
| QRRBBNNPPPP     | 7744   | 0 |
| QRRBBNNPPPPP    | 35984  | 0 |
| QRRBBNNPPPPPP   | 123036 | 1 |
| QRRBBNNPPPPPPP  | 204689 | 3 |
| QRRBBNNPPPPPPPP | 81615  | 1 |
| QRRBBNNNPPPPPP  | 1      | 0 |
| QRRBBNNNPPPPPPP | 1      | 0 |
| QRRBBNNNNPPPPPP | 2      | 0 |
| QRRBBBPPP       | 1      | 0 |
| QRRBBBPPPPP     | 1      | 0 |
| QRRBBBNNPPPPPP  | 1      | 0 |
| QRRRP           | 1      | 0 |
| QRRRPP          | 2      | 0 |
| QRRRPPPP        | 2      | 0 |
| QRRRPPPPPPP     | 1      | 0 |
| QRRRNPPP        | 1      | 0 |
| QRRRBPP         | 1      | 0 |
| QRRRBPPPPP      | 2      | 0 |
| QRRRBPPPPPP     | 2      | 0 |
| QRRRBNPPPP      | 2      | 0 |
| QRRRBNPPPPPP    | 2      | 0 |
| QRRRBNNPPPP     | 2      | 0 |
| QRRRBBNPPPP     | 1      | 0 |
| QRRRBBNPPPPP    | 1      | 0 |
| QRRRBBNNPPPPPPP | 1      | 0 |
| QQ              | 779    | 0 |
| QQP             | 1562   | 0 |
| QQPP            | 1912   | 0 |
| QQPPP           | 1435   | 0 |
| QQPPPP          | 699    | 0 |
| QQPPPPP         | 304    | 0 |
| QQPPPPPP        | 57     | 0 |
| QQPPPPPPP       | 2      | 0 |
| QQN             | 76     | 0 |
| QQNP            | 201    | 0 |
| QQNPP           | 202    | 0 |
| QQNPPP          | 230    | 0 |
| QQNPPPP         | 141    | 0 |
| QQNPPPPP        | 53     | 0 |
| QQNPPPPPP       | 14     | 0 |
| QQNN            | 1      | 0 |
| QQNNP           | 1      | 0 |
| QQNNPP          | 9      | 0 |
| QQNNPPP         | 12     | 0 |
| QQNNPPPP        | 8      | 0 |
| QQNNPPPPP       | 1      | 0 |
| QQNNPPPPPP      | 1      | 0 |
| QQB             | 95     | 0 |
| QQBP            | 379    | 0 |
| QQBPP           | 452    | 0 |
| QQBPPP          | 353    | 0 |
| QQBPPPP         | 228    | 0 |
| QQBPPPPP        | 85     | 0 |
| QQBPPPPPP       | 32     | 0 |
| QQBPPPPPPP      | 5      | 0 |
| QQBN            | 7      | 0 |
| QQBNP           | 25     | 0 |
| QQBNPP          | 50     | 0 |
| QQBNPPP         | 36     | 0 |
| QQBNPPPP        | 38     | 0 |
| QQBNPPPPP       | 18     | 0 |
| QQBNPPPPPP      | 5      | 0 |
| QQBNPPPPPPP     | 2      | 0 |
| QQBNNPP         | 1      | 0 |
| QQBNNPPP        | 4      | 0 |
| QQBNNPPPP       | 3      | 0 |
| QQBNNPPPPP      | 1      | 0 |
| QQBB            | 2      | 0 |
| QQBBP           | 3      | 0 |
| QQBBPP          | 4      | 0 |
| QQBBPPP         | 29     | 0 |
| QQBBPPPP        | 19     | 0 |
| QQBBPPPPP       | 10     | 0 |
| QQBBPPPPPP      | 5      | 0 |
| QQBBPPPPPPP     | 1      | 0 |
| QQBBNP          | 3      | 0 |
| QQBBNPP         | 2      | 0 |
| QQBBNPPP        | 5      | 0 |
| QQBBNPPPP       | 3      | 0 |
| QQBBNPPPPP      | 3      | 0 |
| QQBBNNPPPP      | 1      | 0 |
| QQBBNNPPPPPP    | 1      | 0 |
| QQR             | 151    | 0 |
| QQRP            | 294    | 0 |
| QQRPP           | 421    | 0 |
| QQRPPP          | 450    | 0 |
| QQRPPPP         | 386    | 0 |
| QQRPPPPP        | 165    | 0 |
| QQRPPPPPP       | 51     | 0 |
| QQRPPPPPPP      | 3      | 0 |
| QQRN            | 11     | 0 |
| QQRNP           | 37     | 0 |
| QQRNPP          | 56     | 0 |
| QQRNPPP         | 81     | 0 |
| QQRNPPPP        | 66     | 0 |
| QQRNPPPPP       | 47     | 0 |
| QQRNPPPPPP      | 20     | 0 |
| QQRNPPPPPPP     | 2      | 0 |
| QQRNNPP         | 3      | 0 |
| QQRNNPPP        | 6      | 0 |
| QQRNNPPPP       | 12     | 0 |
| QQRNNPPPPP      | 11     | 0 |
| QQRNNPPPPPP     | 2      | 0 |
| QQRB            | 16     | 0 |
| QQRBP           | 53     | 0 |
| QQRBPP          | 123    | 0 |
| QQRBPPP         | 220    | 0 |
| QQRBPPPP        | 184    | 0 |
| QQRBPPPPP       | 106    | 0 |
| QQRBPPPPPP      | 28     | 0 |
| QQRBPPPPPPP     | 5      | 0 |
| QQRBN           | 1      | 0 |
| QQRBNP          | 2      | 0 |
| QQRBNPP         | 16     | 0 |
| QQRBNPPP        | 43     | 0 |
| QQRBNPPPP       | 66     | 0 |
| QQRBNPPPPP      | 50     | 0 |
| QQRBNPPPPPP     | 10     | 0 |
| QQRBNPPPPPPP    | 4      | 0 |
| QQRBNNP         | 1      | 0 |
| QQRBNNPPP       | 9      | 0 |
| QQRBNNPPPP      | 9      | 0 |
| QQRBNNPPPPP     | 9      | 0 |
| QQRBNNPPPPPP    | 8      | 0 |
| QQRBBP          | 3      | 0 |
| QQRBBPP         | 4      | 0 |
| QQRBBPPP        | 26     | 0 |
| QQRBBPPPP       | 23     | 0 |
| QQRBBPPPPP      | 9      | 0 |
| QQRBBPPPPPP     | 5      | 0 |
| QQRBBNP         | 1      | 0 |
| QQRBBNPP        | 2      | 0 |
| QQRBBNPPP       | 7      | 0 |
| QQRBBNPPPP      | 9      | 0 |
| QQRBBNPPPPP     | 14     | 0 |
| QQRBBNPPPPPP    | 6      | 0 |
| QQRBBNPPPPPPP   | 4      | 0 |
| QQRBBNNPPP      | 1      | 0 |
| QQRBBNNPPPP     | 3      | 0 |
| QQRBBNNPPPPP    | 3      | 0 |
| QQRBBNNPPPPPP   | 3      | 0 |
| QQRBBNNPPPPPPP  | 1      | 0 |
| QQRR            | 9      | 0 |
| QQRRP           | 21     | 0 |
| QQRRPP          | 70     | 0 |
| QQRRPPP         | 118    | 0 |
| QQRRPPPP        | 130    | 0 |
| QQRRPPPPP       | 101    | 0 |
| QQRRPPPPPP      | 40     | 0 |
| QQRRPPPPPPP     | 9      | 0 |
| QQRRNP          | 6      | 0 |
| QQRRNPP         | 17     | 0 |
| QQRRNPPP        | 29     | 0 |
| QQRRNPPPP       | 56     | 0 |
| QQRRNPPPPP      | 46     | 0 |
| QQRRNPPPPPP     | 23     | 0 |
| QQRRNPPPPPPP    | 6      | 0 |
| QQRRNNPP        | 3      | 0 |
| QQRRNNPPP       | 2      | 0 |
| QQRRNNPPPP      | 8      | 0 |
| QQRRNNPPPPP     | 7      | 0 |
| QQRRNNPPPPPP    | 6      | 0 |
| QQRRNNPPPPPPP   | 3      | 0 |
| QQRRBP          | 10     | 0 |
| QQRRBPP         | 35     | 0 |
| QQRRBPPP        | 72     | 0 |
| QQRRBPPPP       | 68     | 0 |
| QQRRBPPPPP      | 81     | 0 |
| QQRRBPPPPPP     | 36     | 0 |
| QQRRBPPPPPPP    | 6      | 0 |
| QQRRBNPP        | 6      | 0 |
| QQRRBNPPP       | 18     | 0 |
| QQRRBNPPPP      | 47     | 0 |
| QQRRBNPPPPP     | 71     | 0 |
| QQRRBNPPPPPP    | 45     | 0 |
| QQRRBNPPPPPPP   | 17     | 0 |
| QQRRBNNPP       | 1      | 0 |
| QQRRBNNPPP      | 7      | 0 |
| QQRRBNNPPPP     | 2      | 0 |
| QQRRBNNPPPPP    | 10     | 0 |
| QQRRBNNPPPPPP   | 24     | 0 |
| QQRRBNNPPPPPPP  | 11     | 0 |
| QQRRBBPPP       | 6      | 0 |
| QQRRBBPPPP      | 15     | 0 |
| QQRRBBPPPPP     | 12     | 0 |
| QQRRBBPPPPPP    | 14     | 0 |
| QQRRBBPPPPPPP   | 3      | 0 |
| QQRRBBNPP       | 1      | 0 |
| QQRRBBNPPP      | 3      | 0 |
| QQRRBBNPPPP     | 14     | 0 |
| QQRRBBNPPPPP    | 18     | 0 |
| QQRRBBNPPPPPP   | 19     | 0 |
| QQRRBBNPPPPPPP  | 8      | 0 |
| QQRRBBNNPPP     | 3      | 0 |
| QQRRBBNNPPPP    | 4      | 0 |
| QQRRBBNNPPPPP   | 4      | 0 |
| QQRRBBNNPPPPPP  | 7      | 0 |
| QQRRBBNNPPPPPPP | 13     | 0 |
| QQRRRPPPP       | 1      | 0 |
| QQRRRBNNPPP     | 1      | 0 |
| QQQ             | 1      | 0 |
| QQQP            | 11     | 0 |
| QQQPP           | 11     | 0 |
| QQQPPP          | 9      | 0 |
| QQQPPPP         | 4      | 0 |
| QQQNP           | 2      | 0 |
| QQQNPP          | 1      | 0 |
| QQQBP           | 2      | 0 |
| QQQBPP          | 4      | 0 |
| QQQBPPPP        | 1      | 0 |
| QQQBPPPPP       | 1      | 0 |
| QQQBBPPP        | 1      | 0 |
| QQQRP           | 6      | 0 |
| QQQRPP          | 2      | 0 |
| QQQRPPP         | 4      | 0 |
| QQQRNP          | 3      | 0 |
| QQQRNPP         | 2      | 0 |
| QQQRNPPP        | 1      | 0 |
| QQQRNPPPP       | 1      | 0 |
| QQQRBPPP        | 1      | 0 |
| QQQRBPPPP       | 1      | 0 |
| QQQRBNP         | 1      | 0 |
| QQQRBNPP        | 3      | 0 |
| QQQRBNPPP       | 2      | 0 |
| QQQRRPP         | 1      | 0 |
| QQQRRPPP        | 1      | 0 |
| QQQRRBPPP       | 2      | 0 |
| QQQRRBPPPP      | 1      | 0 |
| QQQRRBBNNPPPP   | 1      | 0 |
| QQQRRBBNNPPPPPP | 1      | 0 |
| QQQQP           | 1      | 0 |
| QQQQPP          | 1      | 0 |
| QQQQRP          | 2      | 0 |
| QQQQRPP         | 2      | 0 |
| QQQQQP          | 1      | 0 |
| QQQQQNPP        | 1      | 0 |
| QQQQQRP         | 1      | 0 |
| QQQQQRNP        | 1      | 0 |
| QQQQQRRBNP      | 1      | 0 |
| QQQQQRRBBNNPP   | 1      | 0 |
| QQQQQQRNPP      | 1      | 0 |
| QQQQQQQRP       | 1      | 0 |


K adjusted to: 1.4800000000000002

## new optimization round

### Optimizing with step 10

Error at start: 0.08573823676825652

| Duration | Round | Step | Params adjustments | Adj total | Curr Error | Overall AdjPerSecond | AdjPerSecond |
|----------|-------|------|--------------------|-----------|------------|----------------------|--------------|
| 0:02:54,313 | 100   | 10   | 71                 | 71        | 0.08461514666793318 | 0.40804597701149425  | 0.40804597701149425 |
| 0:05:49,783 | 200   | 10   | 85                 | 156       | 0.08369540427439104 | 0.4469914040114613   | 0.4857142857142857  |
| 0:08:27,340 | 300   | 10   | 79                 | 235       | 0.08298616150663561 | 0.4635108481262327   | 0.5031847133757962  |
| 0:11:52,507 | 400   | 10   | 85                 | 320       | 0.08194772877548373 | 0.449438202247191    | 0.4146341463414634  |
| 0:15:01,222 | 500   | 10   | 57                 | 377       | 0.0812839646110021  | 0.4184239733629301   | 0.30319148936170215 |
| 0:17:50,680 | 600   | 10   | 56                 | 433       | 0.08069501875876547 | 0.4046728971962617   | 0.33136094674556216 |
| 0:20:43,331 | 700   | 10   | 55                 | 488       | 0.08045664395260392 | 0.3925985518905873   | 0.31976744186046513 |
| 0:24:03,188 | 800   | 10   | 36                 | 524       | 0.080149242161199   | 0.36313236313236313  | 0.18090452261306533 |
| 0:26:59,689 | 900   | 10   | 50                 | 574       | 0.0799167987226667  | 0.35453983940704137  | 0.2840909090909091  |
| 0:30:37,861 | 1000  | 10   | 48                 | 622       | 0.07973637352979276 | 0.3385955362003266   | 0.22018348623853212 |
| 0:33:50,781 | 1100  | 10   | 53                 | 675       | 0.0796531750800239  | 0.33251231527093594  | 0.2760416666666667  |
| 0:37:15,060 | 1200  | 10   | 37                 | 712       | 0.07919082548484122 | 0.3185682326621924   | 0.18137254901960784 |
| 0:40:20,233 | 1300  | 10   | 27                 | 739       | 0.07903168185618628 | 0.3053719008264463   | 0.14594594594594595 |
| 0:43:11,476 | 1400  | 10   | 52                 | 791       | 0.07873318140738955 | 0.3052875337707449   | 0.30409356725146197 |
| 0:45:44,666 | 1500  | 10   | 62                 | 853       | 0.07856263669646019 | 0.3108600583090379   | 0.40522875816993464 |
| 0:48:26,665 | 1600  | 10   | 36                 | 889       | 0.07825152158664084 | 0.3059187887130076   | 0.2236024844720497  |
| 0:50:51,298 | 1700  | 10   | 60                 | 949       | 0.07707253074777622 | 0.3110455588331695   | 0.4166666666666667  |
| 0:54:36,024 | 1800  | 10   | 48                 | 997       | 0.07681659803039072 | 0.30442748091603056  | 0.21428571428571427 |
| 0:58:14,926 | 1900  | 10   | 53                 | 1050      | 0.07663983978495553 | 0.3005151688609044   | 0.24311926605504589 |
| 1:01:33,600 | 2000  | 10   | 60                 | 1110      | 0.07655335404072769 | 0.3005686433793664   | 0.30303030303030304 |
| 1:05:01,232 | 2100  | 10   | 52                 | 1162      | 0.07639560702837814 | 0.2978723404255319   | 0.25120772946859904 |
| 1:08:09,823 | 2200  | 10   | 43                 | 1205      | 0.07627676352621034 | 0.2946930789924187   | 0.22872340425531915 |
| 1:11:29,712 | 2300  | 10   | 35                 | 1240      | 0.07618852401176275 | 0.2891116810445325   | 0.17587939698492464 |
| 1:14:36,694 | 2400  | 10   | 37                 | 1277      | 0.07614712881774595 | 0.2852993744414656   | 0.1989247311827957  |
| 1:18:11,884 | 2500  | 10   | 19                 | 1296      | 0.07608127252072022 | 0.27627371562566616  | 0.08837209302325581 |
| 1:21:41,561 | 2600  | 10   | 33                 | 1329      | 0.07603956871634052 | 0.27116914915323403  | 0.15789473684210525 |
| 1:25:26,041 | 2700  | 10   | 34                 | 1363      | 0.07600403304204986 | 0.26595121951219514  | 0.15178571428571427 |
| 1:29:00,670 | 2800  | 10   | 33                 | 1396      | 0.07597440084929404 | 0.2614232209737828   | 0.1542056074766355  |
| 1:33:00,863 | 2900  | 10   | 16                 | 1412      | 0.07587229497353941 | 0.2530465949820789   | 0.06666666666666667 |
| 1:36:51,553 | 3000  | 10   | 26                 | 1438      | 0.07574935711378    | 0.24746171054895888  | 0.11304347826086956 |
| 1:40:50,877 | 3100  | 10   | 48                 | 1486      | 0.07563824649529892 | 0.2456198347107438   | 0.200836820083682   |
| 1:44:03,204 | 3200  | 10   | 49                 | 1535      | 0.07558888844807221 | 0.2458753804260772   | 0.2552083333333333  |
| 1:47:29,657 | 3300  | 10   | 24                 | 1559      | 0.07555422397112839 | 0.24174290587688013  | 0.11650485436893204 |
| 1:50:41,503 | 3400  | 10   | 35                 | 1594      | 0.07539461841186317 | 0.2400240927571149   | 0.18324607329842932 |
| 1:54:10,234 | 3500  | 10   | 35                 | 1629      | 0.07524234005103962 | 0.2378102189781022   | 0.16826923076923078 |
| 1:58:07,175 | 3600  | 10   | 29                 | 1658      | 0.07521513895003318 | 0.23394948497248483  | 0.1228813559322034  |
| 2:01:34,268 | 3700  | 10   | 37                 | 1695      | 0.07518447648831528 | 0.23238278036742527  | 0.178743961352657   |
| 2:04:59,651 | 3800  | 10   | 47                 | 1742      | 0.07514626022742166 | 0.23229763968529138  | 0.22926829268292684 |
| 2:08:23,893 | 3900  | 10   | 21                 | 1763      | 0.0750817834852331  | 0.22887186810333637  | 0.10294117647058823 |
| 2:11:42,848 | 4000  | 10   | 21                 | 1784      | 0.07506346006050466 | 0.225765628954695    | 0.10606060606060606 |
| 2:15:08,202 | 4100  | 10   | 28                 | 1812      | 0.07504516474623328 | 0.22348297977306364  | 0.13658536585365855 |
| 2:18:38,252 | 4200  | 10   | 11                 | 1823      | 0.07502561236519939 | 0.21916326039913442  | 0.05238095238095238 |
| 2:21:56,807 | 4300  | 10   | 26                 | 1849      | 0.07501700702671682 | 0.21712071395021137  | 0.13131313131313133 |
| 2:25:42,048 | 4400  | 10   | 19                 | 1868      | 0.07500210404461671 | 0.2136810798444292   | 0.08444444444444445 |
| 2:29:29,411 | 4500  | 10   | 22                 | 1890      | 0.07499049184379274 | 0.21072583342624596  | 0.09691629955947137 |
| 2:33:20,478 | 4600  | 10   | 4                  | 1894      | 0.07498166018212898 | 0.2058695652173913   | 0.017316017316017316 |
| 2:37:03,875 | 4700  | 10   | 14                 | 1908      | 0.07494983926397915 | 0.20248328557784145  | 0.06278026905829596  |
| 2:40:42,971 | 4800  | 10   | 30                 | 1938      | 0.0749227058771208  | 0.20099564405724954  | 0.136986301369863    |
| 2:43:44,211 | 4900  | 10   | 45                 | 1983      | 0.07489954809841728 | 0.20185260586319217  | 0.24861878453038674  |
| 2:47:17,523 | 5000  | 10   | 24                 | 2007      | 0.07487411788175279 | 0.19996014745441865  | 0.11267605633802817  |
| 2:50:37,622 | 5100  | 10   | 21                 | 2028      | 0.0748307156433297  | 0.1981049135488913   | 0.105                |
| 2:53:55,434 | 5200  | 10   | 38                 | 2066      | 0.07472045802758576 | 0.19798754192620988  | 0.19289340101522842  |
| 2:57:28,657 | 5300  | 10   | 26                 | 2092      | 0.07470452126422562 | 0.19646882043576258  | 0.12206572769953052  |
| 3:01:09,571 | 5400  | 10   | 20                 | 2112      | 0.07469259488207808 | 0.19431410433342533  | 0.09090909090909091  |
| 3:04:49,160 | 5500  | 10   | 29                 | 2141      | 0.07468144772662112 | 0.19307421769320948  | 0.1324200913242009   |
| 3:08:09,860 | 5600  | 10   | 12                 | 2153      | 0.07467203562231202 | 0.1907166268048543   | 0.06                 |
| 3:11:30,780 | 5700  | 10   | 16                 | 2169      | 0.07466224005188796 | 0.18877284595300262  | 0.08                 |
| 3:14:48,984 | 5800  | 10   | 18                 | 2187      | 0.07464475307758803 | 0.18711498973305954  | 0.09090909090909091  |
| 3:18:11,584 | 5900  | 10   | 17                 | 2204      | 0.07463621282530307 | 0.1853502649062316   | 0.08415841584158416  |
| 3:21:20,837 | 6000  | 10   | 9                  | 2213      | 0.07463344743551742 | 0.1831953642384106   | 0.047619047619047616 |
| 3:25:00,893 | 6100  | 10   | 18                 | 2231      | 0.07462526512679098 | 0.18138211382113822  | 0.08181818181818182  |
| 3:28:35,455 | 6200  | 10   | 22                 | 2253      | 0.0746170184708766  | 0.18002397123451858  | 0.102803738317757    |
| 3:32:22,284 | 6300  | 10   | 6                  | 2259      | 0.0746081886089887  | 0.17728770993564588  | 0.02654867256637168  |
| 3:35:58,408 | 6400  | 10   | 13                 | 2272      | 0.07459402745852473 | 0.17533569995369655  | 0.06018518518518518  |
| 3:39:38,036 | 6500  | 10   | 19                 | 2291      | 0.07458352470061527 | 0.17386355012521817  | 0.0867579908675799   |
| 3:43:09,303 | 6600  | 10   | 28                 | 2319      | 0.07457665177958277 | 0.17320188214205692  | 0.13270142180094788  |
| 3:46:35,803 | 6700  | 10   | 19                 | 2338      | 0.07457053206764842 | 0.17197499080544318  | 0.09223300970873786  |
| 3:49:56,614 | 6800  | 10   | 9                  | 2347      | 0.07455969414307217 | 0.17012177442737025  | 0.045                |
| 3:53:14,608 | 6900  | 10   | 27                 | 2374      | 0.07452117621542119 | 0.16964413319994284  | 0.13705583756345177  |
| 3:56:42,112 | 7000  | 10   | 27                 | 2401      | 0.07451486687469344 | 0.16906069567666526  | 0.13043478260869565  |
| 4:00:24,365 | 7100  | 10   | 8                  | 2409      | 0.0745127651263604  | 0.1670133111480865   | 0.036036036036036036 |
| 4:03:55,530 | 7200  | 10   | 24                 | 2433      | 0.0745089750531989  | 0.16624530235736248  | 0.11374407582938388  |
| 4:07:26,395 | 7300  | 10   | 16                 | 2449      | 0.0744996028829131  | 0.1649602586555301   | 0.0761904761904762   |
| 4:11:09,704 | 7400  | 10   | 5                  | 2454      | 0.0744971015981998  | 0.16285088592474617  | 0.02242152466367713  |
| 4:14:48,551 | 7500  | 10   | 10                 | 2464      | 0.07448898699677987 | 0.16117216117216118  | 0.045871559633027525 |
| 4:18:26,890 | 7600  | 10   | 16                 | 2480      | 0.0744850551610408  | 0.159938088481878    | 0.07339449541284404  |
| 4:22:01,816 | 7700  | 10   | 6                  | 2486      | 0.07448275365304131 | 0.1581324343235163   | 0.028037383177570093 |
| 4:26:01,940 | 7800  | 10   | 12                 | 2498      | 0.07448202777298407 | 0.1565064845561055   | 0.05                 |
| 4:30:02,343 | 7900  | 10   | 13                 | 2511      | 0.07447696739511347 | 0.154980866559684    | 0.05416666666666667  |
| 4:34:09,903 | 8000  | 10   | 4                  | 2515      | 0.0744756887357288  | 0.1528968326342027   | 0.016194331983805668 |
| 4:38:15,115 | 8100  | 10   | 5                  | 2520      | 0.07446923367004166 | 0.1509433962264151   | 0.02040816326530612  |
| 4:42:26,200 | 8200  | 10   | 7                  | 2527      | 0.07446859519890298 | 0.149120736456981    | 0.027888446215139442 |
| 4:46:25,775 | 8300  | 10   | 15                 | 2542      | 0.07446617852491479 | 0.14791969741053243  | 0.06276150627615062  |
| 4:50:33,476 | 8400  | 10   | 18                 | 2560      | 0.07446359875388704 | 0.14684793208283142  | 0.0728744939271255   |
| 4:54:37,546 | 8500  | 10   | 0                  | 2560      | 0.07446359875388704 | 0.1448209537817503   | 0.0                  |
| 4:58:39,619 | 8600  | 10   | 19                 | 2579      | 0.07445179373939487 | 0.1439254422679837   | 0.07851239669421488  |
| 5:02:53,861 | 8700  | 10   | 20                 | 2599      | 0.07444484733709057 | 0.14301436196555328  | 0.07874015748031496  |
| 5:07:06,481 | 8800  | 10   | 9                  | 2608      | 0.07444072644564763 | 0.14153912949093672  | 0.03571428571428571  |
| 5:11:24,651 | 8900  | 10   | 21                 | 2629      | 0.07443756378748746 | 0.14070862770284737  | 0.08139534883720931  |
| 5:15:18,138 | 9000  | 10   | 9                  | 2638      | 0.07443580114584723 | 0.13944391584734114  | 0.03862660944206009  |
| 5:19:06,765 | 9100  | 10   | 4                  | 2642      | 0.07443522091005322 | 0.13799226992583308  | 0.017543859649122806 |
| 5:23:00,413 | 9200  | 10   | 5                  | 2647      | 0.0744342726051203  | 0.13658410732714138  | 0.02145922746781116  |
| 5:26:36,160 | 9300  | 10   | 9                  | 2656      | 0.07443341464331113 | 0.13553786487038172  | 0.04186046511627907  |
| 5:30:08,238 | 9400  | 10   | 1                  | 2657      | 0.07443317758700418 | 0.13413772213247172  | 0.0047169811320754715 |
| 5:34:09,575 | 9500  | 10   | 7                  | 2664      | 0.07443179402761954 | 0.13287445757893163  | 0.029045643153526972  |
| 5:38:10,053 | 9600  | 10   | 9                  | 2673      | 0.07443010658460002 | 0.13173977328733366  | 0.0375                |
| 5:42:10,657 | 9700  | 10   | 3                  | 2676      | 0.07442959579449356 | 0.13034583536288358  | 0.0125                |
| 5:46:11,132 | 9800  | 10   | 7                  | 2683      | 0.07442351608479798 | 0.1291704780703866   | 0.029166666666666667  |
| 5:50:16,441 | 9900  | 10   | 0                  | 2683      | 0.07442351608479798 | 0.12766463646745338  | 0.0                   |
| 5:54:03,152 | 10000 | 10   | 13                 | 2696      | 0.07442249878610481 | 0.1269123946711858   | 0.05752212389380531   |
| 5:57:43,048 | 10100 | 10   | 15                 | 2711      | 0.07442108488718556 | 0.1263162799366322   | 0.0684931506849315    |
| 6:01:19,798 | 10200 | 10   | 0                  | 2711      | 0.07442108488718556 | 0.1250518935375248   | 0.0                   |
| 6:05:07,277 | 10300 | 10   | 9                  | 2720      | 0.07441998043608382 | 0.12416122700506688  | 0.039647577092511016  |
| 6:09:05,977 | 10400 | 10   | 11                 | 2731      | 0.0744164088579016  | 0.12332354933393543  | 0.046218487394957986  |
| 6:12:59,317 | 10500 | 10   | 9                  | 2740      | 0.07441484087296782 | 0.12243621252066669  | 0.03862660944206009   |
| 6:16:51,772 | 10600 | 10   | 12                 | 2752      | 0.07441340713564668 | 0.121710671796913    | 0.05172413793103448   |
| 6:20:40,090 | 10700 | 10   | 10                 | 2762      | 0.07441014901330546 | 0.12092819614711034  | 0.043859649122807015  |
| 6:24:12,926 | 10800 | 10   | 4                  | 2766      | 0.07440958809481639 | 0.11998958875585633  | 0.018867924528301886  |
| 6:27:46,097 | 10900 | 10   | 4                  | 2770      | 0.07440879352106475 | 0.11905785266053469  | 0.018779342723004695  |
| 6:31:12,057 | 11000 | 10   | 13                 | 2783      | 0.07440791473276614 | 0.11856680299931834  | 0.06341463414634146   |
| 6:34:41,598 | 11100 | 10   | 0                  | 2783      | 0.07440791473276614 | 0.11752037498416452  | 0.0                   |
| 6:38:42,754 | 11200 | 10   | 6                  | 2789      | 0.07440767151167924 | 0.11658724186940891  | 0.024896265560165973  |
| 6:43:02,155 | 11300 | 10   | 5                  | 2794      | 0.07440740315328905 | 0.11554048465801009  | 0.019305019305019305  |
| 6:46:55,351 | 11400 | 10   | 3                  | 2797      | 0.07440709671813917 | 0.11456072086831866  | 0.012875536480686695  |
| 6:50:45,760 | 11500 | 10   | 5                  | 2802      | 0.07440653752227218 | 0.11369446135118685  | 0.021739130434782608  |
| 6:54:51,287 | 11600 | 10   | 1                  | 2803      | 0.07440647189734724 | 0.11261098388975935  | 0.004081632653061225  |
| 6:58:52,354 | 11700 | 10   | 11                 | 2814      | 0.07440565806288404 | 0.11196880471112526  | 0.04564315352697095   |
| 7:02:49,501 | 11800 | 10   | 9                  | 2823      | 0.07440472440831057 | 0.11127754345855177  | 0.0379746835443038    |
| 7:06:26,248 | 11900 | 10   | 1                  | 2824      | 0.07440464406613288 | 0.11037286015789885  | 0.004629629629629629  |
| 7:10:13,999 | 12000 | 10   | 7                  | 2831      | 0.07440218029375928 | 0.10967342036958122  | 0.030837004405286344  |
| 7:14:02,160 | 12100 | 10   | 10                 | 2841      | 0.07440024459443352 | 0.1090930036095538   | 0.043859649122807015  |
| 7:18:12,293 | 12200 | 10   | 5                  | 2846      | 0.0743993286163423  | 0.10824585425224403  | 0.02                  |
| 7:22:05,246 | 12300 | 10   | 9                  | 2855      | 0.0743989154412729  | 0.10763430725730443  | 0.03879310344827586   |
| 7:25:55,594 | 12400 | 10   | 9                  | 2864      | 0.07439842791263612 | 0.10704541207250981  | 0.0391304347826087    |
| 7:29:18,094 | 12500 | 10   | 3                  | 2867      | 0.07439815049620714 | 0.10635061948215743  | 0.01485148514851485   |
| 7:33:00,421 | 12600 | 10   | 1                  | 2868      | 0.07439792052168276 | 0.1055187637969095   | 0.0045045045045045045 |
| 7:36:33,789 | 12700 | 10   | 11                 | 2879      | 0.0743970521039994  | 0.10509984302559047  | 0.051643192488262914  |
| 7:40:11,462 | 12800 | 10   | 0                  | 2879      | 0.0743970521039994  | 0.10427003730397305  | 0.0                   |
| 7:44:13,398 | 12900 | 10   | 2                  | 2881      | 0.0743969953332893  | 0.10343589559472947  | 0.008298755186721992  |
| 7:48:08,304 | 13000 | 10   | 1                  | 2882      | 0.07439698231079783 | 0.1026060951295927   | 0.004273504273504274  |
| 7:52:12,032 | 13100 | 10   | 2                  | 2884      | 0.07439668410536415 | 0.10179661854505666  | 0.00823045267489712   |
| 7:56:49,157 | 13200 | 10   | 3                  | 2887      | 0.07439567063247424 | 0.10091230032507253  | 0.010830324909747292  |
| 8:00:58,265 | 13300 | 10   | 0                  | 2887      | 0.07439567063247424 | 0.10004158292327951  | 0.0                   |
| 8:05:03,421 | 13400 | 10   | 6                  | 2893      | 0.07439545409933256 | 0.09940555956430608  | 0.024489795918367346  |
| 8:09:08,852 | 13500 | 10   | 5                  | 2898      | 0.07439514265328706 | 0.0987460815047022   | 0.02040816326530612   |
| 8:13:09,957 | 13600 | 10   | 5                  | 2903      | 0.07439434324191296 | 0.0981107844131265   | 0.02074688796680498   |
| 8:17:17,164 | 13700 | 10   | 11                 | 2914      | 0.07438898742132946 | 0.0976639742601468   | 0.044534412955465584  |
| 8:21:39,081 | 13800 | 10   | 11                 | 2925      | 0.07438711994580713 | 0.0971793082826672   | 0.0421455938697318    |
| 8:25:47,767 | 13900 | 10   | 8                  | 2933      | 0.07438604085822564 | 0.09664876264540152  | 0.03225806451612903   |
| 8:29:53,543 | 14000 | 10   | 5                  | 2938      | 0.07438594507397277 | 0.09603504069558395  | 0.02040816326530612   |
| 8:34:02,190 | 14100 | 10   | 4                  | 2942      | 0.07438575414394928 | 0.09538940405939952  | 0.016129032258064516  |
| 8:37:38,568 | 14200 | 10   | 4                  | 2946      | 0.07438491944405881 | 0.09485478781634361  | 0.018518518518518517  |
| 8:41:09,473 | 14300 | 10   | 0                  | 2946      | 0.07438491944405881 | 0.09421471745178932  | 0.0                   |
| 8:44:50,080 | 14400 | 10   | 14                 | 2960      | 0.07438405058290003 | 0.0939980946332169   | 0.06363636363636363   |
| 8:48:19,092 | 14500 | 10   | 0                  | 2960      | 0.07438405058290003 | 0.0933783400107259   | 0.0                   |
| 8:52:36,527 | 14600 | 10   | 2                  | 2962      | 0.07438401965911673 | 0.09268994867943423  | 0.007782101167315175  |
| 8:56:38,909 | 14700 | 10   | 2                  | 2964      | 0.07438399044171634 | 0.09205540716814709  | 0.008264462809917356  |
| 9:00:43,151 | 14800 | 10   | 3                  | 2967      | 0.07438391205141896 | 0.09145270166137534  | 0.012295081967213115  |
| 9:04:51,629 | 14900 | 10   | 3                  | 2970      | 0.07438297527823308 | 0.09085069285124346  | 0.012096774193548387  |
| 9:08:56,891 | 15000 | 10   | 0                  | 2970      | 0.07438297527823308 | 0.09017488462472674  | 0.0                   |
| 9:12:50,207 | 15100 | 10   | 5                  | 2975      | 0.07438258149040508 | 0.08968947844437745  | 0.02145922746781116   |
| 9:16:44,525 | 15200 | 10   | 3                  | 2978      | 0.07438252321035393 | 0.08915099988025386  | 0.01282051282051282   |
| 9:20:14,048 | 15300 | 10   | 5                  | 2983      | 0.07438208333044374 | 0.08874278574403523  | 0.023923444976076555  |
| 9:23:52,268 | 15400 | 10   | 12                 | 2995      | 0.07437998579455127 | 0.08852565618349492  | 0.05504587155963303   |
| 9:27:45,900 | 15500 | 10   | 9                  | 3004      | 0.0743782749099223  | 0.08818435344194922  | 0.03862660944206009   |
| 9:31:30,618 | 15600 | 10   | 11                 | 3015      | 0.07437502625312821 | 0.08792650918635171  | 0.049107142857142856  |
| 9:35:25,533 | 15700 | 10   | 4                  | 3019      | 0.07437402203602629 | 0.0874438812454743   | 0.017094017094017096  |
| 9:39:32,655 | 15800 | 10   | 1                  | 3020      | 0.07437400582721117 | 0.08685148970435982  | 0.004048582995951417  |
| 9:43:00,723 | 15900 | 10   | 5                  | 3025      | 0.07437079845918912 | 0.08647798742138364  | 0.02403846153846154   |
| 9:46:18,623 | 16000 | 10   | 0                  | 3025      | 0.07437079845918912 | 0.0859912445278299   | 0.0                   |
| 9:50:00,224 | 16100 | 10   | 4                  | 3029      | 0.07437048754191178 | 0.08556497175141244  | 0.01809954751131222   |
| 9:53:25,907 | 16200 | 10   | 1                  | 3030      | 0.07437044607547347 | 0.08510040724617329  | 0.004878048780487805  |
| 9:57:10,874 | 16300 | 10   | 2                  | 3032      | 0.07437038330580441 | 0.08462182528607312  | 0.008928571428571428  |
| 10:01:09,943 | 16400 | 10   | 1                  | 3033      | 0.0743703723671173  | 0.0840888297429926   | 0.0041841004184100415 |
| 10:05:01,007 | 16500 | 10   | 3                  | 3036      | 0.07436988170655054 | 0.08363636363636363  | 0.012987012987012988  |
| 10:08:53,306 | 16600 | 10   | 1                  | 3037      | 0.07436986886868747 | 0.08313032053212165  | 0.004310344827586207  |
| 10:12:46,467 | 16700 | 10   | 1                  | 3038      | 0.07436982365010096 | 0.08263069139966273  | 0.004291845493562232  |
| 10:16:39,773 | 16800 | 10   | 2                  | 3040      | 0.07436973436350312 | 0.08216438282115733  | 0.008583690987124463  |
| 10:20:33,877 | 16900 | 10   | 3                  | 3043      | 0.07436960658979928 | 0.08172857411436092  | 0.01282051282051282   |
| 10:24:07,883 | 17000 | 10   | 4                  | 3047      | 0.07436925621075495 | 0.08136833391192885  | 0.018691588785046728  |
| 10:27:53,848 | 17100 | 10   | 13                 | 3060      | 0.07436480821269154 | 0.0812252807050142   | 0.057777777777777775  |
| 10:31:45,811 | 17200 | 10   | 6                  | 3066      | 0.0743639131426384  | 0.08088642659279778  | 0.025974025974025976  |
| 10:35:43,097 | 17300 | 10   | 6                  | 3072      | 0.07436235520240796 | 0.08053902419841125  | 0.02531645569620253   |
| 10:39:30,439 | 17400 | 10   | 4                  | 3076      | 0.07436199766345701 | 0.0801667969768048   | 0.01762114537444934   |
| 10:43:38,468 | 17500 | 10   | 2                  | 3078      | 0.07436180399188015 | 0.07970376508363976  | 0.008064516129032258  |
| 10:47:01,919 | 17600 | 10   | 6                  | 3084      | 0.0743579405841381  | 0.07944153937301976  | 0.029556650246305417  |
| 10:50:27,899 | 17700 | 10   | 2                  | 3086      | 0.07435500992889664 | 0.07907346196223128  | 0.00975609756097561   |
| 10:54:11,317 | 17800 | 10   | 7                  | 3093      | 0.07435429229851438 | 0.07880054011362768  | 0.03139013452914798   |
| 10:57:36,525 | 17900 | 10   | 0                  | 3093      | 0.07435429229851438 | 0.07839111922141119  | 0.0                   |
| 11:01:24,822 | 18000 | 10   | 1                  | 3094      | 0.07435426947158957 | 0.07796593085374458  | 0.0043859649122807015 |
| 11:05:28,118 | 18100 | 10   | 1                  | 3095      | 0.07435343359845843 | 0.07751452614706472  | 0.00411522633744856   |
| 11:09:19,370 | 18200 | 10   | 2                  | 3097      | 0.07435310334070547 | 0.07711845414477451  | 0.008658008658008658  |
| 11:13:17,328 | 18300 | 10   | 2                  | 3099      | 0.07435165376650807 | 0.07671361734782285  | 0.008438818565400843  |
| 11:17:22,405 | 18400 | 10   | 0                  | 3099      | 0.07435165376650807 | 0.07625116874169578  | 0.0                   |
| 11:21:29,727 | 18500 | 10   | 5                  | 3104      | 0.07435153607040754 | 0.07591283719337719  | 0.020242914979757085  |
| 11:25:22,759 | 18600 | 10   | 3                  | 3107      | 0.07435143334545768 | 0.07555566363503721  | 0.012875536480686695  |
| 11:28:59,179 | 18700 | 10   | 5                  | 3112      | 0.0743511342998342  | 0.07528000193521855  | 0.023148148148148147  |
| 11:32:39,939 | 18800 | 10   | 12                 | 3124      | 0.07434813334596141 | 0.07517023989990135  | 0.05454545454545454   |
| 11:36:30,198 | 18900 | 10   | 3                  | 3127      | 0.07434750198371573 | 0.07482651351998086  | 0.013043478260869565  |
| 11:40:18,292 | 19000 | 10   | 6                  | 3133      | 0.07434552656113948 | 0.07456328240277976  | 0.02631578947368421   |
| 11:44:09,648 | 19100 | 10   | 5                  | 3138      | 0.07434503108593839 | 0.07427394731236242  | 0.021645021645021644  |
| 11:48:00,690 | 19200 | 10   | 3                  | 3141      | 0.074344786604989   | 0.0739406779661017   | 0.012987012987012988  |
| 11:51:31,680 | 19300 | 10   | 6                  | 3147      | 0.07434247210038741 | 0.07371577147408119  | 0.02857142857142857   |
| 11:54:55,369 | 19400 | 10   | 1                  | 3148      | 0.0743418077498109  | 0.07338850681897657  | 0.0049261083743842365 |
| 11:58:23,683 | 19500 | 10   | 4                  | 3152      | 0.07434172050450731 | 0.07312716052246944  | 0.019230769230769232  |
| 12:01:53,054 | 19600 | 10   | 0                  | 3152      | 0.07434172050450731 | 0.07277428888067972  | 0.0                   |
| 12:05:22,437 | 19700 | 10   | 5                  | 3157      | 0.07434144651232166 | 0.07253802674509444  | 0.023923444976076555  |
| 12:09:18,498 | 19800 | 10   | 0                  | 3157      | 0.07434144651232166 | 0.07214680744092508  | 0.0                   |
| 12:13:13,061 | 19900 | 10   | 1                  | 3158      | 0.07434142927331654 | 0.07178577923258775  | 0.004273504273504274  |
| 12:17:03,520 | 20000 | 10   | 1                  | 3159      | 0.07434141300644245 | 0.07143341700020352  | 0.004347826086956522  |
| 12:21:02,583 | 20100 | 10   | 1                  | 3160      | 0.07434129413256893 | 0.07107192658899734  | 0.0041841004184100415 |
| 12:25:05,784 | 20200 | 10   | 0                  | 3160      | 0.07434129413256893 | 0.07068560563695336  | 0.0                   |
| 12:28:54,666 | 20300 | 10   | 4                  | 3164      | 0.07434119329514391 | 0.0704143855432412   | 0.017543859649122806  |
| 12:32:31,356 | 20400 | 10   | 7                  | 3171      | 0.074340948695842   | 0.07023100263560055  | 0.032407407407407406  |
| 12:36:14,491 | 20500 | 10   | 6                  | 3177      | 0.07434023348464931 | 0.07001807202362587  | 0.026905829596412557  |
| 12:40:05,409 | 20600 | 10   | 5                  | 3182      | 0.07433969589740916 | 0.06977305120052626  | 0.021739130434782608  |
| 12:43:55,784 | 20700 | 10   | 2                  | 3184      | 0.07433963973971687 | 0.06946656485218719  | 0.008695652173913044  |
| 12:47:49,522 | 20800 | 10   | 2                  | 3186      | 0.07433941997023158 | 0.06915713386442077  | 0.008583690987124463  |
| 12:51:45,686 | 20900 | 10   | 4                  | 3190      | 0.07433924582533603 | 0.06889104848288521  | 0.01694915254237288   |
| 12:55:20,894 | 21000 | 10   | 1                  | 3191      | 0.07433921377301372 | 0.06859415305245056  | 0.004651162790697674  |
| 12:58:53,090 | 21100 | 10   | 0                  | 3191      | 0.07433921377301372 | 0.0682815141334817   | 0.0                   |
| 13:02:23,845 | 21200 | 10   | 2                  | 3193      | 0.07433903508546463 | 0.06801866092921202  | 0.009523809523809525  |
| 13:06:12,464 | 21300 | 10   | 0                  | 3193      | 0.07433903508546463 | 0.06768845925549055  | 0.0                   |
| 13:09:31,859 | 21400 | 10   | 1                  | 3194      | 0.07433902151890905 | 0.06742521796035549  | 0.005025125628140704  |
| 13:13:29,733 | 21500 | 10   | 0                  | 3194      | 0.07433902151890905 | 0.06708815560083177  | 0.0                   |
| 13:17:29,714 | 21600 | 10   | 0                  | 3194      | 0.07433902151890905 | 0.0667516562519593   | 0.0                   |
| 13:21:24,210 | 21700 | 10   | 2                  | 3196      | 0.07433809858807584 | 0.06646701605523667  | 0.008547008547008548  |
| 13:25:11,509 | 21800 | 10   | 0                  | 3196      | 0.07433809858807584 | 0.06615470596758502  | 0.0                   |
| 13:29:20,032 | 21900 | 10   | 0                  | 3196      | 0.07433809858807584 | 0.06581684136823246  | 0.0                   |
| 13:33:18,641 | 22000 | 10   | 5                  | 3201      | 0.07433792686187228 | 0.0655969506947006   | 0.02100840336134454   |
| 13:36:59,765 | 22100 | 10   | 4                  | 3205      | 0.0743377370116822  | 0.06538281074685326  | 0.01809954751131222   |
| 13:40:37,694 | 22200 | 10   | 3                  | 3208      | 0.07433756505419842 | 0.06515425391473892  | 0.013824884792626729  |
| 13:44:29,865 | 22300 | 10   | 3                  | 3211      | 0.07433733339409918 | 0.06490933716064606  | 0.01293103448275862   |
| 13:48:18,959 | 22400 | 10   | 1                  | 3212      | 0.07433731645315855 | 0.06463036741921203  | 0.004366812227074236  |
| 13:52:08,381 | 22500 | 10   | 1                  | 3213      | 0.07433729934854882 | 0.06435266784169204  | 0.004366812227074236  |
| 13:55:56,205 | 22600 | 10   | 6                  | 3219      | 0.07433710958019779 | 0.06417975915144748  | 0.02643171806167401   |
| 13:59:23,738 | 22700 | 10   | 0                  | 3219      | 0.07433710958019779 | 0.06391597005738339  | 0.0                   |
| 14:02:50,610 | 22800 | 10   | 1                  | 3220      | 0.07433709868857252 | 0.06367411508799684  | 0.0048543689320388345 |
| 14:06:20,713 | 22900 | 10   | 4                  | 3224      | 0.07433698215676082 | 0.06348956282000788  | 0.01904761904761905   |
| 14:10:00,082 | 23000 | 10   | 0                  | 3224      | 0.07433698215676082 | 0.06321568627450981  | 0.0                   |
| 14:13:27,142 | 23100 | 10   | 0                  | 3224      | 0.07433698215676082 | 0.06296014216806296  | 0.0                   |
| 14:17:19,518 | 23200 | 10   | 1                  | 3225      | 0.0743369557457291  | 0.06269562005482221  | 0.004310344827586207  |
| 14:21:09,518 | 23300 | 10   | 0                  | 3225      | 0.0743369557457291  | 0.06241653602740521  | 0.0                   |
| 14:25:21,645 | 23400 | 10   | 0                  | 3225      | 0.0743369557457291  | 0.062113595654937306 | 0.0                   |
| 14:29:12,654 | 23500 | 10   | 2                  | 3227      | 0.07433629383209023 | 0.061876821598404665 | 0.008658008658008658  |
| 14:32:58,177 | 23600 | 10   | 0                  | 3227      | 0.07433629383209023 | 0.06160983619076712  | 0.0                   |
| 14:36:53,579 | 23700 | 10   | 1                  | 3228      | 0.0743362798767759  | 0.06135365784121795  | 0.00425531914893617   |
| 14:40:38,206 | 23800 | 10   | 7                  | 3235      | 0.07433608525471166 | 0.06122487603618608  | 0.03125               |
| 14:44:15,424 | 23900 | 10   | 1                  | 3236      | 0.07433605735379353 | 0.060993308830458955 | 0.004608294930875576  |
| 14:48:30,410 | 24000 | 10   | 1                  | 3237      | 0.0743360266302876  | 0.06072031513787282  | 0.003937007874015748  |
# Tuning Options

| Parameter                    | Value                        |
|------------------------------|------------------------------|
| adjustK                      | true                         |
| delta                        | 1.0E-8                       |
| evalParamSet                 | CURRENT                      |
| geneticParams                | null                         |
| inputFiles                   | [C:\projekte\cygwin_home\mla\jackyChessDockerTesting\tuningdata\lichess-big3-resolved.book]|
| multiThreading               | true                         |
| name                         | lichess test2                |
| optimizeRecalcOnlyDependendFens| false                        |
| outputdir                    | null                         |
| progressUpdatesInMinutes     | 0                            |
| removeDuplicateFens          | true                         |
| resetParametersBeforeTuning  | false                        |
| shuffleTuningParameter       | false                        |
| stepGranularity              | [10, 5, 3, 1]                |
| threadCount                  | 5                            |
| tuneAdjustments              | true                         |
| tuneAdjustmentsFactors       | false                        |
| tuneComplexity               | true                         |
| tuneKingAttack               | true                         |
| tuneKingSafety               | true                         |
| tuneMaterial                 | false                        |
| tuneMobility                 | true                         |
| tuneMobilityTropism          | true                         |
| tunePassedPawnEval           | true                         |
| tunePawnEval                 | true                         |
| tunePositional               | true                         |
| tunePst                      | true                         |
| tuneThreats                  | true                         |


# Dataset Information

|                |         |
|----------------|---------|
| Num Fens       | 6819087 |
| MATE White     | 2801909 |
| MATE Black     | 2605257 |
| Draws          | 1411921 |
| Duplicate Fens | 0       |


## Number of fens by Game Phase

| Phase | Count  | %  |
|-------|--------|----|
| 0     | 593109 | 8  |
| 1     | 923895 | 13 |
| 2     | 736133 | 10 |
| 3     | 619828 | 9  |
| 4     | 561207 | 8  |
| 5     | 463708 | 6  |
| 6     | 437031 | 6  |
| 7     | 530586 | 7  |
| 8     | 654432 | 9  |
| 9     | 692383 | 10 |
| 10    | 606775 | 8  |


## Number of fens by having figures

| FigureType | Count   | %   |
|------------|---------|-----|
| Rook       | 5988879 | 87  |
| Knight     | 4456890 | 65  |
| Bishop     | 4966199 | 72  |
| Queen      | 3817373 | 55  |
| Pawn       | 6819087 | 100 |


## Number of fens by Material

| Material        | Count  | % |
|-----------------|--------|---|
|                 | 33454  | 0 |
| P               | 76606  | 1 |
| PP              | 122515 | 1 |
| PPP             | 132276 | 1 |
| PPPP            | 103981 | 1 |
| PPPPP           | 68109  | 0 |
| PPPPPP          | 32877  | 0 |
| PPPPPPP         | 8662   | 0 |
| PPPPPPPP        | 323    | 0 |
| N               | 12616  | 0 |
| NP              | 33814  | 0 |
| NPP             | 55129  | 0 |
| NPPP            | 68974  | 1 |
| NPPPP           | 64043  | 0 |
| NPPPPP          | 49168  | 0 |
| NPPPPPP         | 28688  | 0 |
| NPPPPPPP        | 9006   | 0 |
| NPPPPPPPP       | 209    | 0 |
| NN              | 530    | 0 |
| NNP             | 1841   | 0 |
| NNPP            | 3202   | 0 |
| NNPPP           | 5025   | 0 |
| NNPPPP          | 5922   | 0 |
| NNPPPPP         | 6029   | 0 |
| NNPPPPPP        | 4408   | 0 |
| NNPPPPPPP       | 2177   | 0 |
| NNPPPPPPPP      | 20     | 0 |
| NNN             | 3      | 0 |
| NNNP            | 4      | 0 |
| NNNPP           | 13     | 0 |
| NNNPPP          | 4      | 0 |
| NNNPPPP         | 3      | 0 |
| NNNPPPPP        | 1      | 0 |
| NNNNPPP         | 2      | 0 |
| B               | 16616  | 0 |
| BP              | 51273  | 0 |
| BPP             | 87515  | 1 |
| BPPP            | 103420 | 1 |
| BPPPP           | 100360 | 1 |
| BPPPPP          | 76023  | 1 |
| BPPPPPP         | 42486  | 0 |
| BPPPPPPP        | 11779  | 0 |
| BPPPPPPPP       | 401    | 0 |
| BN              | 2812   | 0 |
| BNP             | 9913   | 0 |
| BNPP            | 19555  | 0 |
| BNPPP           | 27490  | 0 |
| BNPPPP          | 32087  | 0 |
| BNPPPPP         | 29573  | 0 |
| BNPPPPPP        | 21544  | 0 |
| BNPPPPPPP       | 8004   | 0 |
| BNPPPPPPPP      | 155    | 0 |
| BNN             | 49     | 0 |
| BNNP            | 275    | 0 |
| BNNPP           | 873    | 0 |
| BNNPPP          | 1789   | 0 |
| BNNPPPP         | 2884   | 0 |
| BNNPPPPP        | 3467   | 0 |
| BNNPPPPPP       | 3189   | 0 |
| BNNPPPPPPP      | 1840   | 0 |
| BNNPPPPPPPP     | 14     | 0 |
| BNNNP           | 1      | 0 |
| BNNNPPPP        | 1      | 0 |
| BNNNNNP         | 1      | 0 |
| BNNNNNPP        | 1      | 0 |
| BB              | 1971   | 0 |
| BBP             | 4075   | 0 |
| BBPP            | 7041   | 0 |
| BBPPP           | 11143  | 0 |
| BBPPPP          | 11437  | 0 |
| BBPPPPP         | 11128  | 0 |
| BBPPPPPP        | 8216   | 0 |
| BBPPPPPPP       | 2873   | 0 |
| BBPPPPPPPP      | 131    | 0 |
| BBN             | 93     | 0 |
| BBNP            | 299    | 0 |
| BBNPP           | 1039   | 0 |
| BBNPPP          | 2299   | 0 |
| BBNPPPP         | 3735   | 0 |
| BBNPPPPP        | 4460   | 0 |
| BBNPPPPPP       | 4140   | 0 |
| BBNPPPPPPP      | 2009   | 0 |
| BBNPPPPPPPP     | 23     | 0 |
| BBNNP           | 20     | 0 |
| BBNNPP          | 79     | 0 |
| BBNNPPP         | 301    | 0 |
| BBNNPPPP        | 636    | 0 |
| BBNNPPPPP       | 738    | 0 |
| BBNNPPPPPP      | 880    | 0 |
| BBNNPPPPPPP     | 633    | 0 |
| BBNNPPPPPPPP    | 5      | 0 |
| BBNNNP          | 3      | 0 |
| BBBP            | 4      | 0 |
| BBBPP           | 4      | 0 |
| BBBPPP          | 3      | 0 |
| BBBPPPP         | 4      | 0 |
| BBBNP           | 1      | 0 |
| BBBNPPP         | 1      | 0 |
| BBBNNP          | 1      | 0 |
| BBBBP           | 3      | 0 |
| BBBBPPP         | 2      | 0 |
| BBBBPPPP        | 1      | 0 |
| BBBBNPP         | 1      | 0 |
| BBBBBPPP        | 1      | 0 |
| BBBBBBPP        | 1      | 0 |
| R               | 70970  | 1 |
| RP              | 192547 | 2 |
| RPP             | 252064 | 3 |
| RPPP            | 268348 | 3 |
| RPPPP           | 221962 | 3 |
| RPPPPP          | 158007 | 2 |
| RPPPPPP         | 84534  | 1 |
| RPPPPPPP        | 22633  | 0 |
| RPPPPPPPP       | 1060   | 0 |
| RN              | 14423  | 0 |
| RNP             | 32920  | 0 |
| RNPP            | 60144  | 0 |
| RNPPP           | 96059  | 1 |
| RNPPPP          | 107436 | 1 |
| RNPPPPP         | 104563 | 1 |
| RNPPPPPP        | 69062  | 1 |
| RNPPPPPPP       | 20732  | 0 |
| RNPPPPPPPP      | 1028   | 0 |
| RNN             | 203    | 0 |
| RNNP            | 1056   | 0 |
| RNNPP           | 3050   | 0 |
| RNNPPP          | 6878   | 0 |
| RNNPPPP         | 11631  | 0 |
| RNNPPPPP        | 15220  | 0 |
| RNNPPPPPP       | 12681  | 0 |
| RNNPPPPPPP      | 5002   | 0 |
| RNNPPPPPPPP     | 254    | 0 |
| RNNN            | 1      | 0 |
| RNNNP           | 4      | 0 |
| RNNNPP          | 1      | 0 |
| RNNNPPP         | 5      | 0 |
| RNNNPPPP        | 2      | 0 |
| RNNNPPPPP       | 1      | 0 |
| RNNNNP          | 1      | 0 |
| RNNNNPP         | 2      | 0 |
| RB              | 15708  | 0 |
| RBP             | 44306  | 0 |
| RBPP            | 82180  | 1 |
| RBPPP           | 125978 | 1 |
| RBPPPP          | 151618 | 2 |
| RBPPPPP         | 144476 | 2 |
| RBPPPPPP        | 94000  | 1 |
| RBPPPPPPP       | 27932  | 0 |
| RBPPPPPPPP      | 1485   | 0 |
| RBN             | 1368   | 0 |
| RBNP            | 5362   | 0 |
| RBNPP           | 15999  | 0 |
| RBNPPP          | 36004  | 0 |
| RBNPPPP         | 62303  | 0 |
| RBNPPPPP        | 79905  | 1 |
| RBNPPPPPP       | 64804  | 0 |
| RBNPPPPPPP      | 23491  | 0 |
| RBNPPPPPPPP     | 1280   | 0 |
| RBNN            | 34     | 0 |
| RBNNP           | 233    | 0 |
| RBNNPP          | 1117   | 0 |
| RBNNPPP         | 3910   | 0 |
| RBNNPPPP        | 8980   | 0 |
| RBNNPPPPP       | 14328  | 0 |
| RBNNPPPPPP      | 14249  | 0 |
| RBNNPPPPPPP     | 6298   | 0 |
| RBNNPPPPPPPP    | 410    | 0 |
| RBNNNP          | 6      | 0 |
| RBNNNPP         | 2      | 0 |
| RBNNNPPP        | 3      | 0 |
| RBNNNPPPP       | 2      | 0 |
| RBNNNPPPPP      | 1      | 0 |
| RBNNNNPP        | 1      | 0 |
| RBNNNNPPP       | 1      | 0 |
| RBB             | 763    | 0 |
| RBBP            | 1433   | 0 |
| RBBPP           | 4738   | 0 |
| RBBPPP          | 11059  | 0 |
| RBBPPPP         | 20282  | 0 |
| RBBPPPPP        | 26105  | 0 |
| RBBPPPPPP       | 21218  | 0 |
| RBBPPPPPPP      | 7519   | 0 |
| RBBPPPPPPPP     | 453    | 0 |
| RBBN            | 40     | 0 |
| RBBNP           | 304    | 0 |
| RBBNPP          | 1287   | 0 |
| RBBNPPP         | 4610   | 0 |
| RBBNPPPP        | 10988  | 0 |
| RBBNPPPPP       | 18234  | 0 |
| RBBNPPPPPP      | 17827  | 0 |
| RBBNPPPPPPP     | 7268   | 0 |
| RBBNPPPPPPPP    | 402    | 0 |
| RBBNN           | 2      | 0 |
| RBBNNP          | 39     | 0 |
| RBBNNPP         | 168    | 0 |
| RBBNNPPP        | 789    | 0 |
| RBBNNPPPP       | 2327   | 0 |
| RBBNNPPPPP      | 4686   | 0 |
| RBBNNPPPPPP     | 5215   | 0 |
| RBBNNPPPPPPP    | 2659   | 0 |
| RBBNNPPPPPPPP   | 150    | 0 |
| RBBNNNPPPP      | 1      | 0 |
| RBBNNNPPPPP     | 1      | 0 |
| RBBNNNPPPPPP    | 1      | 0 |
| RBBBP           | 1      | 0 |
| RBBBPP          | 1      | 0 |
| RBBBPPP         | 2      | 0 |
| RBBBNP          | 1      | 0 |
| RBBBNPP         | 2      | 0 |
| RBBBNPPP        | 1      | 0 |
| RBBBBP          | 2      | 0 |
| RBBBBPP         | 2      | 0 |
| RR              | 8637   | 0 |
| RRP             | 22399  | 0 |
| RRPP            | 43620  | 0 |
| RRPPP           | 71470  | 1 |
| RRPPPP          | 91824  | 1 |
| RRPPPPP         | 99571  | 1 |
| RRPPPPPP        | 78830  | 1 |
| RRPPPPPPP       | 35657  | 0 |
| RRPPPPPPPP      | 4122   | 0 |
| RRN             | 1368   | 0 |
| RRNP            | 4107   | 0 |
| RRNPP           | 11489  | 0 |
| RRNPPP          | 27864  | 0 |
| RRNPPPP         | 53679  | 0 |
| RRNPPPPP        | 83375  | 1 |
| RRNPPPPPP       | 88799  | 1 |
| RRNPPPPPPP      | 50063  | 0 |
| RRNPPPPPPPP     | 7135   | 0 |
| RRNN            | 35     | 0 |
| RRNNP           | 186    | 0 |
| RRNNPP          | 841    | 0 |
| RRNNPPP         | 3292   | 0 |
| RRNNPPPP        | 8797   | 0 |
| RRNNPPPPP       | 17939  | 0 |
| RRNNPPPPPP      | 24327  | 0 |
| RRNNPPPPPPP     | 17522  | 0 |
| RRNNPPPPPPPP    | 3029   | 0 |
| RRNNNP          | 1      | 0 |
| RRNNNPP         | 1      | 0 |
| RRNNNPPPP       | 2      | 0 |
| RRNNNPPPPPP     | 1      | 0 |
| RRNNNNPP        | 1      | 0 |
| RRB             | 1118   | 0 |
| RRBP            | 5361   | 0 |
| RRBPP           | 15376  | 0 |
| RRBPPP          | 38038  | 0 |
| RRBPPPP         | 74578  | 1 |
| RRBPPPPP        | 115257 | 1 |
| RRBPPPPPP       | 120731 | 1 |
| RRBPPPPPPP      | 65672  | 0 |
| RRBPPPPPPPP     | 8391   | 0 |
| RRBN            | 392    | 0 |
| RRBNP           | 1445   | 0 |
| RRBNPP          | 4662   | 0 |
| RRBNPPP         | 16718  | 0 |
| RRBNPPPP        | 46521  | 0 |
| RRBNPPPPP       | 96948  | 1 |
| RRBNPPPPPP      | 136001 | 1 |
| RRBNPPPPPPP     | 93586  | 1 |
| RRBNPPPPPPPP    | 14421  | 0 |
| RRBNN           | 6      | 0 |
| RRBNNP          | 78     | 0 |
| RRBNNPP         | 503    | 0 |
| RRBNNPPP        | 2559   | 0 |
| RRBNNPPPP       | 9221   | 0 |
| RRBNNPPPPP      | 24702  | 0 |
| RRBNNPPPPPP     | 44521  | 0 |
| RRBNNPPPPPPP    | 37629  | 0 |
| RRBNNPPPPPPPP   | 6990   | 0 |
| RRBNNNPP        | 1      | 0 |
| RRBNNNPPP       | 1      | 0 |
| RRBNNNPPPP      | 2      | 0 |
| RRBB            | 221    | 0 |
| RRBBP           | 336    | 0 |
| RRBBPP          | 1394   | 0 |
| RRBBPPP         | 5139   | 0 |
| RRBBPPPP        | 14214  | 0 |
| RRBBPPPPP       | 30328  | 0 |
| RRBBPPPPPP      | 42966  | 0 |
| RRBBPPPPPPP     | 27875  | 0 |
| RRBBPPPPPPPP    | 3618   | 0 |
| RRBBN           | 11     | 0 |
| RRBBNP          | 90     | 0 |
| RRBBNPP         | 672    | 0 |
| RRBBNPPP        | 3055   | 0 |
| RRBBNPPPP       | 11591  | 0 |
| RRBBNPPPPP      | 31919  | 0 |
| RRBBNPPPPPP     | 58179  | 0 |
| RRBBNPPPPPPP    | 45224  | 0 |
| RRBBNPPPPPPPP   | 6508   | 0 |
| RRBBNN          | 1      | 0 |
| RRBBNNP         | 9      | 0 |
| RRBBNNPP        | 117    | 0 |
| RRBBNNPPP       | 673    | 0 |
| RRBBNNPPPP      | 3139   | 0 |
| RRBBNNPPPPP     | 10137  | 0 |
| RRBBNNPPPPPP    | 21645  | 0 |
| RRBBNNPPPPPPP   | 19947  | 0 |
| RRBBNNPPPPPPPP  | 3797   | 0 |
| RRBBNNNPPPPPP   | 1      | 0 |
| RRBBBPPPPP      | 1      | 0 |
| RRBBBNPPPP      | 1      | 0 |
| RRR             | 3      | 0 |
| RRRP            | 5      | 0 |
| RRRPP           | 11     | 0 |
| RRRPPP          | 6      | 0 |
| RRRPPPP         | 5      | 0 |
| RRRPPPPP        | 6      | 0 |
| RRRPPPPPP       | 1      | 0 |
| RRRN            | 2      | 0 |
| RRRNP           | 2      | 0 |
| RRRNPP          | 4      | 0 |
| RRRNPPP         | 3      | 0 |
| RRRNPPPP        | 1      | 0 |
| RRRNPPPPP       | 2      | 0 |
| RRRNPPPPPP      | 1      | 0 |
| RRRBP           | 6      | 0 |
| RRRBPP          | 2      | 0 |
| RRRBPPP         | 4      | 0 |
| RRRBPPPP        | 5      | 0 |
| RRRBPPPPP       | 1      | 0 |
| RRRBNP          | 1      | 0 |
| RRRBNPPPP       | 1      | 0 |
| RRRBNPPPPP      | 1      | 0 |
| RRRBNPPPPPP     | 1      | 0 |
| RRRBNNPPPPPP    | 1      | 0 |
| RRRBBPPPPP      | 1      | 0 |
| RRRBBPPPPPP     | 1      | 0 |
| RRRBBNNPPPPPPP  | 1      | 0 |
| RRRRP           | 1      | 0 |
| RRRRPP          | 1      | 0 |
| Q               | 15540  | 0 |
| QP              | 51155  | 0 |
| QPP             | 68458  | 1 |
| QPPP            | 77494  | 1 |
| QPPPP           | 64778  | 0 |
| QPPPPP          | 44757  | 0 |
| QPPPPPP         | 23088  | 0 |
| QPPPPPPP        | 6359   | 0 |
| QPPPPPPPP       | 270    | 0 |
| QN              | 2746   | 0 |
| QNP             | 6954   | 0 |
| QNPP            | 13793  | 0 |
| QNPPP           | 20458  | 0 |
| QNPPPP          | 25540  | 0 |
| QNPPPPP         | 24736  | 0 |
| QNPPPPPP        | 18145  | 0 |
| QNPPPPPPP       | 6473   | 0 |
| QNPPPPPPPP      | 245    | 0 |
| QNN             | 61     | 0 |
| QNNP            | 296    | 0 |
| QNNPP           | 697    | 0 |
| QNNPPP          | 1484   | 0 |
| QNNPPPP         | 2682   | 0 |
| QNNPPPPP        | 3733   | 0 |
| QNNPPPPPP       | 3487   | 0 |
| QNNPPPPPPP      | 2003   | 0 |
| QNNPPPPPPPP     | 50     | 0 |
| QNNNP           | 4      | 0 |
| QNNNPP          | 5      | 0 |
| QNNNPPPP        | 1      | 0 |
| QNNNNP          | 1      | 0 |
| QB              | 4408   | 0 |
| QBP             | 11633  | 0 |
| QBPP            | 21929  | 0 |
| QBPPP           | 34278  | 0 |
| QBPPPP          | 40675  | 0 |
| QBPPPPP         | 36856  | 0 |
| QBPPPPPP        | 24905  | 0 |
| QBPPPPPPP       | 8455   | 0 |
| QBPPPPPPPP      | 396    | 0 |
| QBN             | 755    | 0 |
| QBNP            | 1421   | 0 |
| QBNPP           | 4293   | 0 |
| QBNPPP          | 8699   | 0 |
| QBNPPPP         | 14521  | 0 |
| QBNPPPPP        | 19293  | 0 |
| QBNPPPPPP       | 17503  | 0 |
| QBNPPPPPPP      | 7705   | 0 |
| QBNPPPPPPPP     | 208    | 0 |
| QBNN            | 13     | 0 |
| QBNNP           | 79     | 0 |
| QBNNPP          | 360    | 0 |
| QBNNPPP         | 899    | 0 |
| QBNNPPPP        | 1984   | 0 |
| QBNNPPPPP       | 3177   | 0 |
| QBNNPPPPPP      | 3491   | 0 |
| QBNNPPPPPPP     | 2358   | 0 |
| QBNNPPPPPPPP    | 43     | 0 |
| QBNNNP          | 1      | 0 |
| QBB             | 165    | 0 |
| QBBP            | 500    | 0 |
| QBBPP           | 1001   | 0 |
| QBBPPP          | 2524   | 0 |
| QBBPPPP         | 4818   | 0 |
| QBBPPPPP        | 6532   | 0 |
| QBBPPPPPP       | 6361   | 0 |
| QBBPPPPPPP      | 2767   | 0 |
| QBBPPPPPPPP     | 121    | 0 |
| QBBN            | 44     | 0 |
| QBBNP           | 90     | 0 |
| QBBNPP          | 301    | 0 |
| QBBNPPP         | 1125   | 0 |
| QBBNPPPP        | 2591   | 0 |
| QBBNPPPPP       | 4205   | 0 |
| QBBNPPPPPP      | 4813   | 0 |
| QBBNPPPPPPP     | 2669   | 0 |
| QBBNPPPPPPPP    | 38     | 0 |
| QBBNN           | 1      | 0 |
| QBBNNP          | 9      | 0 |
| QBBNNPP         | 63     | 0 |
| QBBNNPPP        | 224    | 0 |
| QBBNNPPPP       | 535    | 0 |
| QBBNNPPPPP      | 978    | 0 |
| QBBNNPPPPPP     | 1352   | 0 |
| QBBNNPPPPPPP    | 1097   | 0 |
| QBBNNPPPPPPPP   | 13     | 0 |
| QBBBPP          | 2      | 0 |
| QBBBNP          | 1      | 0 |
| QR              | 8135   | 0 |
| QRP             | 19397  | 0 |
| QRPP            | 33772  | 0 |
| QRPPP           | 56460  | 0 |
| QRPPPP          | 77376  | 1 |
| QRPPPPP         | 83151  | 1 |
| QRPPPPPP        | 60315  | 0 |
| QRPPPPPPP       | 20569  | 0 |
| QRPPPPPPPP      | 1368   | 0 |
| QRN             | 610    | 0 |
| QRNP            | 2750   | 0 |
| QRNPP           | 8114   | 0 |
| QRNPPP          | 22551  | 0 |
| QRNPPPP         | 45224  | 0 |
| QRNPPPPP        | 66331  | 0 |
| QRNPPPPPP       | 62510  | 0 |
| QRNPPPPPPP      | 25129  | 0 |
| QRNPPPPPPPP     | 1897   | 0 |
| QRNN            | 21     | 0 |
| QRNNP           | 151    | 0 |
| QRNNPP          | 787    | 0 |
| QRNNPPP         | 2934   | 0 |
| QRNNPPPP        | 7800   | 0 |
| QRNNPPPPP       | 14529  | 0 |
| QRNNPPPPPP      | 16596  | 0 |
| QRNNPPPPPPP     | 8776   | 0 |
| QRNNPPPPPPPP    | 848    | 0 |
| QRNNN           | 1      | 0 |
| QRNNNP          | 1      | 0 |
| QRNNNPPPPP      | 2      | 0 |
| QRB             | 1022   | 0 |
| QRBP            | 4388   | 0 |
| QRBPP           | 12945  | 0 |
| QRBPPP          | 34504  | 0 |
| QRBPPPP         | 69752  | 1 |
| QRBPPPPP        | 97024  | 1 |
| QRBPPPPPP       | 86071  | 1 |
| QRBPPPPPPP      | 33524  | 0 |
| QRBPPPPPPPP     | 2542   | 0 |
| QRBN            | 107    | 0 |
| QRBNP           | 951    | 0 |
| QRBNPP          | 4455   | 0 |
| QRBNPPP         | 16649  | 0 |
| QRBNPPPP        | 43838  | 0 |
| QRBNPPPPP       | 79109  | 1 |
| QRBNPPPPPP      | 88259  | 1 |
| QRBNPPPPPPP     | 42053  | 0 |
| QRBNPPPPPPPP    | 3639   | 0 |
| QRBNN           | 10     | 0 |
| QRBNNP          | 90     | 0 |
| QRBNNPP         | 631    | 0 |
| QRBNNPPP        | 2988   | 0 |
| QRBNNPPPP       | 9621   | 0 |
| QRBNNPPPPP      | 20764  | 0 |
| QRBNNPPPPPP     | 28260  | 0 |
| QRBNNPPPPPPP    | 17215  | 0 |
| QRBNNPPPPPPPP   | 1735   | 0 |
| QRBNNNPP        | 1      | 0 |
| QRBNNNPPP       | 2      | 0 |
| QRBNNNPPPP      | 1      | 0 |
| QRBNNNPPPPP     | 1      | 0 |
| QRBB            | 58     | 0 |
| QRBBP           | 361    | 0 |
| QRBBPP          | 1403   | 0 |
| QRBBPPP         | 5452   | 0 |
| QRBBPPPP        | 15278  | 0 |
| QRBBPPPPP       | 27067  | 0 |
| QRBBPPPPPP      | 30000  | 0 |
| QRBBPPPPPPP     | 13859  | 0 |
| QRBBPPPPPPPP    | 1160   | 0 |
| QRBBN           | 12     | 0 |
| QRBBNP          | 108    | 0 |
| QRBBNPP         | 715    | 0 |
| QRBBNPPP        | 3966   | 0 |
| QRBBNPPPP       | 12792  | 0 |
| QRBBNPPPPP      | 28466  | 0 |
| QRBBNPPPPPP     | 38009  | 0 |
| QRBBNPPPPPPP    | 20590  | 0 |
| QRBBNPPPPPPPP   | 1634   | 0 |
| QRBBNN          | 1      | 0 |
| QRBBNNP         | 17     | 0 |
| QRBBNNPP        | 167    | 0 |
| QRBBNNPPP       | 920    | 0 |
| QRBBNNPPPP      | 3767   | 0 |
| QRBBNNPPPPP     | 10300  | 0 |
| QRBBNNPPPPPP    | 16430  | 0 |
| QRBBNNPPPPPPP   | 11821  | 0 |
| QRBBNNPPPPPPPP  | 895    | 0 |
| QRBBNNNPP       | 1      | 0 |
| QRBBNNNPPPP     | 2      | 0 |
| QRBBBPPP        | 1      | 0 |
| QRBBBNPPP       | 1      | 0 |
| QRR             | 535    | 0 |
| QRRP            | 1915   | 0 |
| QRRPP           | 7284   | 0 |
| QRRPPP          | 22398  | 0 |
| QRRPPPP         | 49281  | 0 |
| QRRPPPPP        | 81831  | 1 |
| QRRPPPPPP       | 93493  | 1 |
| QRRPPPPPPP      | 58578  | 0 |
| QRRPPPPPPPP     | 9976   | 0 |
| QRRN            | 162    | 0 |
| QRRNP           | 581    | 0 |
| QRRNPP          | 3045   | 0 |
| QRRNPPP         | 13209  | 0 |
| QRRNPPPP        | 39899  | 0 |
| QRRNPPPPP       | 91339  | 1 |
| QRRNPPPPPP      | 140852 | 2 |
| QRRNPPPPPPP     | 115463 | 1 |
| QRRNPPPPPPPP    | 25549  | 0 |
| QRRNN           | 5      | 0 |
| QRRNNP          | 50     | 0 |
| QRRNNPP         | 376    | 0 |
| QRRNNPPP        | 2262   | 0 |
| QRRNNPPPP       | 9353   | 0 |
| QRRNNPPPPP      | 27216  | 0 |
| QRRNNPPPPPP     | 54708  | 0 |
| QRRNNPPPPPPP    | 62012  | 0 |
| QRRNNPPPPPPPP   | 19238  | 0 |
| QRRNNNPP        | 1      | 0 |
| QRRNNNNPPP      | 1      | 0 |
| QRRB            | 117    | 0 |
| QRRBP           | 861    | 0 |
| QRRBPP          | 4531   | 0 |
| QRRBPPP         | 19234  | 0 |
| QRRBPPPP        | 58562  | 0 |
| QRRBPPPPP       | 130154 | 1 |
| QRRBPPPPPP      | 192878 | 2 |
| QRRBPPPPPPP     | 151621 | 2 |
| QRRBPPPPPPPP    | 30336  | 0 |
| QRRBN           | 28     | 0 |
| QRRBNP          | 287    | 0 |
| QRRBNPP         | 2378   | 0 |
| QRRBNPPP        | 12972  | 0 |
| QRRBNPPPP       | 51618  | 0 |
| QRRBNPPPPP      | 152706 | 2 |
| QRRBNPPPPPP     | 308837 | 4 |
| QRRBNPPPPPPP    | 323797 | 4 |
| QRRBNPPPPPPPP   | 81017  | 1 |
| QRRBNN          | 2      | 0 |
| QRRBNNP         | 40     | 0 |
| QRRBNNPP        | 461    | 0 |
| QRRBNNPPP       | 2993   | 0 |
| QRRBNNPPPP      | 14792  | 0 |
| QRRBNNPPPPP     | 55554  | 0 |
| QRRBNNPPPPPP    | 145142 | 2 |
| QRRBNNPPPPPPP   | 205470 | 3 |
| QRRBNNPPPPPPPP  | 68644  | 1 |
| QRRBNNNNPPP     | 1      | 0 |
| QRRBB           | 10     | 0 |
| QRRBBP          | 91     | 0 |
| QRRBBPP         | 793    | 0 |
| QRRBBPPP        | 4389   | 0 |
| QRRBBPPPP       | 17515  | 0 |
| QRRBBPPPPP      | 51745  | 0 |
| QRRBBPPPPPP     | 102524 | 1 |
| QRRBBPPPPPPP    | 100268 | 1 |
| QRRBBPPPPPPPP   | 23359  | 0 |
| QRRBBN          | 1      | 0 |
| QRRBBNP         | 54     | 0 |
| QRRBBNPP        | 597    | 0 |
| QRRBBNPPP       | 3975   | 0 |
| QRRBBNPPPP      | 20278  | 0 |
| QRRBBNPPPPP     | 77645  | 1 |
| QRRBBNPPPPPP    | 207543 | 3 |
| QRRBBNPPPPPPP   | 263394 | 3 |
| QRRBBNPPPPPPPP  | 71456  | 1 |
| QRRBBNN         | 1      | 0 |
| QRRBBNNP        | 12     | 0 |
| QRRBBNNPP       | 146    | 0 |
| QRRBBNNPPP      | 1270   | 0 |
| QRRBBNNPPPP     | 7744   | 0 |
| QRRBBNNPPPPP    | 35984  | 0 |
| QRRBBNNPPPPPP   | 123036 | 1 |
| QRRBBNNPPPPPPP  | 204689 | 3 |
| QRRBBNNPPPPPPPP | 81615  | 1 |
| QRRBBNNNPPPPPP  | 1      | 0 |
| QRRBBNNNPPPPPPP | 1      | 0 |
| QRRBBNNNNPPPPPP | 2      | 0 |
| QRRBBBPPP       | 1      | 0 |
| QRRBBBPPPPP     | 1      | 0 |
| QRRBBBNNPPPPPP  | 1      | 0 |
| QRRRP           | 1      | 0 |
| QRRRPP          | 2      | 0 |
| QRRRPPPP        | 2      | 0 |
| QRRRPPPPPPP     | 1      | 0 |
| QRRRNPPP        | 1      | 0 |
| QRRRBPP         | 1      | 0 |
| QRRRBPPPPP      | 2      | 0 |
| QRRRBPPPPPP     | 2      | 0 |
| QRRRBNPPPP      | 2      | 0 |
| QRRRBNPPPPPP    | 2      | 0 |
| QRRRBNNPPPP     | 2      | 0 |
| QRRRBBNPPPP     | 1      | 0 |
| QRRRBBNPPPPP    | 1      | 0 |
| QRRRBBNNPPPPPPP | 1      | 0 |
| QQ              | 779    | 0 |
| QQP             | 1562   | 0 |
| QQPP            | 1912   | 0 |
| QQPPP           | 1435   | 0 |
| QQPPPP          | 699    | 0 |
| QQPPPPP         | 304    | 0 |
| QQPPPPPP        | 57     | 0 |
| QQPPPPPPP       | 2      | 0 |
| QQN             | 76     | 0 |
| QQNP            | 201    | 0 |
| QQNPP           | 202    | 0 |
| QQNPPP          | 230    | 0 |
| QQNPPPP         | 141    | 0 |
| QQNPPPPP        | 53     | 0 |
| QQNPPPPPP       | 14     | 0 |
| QQNN            | 1      | 0 |
| QQNNP           | 1      | 0 |
| QQNNPP          | 9      | 0 |
| QQNNPPP         | 12     | 0 |
| QQNNPPPP        | 8      | 0 |
| QQNNPPPPP       | 1      | 0 |
| QQNNPPPPPP      | 1      | 0 |
| QQB             | 95     | 0 |
| QQBP            | 379    | 0 |
| QQBPP           | 452    | 0 |
| QQBPPP          | 353    | 0 |
| QQBPPPP         | 228    | 0 |
| QQBPPPPP        | 85     | 0 |
| QQBPPPPPP       | 32     | 0 |
| QQBPPPPPPP      | 5      | 0 |
| QQBN            | 7      | 0 |
| QQBNP           | 25     | 0 |
| QQBNPP          | 50     | 0 |
| QQBNPPP         | 36     | 0 |
| QQBNPPPP        | 38     | 0 |
| QQBNPPPPP       | 18     | 0 |
| QQBNPPPPPP      | 5      | 0 |
| QQBNPPPPPPP     | 2      | 0 |
| QQBNNPP         | 1      | 0 |
| QQBNNPPP        | 4      | 0 |
| QQBNNPPPP       | 3      | 0 |
| QQBNNPPPPP      | 1      | 0 |
| QQBB            | 2      | 0 |
| QQBBP           | 3      | 0 |
| QQBBPP          | 4      | 0 |
| QQBBPPP         | 29     | 0 |
| QQBBPPPP        | 19     | 0 |
| QQBBPPPPP       | 10     | 0 |
| QQBBPPPPPP      | 5      | 0 |
| QQBBPPPPPPP     | 1      | 0 |
| QQBBNP          | 3      | 0 |
| QQBBNPP         | 2      | 0 |
| QQBBNPPP        | 5      | 0 |
| QQBBNPPPP       | 3      | 0 |
| QQBBNPPPPP      | 3      | 0 |
| QQBBNNPPPP      | 1      | 0 |
| QQBBNNPPPPPP    | 1      | 0 |
| QQR             | 151    | 0 |
| QQRP            | 294    | 0 |
| QQRPP           | 421    | 0 |
| QQRPPP          | 450    | 0 |
| QQRPPPP         | 386    | 0 |
| QQRPPPPP        | 165    | 0 |
| QQRPPPPPP       | 51     | 0 |
| QQRPPPPPPP      | 3      | 0 |
| QQRN            | 11     | 0 |
| QQRNP           | 37     | 0 |
| QQRNPP          | 56     | 0 |
| QQRNPPP         | 81     | 0 |
| QQRNPPPP        | 66     | 0 |
| QQRNPPPPP       | 47     | 0 |
| QQRNPPPPPP      | 20     | 0 |
| QQRNPPPPPPP     | 2      | 0 |
| QQRNNPP         | 3      | 0 |
| QQRNNPPP        | 6      | 0 |
| QQRNNPPPP       | 12     | 0 |
| QQRNNPPPPP      | 11     | 0 |
| QQRNNPPPPPP     | 2      | 0 |
| QQRB            | 16     | 0 |
| QQRBP           | 53     | 0 |
| QQRBPP          | 123    | 0 |
| QQRBPPP         | 220    | 0 |
| QQRBPPPP        | 184    | 0 |
| QQRBPPPPP       | 106    | 0 |
| QQRBPPPPPP      | 28     | 0 |
| QQRBPPPPPPP     | 5      | 0 |
| QQRBN           | 1      | 0 |
| QQRBNP          | 2      | 0 |
| QQRBNPP         | 16     | 0 |
| QQRBNPPP        | 43     | 0 |
| QQRBNPPPP       | 66     | 0 |
| QQRBNPPPPP      | 50     | 0 |
| QQRBNPPPPPP     | 10     | 0 |
| QQRBNPPPPPPP    | 4      | 0 |
| QQRBNNP         | 1      | 0 |
| QQRBNNPPP       | 9      | 0 |
| QQRBNNPPPP      | 9      | 0 |
| QQRBNNPPPPP     | 9      | 0 |
| QQRBNNPPPPPP    | 8      | 0 |
| QQRBBP          | 3      | 0 |
| QQRBBPP         | 4      | 0 |
| QQRBBPPP        | 26     | 0 |
| QQRBBPPPP       | 23     | 0 |
| QQRBBPPPPP      | 9      | 0 |
| QQRBBPPPPPP     | 5      | 0 |
| QQRBBNP         | 1      | 0 |
| QQRBBNPP        | 2      | 0 |
| QQRBBNPPP       | 7      | 0 |
| QQRBBNPPPP      | 9      | 0 |
| QQRBBNPPPPP     | 14     | 0 |
| QQRBBNPPPPPP    | 6      | 0 |
| QQRBBNPPPPPPP   | 4      | 0 |
| QQRBBNNPPP      | 1      | 0 |
| QQRBBNNPPPP     | 3      | 0 |
| QQRBBNNPPPPP    | 3      | 0 |
| QQRBBNNPPPPPP   | 3      | 0 |
| QQRBBNNPPPPPPP  | 1      | 0 |
| QQRR            | 9      | 0 |
| QQRRP           | 21     | 0 |
| QQRRPP          | 70     | 0 |
| QQRRPPP         | 118    | 0 |
| QQRRPPPP        | 130    | 0 |
| QQRRPPPPP       | 101    | 0 |
| QQRRPPPPPP      | 40     | 0 |
| QQRRPPPPPPP     | 9      | 0 |
| QQRRNP          | 6      | 0 |
| QQRRNPP         | 17     | 0 |
| QQRRNPPP        | 29     | 0 |
| QQRRNPPPP       | 56     | 0 |
| QQRRNPPPPP      | 46     | 0 |
| QQRRNPPPPPP     | 23     | 0 |
| QQRRNPPPPPPP    | 6      | 0 |
| QQRRNNPP        | 3      | 0 |
| QQRRNNPPP       | 2      | 0 |
| QQRRNNPPPP      | 8      | 0 |
| QQRRNNPPPPP     | 7      | 0 |
| QQRRNNPPPPPP    | 6      | 0 |
| QQRRNNPPPPPPP   | 3      | 0 |
| QQRRBP          | 10     | 0 |
| QQRRBPP         | 35     | 0 |
| QQRRBPPP        | 72     | 0 |
| QQRRBPPPP       | 68     | 0 |
| QQRRBPPPPP      | 81     | 0 |
| QQRRBPPPPPP     | 36     | 0 |
| QQRRBPPPPPPP    | 6      | 0 |
| QQRRBNPP        | 6      | 0 |
| QQRRBNPPP       | 18     | 0 |
| QQRRBNPPPP      | 47     | 0 |
| QQRRBNPPPPP     | 71     | 0 |
| QQRRBNPPPPPP    | 45     | 0 |
| QQRRBNPPPPPPP   | 17     | 0 |
| QQRRBNNPP       | 1      | 0 |
| QQRRBNNPPP      | 7      | 0 |
| QQRRBNNPPPP     | 2      | 0 |
| QQRRBNNPPPPP    | 10     | 0 |
| QQRRBNNPPPPPP   | 24     | 0 |
| QQRRBNNPPPPPPP  | 11     | 0 |
| QQRRBBPPP       | 6      | 0 |
| QQRRBBPPPP      | 15     | 0 |
| QQRRBBPPPPP     | 12     | 0 |
| QQRRBBPPPPPP    | 14     | 0 |
| QQRRBBPPPPPPP   | 3      | 0 |
| QQRRBBNPP       | 1      | 0 |
| QQRRBBNPPP      | 3      | 0 |
| QQRRBBNPPPP     | 14     | 0 |
| QQRRBBNPPPPP    | 18     | 0 |
| QQRRBBNPPPPPP   | 19     | 0 |
| QQRRBBNPPPPPPP  | 8      | 0 |
| QQRRBBNNPPP     | 3      | 0 |
| QQRRBBNNPPPP    | 4      | 0 |
| QQRRBBNNPPPPP   | 4      | 0 |
| QQRRBBNNPPPPPP  | 7      | 0 |
| QQRRBBNNPPPPPPP | 13     | 0 |
| QQRRRPPPP       | 1      | 0 |
| QQRRRBNNPPP     | 1      | 0 |
| QQQ             | 1      | 0 |
| QQQP            | 11     | 0 |
| QQQPP           | 11     | 0 |
| QQQPPP          | 9      | 0 |
| QQQPPPP         | 4      | 0 |
| QQQNP           | 2      | 0 |
| QQQNPP          | 1      | 0 |
| QQQBP           | 2      | 0 |
| QQQBPP          | 4      | 0 |
| QQQBPPPP        | 1      | 0 |
| QQQBPPPPP       | 1      | 0 |
| QQQBBPPP        | 1      | 0 |
| QQQRP           | 6      | 0 |
| QQQRPP          | 2      | 0 |
| QQQRPPP         | 4      | 0 |
| QQQRNP          | 3      | 0 |
| QQQRNPP         | 2      | 0 |
| QQQRNPPP        | 1      | 0 |
| QQQRNPPPP       | 1      | 0 |
| QQQRBPPP        | 1      | 0 |
| QQQRBPPPP       | 1      | 0 |
| QQQRBNP         | 1      | 0 |
| QQQRBNPP        | 3      | 0 |
| QQQRBNPPP       | 2      | 0 |
| QQQRRPP         | 1      | 0 |
| QQQRRPPP        | 1      | 0 |
| QQQRRBPPP       | 2      | 0 |
| QQQRRBPPPP      | 1      | 0 |
| QQQRRBBNNPPPP   | 1      | 0 |
| QQQRRBBNNPPPPPP | 1      | 0 |
| QQQQP           | 1      | 0 |
| QQQQPP          | 1      | 0 |
| QQQQRP          | 2      | 0 |
| QQQQRPP         | 2      | 0 |
| QQQQQP          | 1      | 0 |
| QQQQQNPP        | 1      | 0 |
| QQQQQRP         | 1      | 0 |
| QQQQQRNP        | 1      | 0 |
| QQQQQRRBNP      | 1      | 0 |
| QQQQQRRBBNNPP   | 1      | 0 |
| QQQQQQRNPP      | 1      | 0 |
| QQQQQQQRP       | 1      | 0 |


K adjusted to: 1.4500000000000002

## new optimization round

### Optimizing with step 10

Error at start: 0.07433991912963334

| Duration | Round | Step | Params adjustments | Adj total | Curr Error | Overall AdjPerSecond | AdjPerSecond |
|----------|-------|------|--------------------|-----------|------------|----------------------|--------------|
| 0:03:52,853 | 100   | 10   | 0                  | 0         | 0.07433991912963334 | 0.0                  | 0.0          |
| 0:08:36,454 | 200   | 10   | 1                  | 1         | 0.07433961985112417 | 0.001937984496124031 | 0.0035335689045936395 |
| 0:13:44,325 | 300   | 10   | 5                  | 6         | 0.07433914809120408 | 0.007281553398058253 | 0.016286644951140065  |
| 0:17:35,229 | 400   | 10   | 13                 | 19        | 0.07432735419360934 | 0.01800947867298578  | 0.05652173913043478   |
| 0:21:40,943 | 500   | 10   | 1                  | 20        | 0.07432730381698342 | 0.015384615384615385 | 0.004081632653061225  |
| 0:25:53,740 | 600   | 10   | 2                  | 22        | 0.07432719507578876 | 0.014166130070830651 | 0.007936507936507936  |
| 0:29:53,857 | 700   | 10   | 3                  | 25        | 0.07432713650775262 | 0.013943112102621304 | 0.0125                |
| 0:33:44,450 | 800   | 10   | 2                  | 27        | 0.07432708540829855 | 0.0133399209486166   | 0.008695652173913044  |
| 0:39:05,367 | 900   | 10   | 2                  | 29        | 0.07432703419430864 | 0.012366737739872069 | 0.00625               |
# Tuning Options

| Parameter                    | Value                        |
|------------------------------|------------------------------|
| adjustK                      | true                         |
| delta                        | 1.0E-8                       |
| evalParamSet                 | CURRENT                      |
| geneticParams                | null                         |
| inputFiles                   | [C:\projekte\cygwin_home\mla\jackyChessDockerTesting\tuningdata\lichess-big3-resolved.book]|
| multiThreading               | true                         |
| name                         | lichess test2                |
| optimizeRecalcOnlyDependendFens| false                        |
| outputdir                    | null                         |
| progressUpdatesInMinutes     | 0                            |
| removeDuplicateFens          | true                         |
| resetParametersBeforeTuning  | false                        |
| shuffleTuningParameter       | false                        |
| stepGranularity              | [10, 5, 3, 1]                |
| threadCount                  | 5                            |
| tuneAdjustments              | true                         |
| tuneAdjustmentsFactors       | false                        |
| tuneComplexity               | true                         |
| tuneKingAttack               | true                         |
| tuneKingSafety               | true                         |
| tuneMaterial                 | false                        |
| tuneMobility                 | true                         |
| tuneMobilityTropism          | true                         |
| tunePassedPawnEval           | true                         |
| tunePawnEval                 | true                         |
| tunePositional               | true                         |
| tunePst                      | true                         |
| tuneThreats                  | true                         |


# Dataset Information

|                |         |
|----------------|---------|
| Num Fens       | 6819087 |
| MATE White     | 2801909 |
| MATE Black     | 2605257 |
| Draws          | 1411921 |
| Duplicate Fens | 0       |


## Number of fens by Game Phase

| Phase | Count  | %  |
|-------|--------|----|
| 0     | 593109 | 8  |
| 1     | 923895 | 13 |
| 2     | 736133 | 10 |
| 3     | 619828 | 9  |
| 4     | 561207 | 8  |
| 5     | 463708 | 6  |
| 6     | 437031 | 6  |
| 7     | 530586 | 7  |
| 8     | 654432 | 9  |
| 9     | 692383 | 10 |
| 10    | 606775 | 8  |


## Number of fens by having figures

| FigureType | Count   | %   |
|------------|---------|-----|
| Rook       | 5988879 | 87  |
| Knight     | 4456890 | 65  |
| Bishop     | 4966199 | 72  |
| Queen      | 3817373 | 55  |
| Pawn       | 6819087 | 100 |


## Number of fens by Material

| Material        | Count  | % |
|-----------------|--------|---|
|                 | 33454  | 0 |
| P               | 76606  | 1 |
| PP              | 122515 | 1 |
| PPP             | 132276 | 1 |
| PPPP            | 103981 | 1 |
| PPPPP           | 68109  | 0 |
| PPPPPP          | 32877  | 0 |
| PPPPPPP         | 8662   | 0 |
| PPPPPPPP        | 323    | 0 |
| N               | 12616  | 0 |
| NP              | 33814  | 0 |
| NPP             | 55129  | 0 |
| NPPP            | 68974  | 1 |
| NPPPP           | 64043  | 0 |
| NPPPPP          | 49168  | 0 |
| NPPPPPP         | 28688  | 0 |
| NPPPPPPP        | 9006   | 0 |
| NPPPPPPPP       | 209    | 0 |
| NN              | 530    | 0 |
| NNP             | 1841   | 0 |
| NNPP            | 3202   | 0 |
| NNPPP           | 5025   | 0 |
| NNPPPP          | 5922   | 0 |
| NNPPPPP         | 6029   | 0 |
| NNPPPPPP        | 4408   | 0 |
| NNPPPPPPP       | 2177   | 0 |
| NNPPPPPPPP      | 20     | 0 |
| NNN             | 3      | 0 |
| NNNP            | 4      | 0 |
| NNNPP           | 13     | 0 |
| NNNPPP          | 4      | 0 |
| NNNPPPP         | 3      | 0 |
| NNNPPPPP        | 1      | 0 |
| NNNNPPP         | 2      | 0 |
| B               | 16616  | 0 |
| BP              | 51273  | 0 |
| BPP             | 87515  | 1 |
| BPPP            | 103420 | 1 |
| BPPPP           | 100360 | 1 |
| BPPPPP          | 76023  | 1 |
| BPPPPPP         | 42486  | 0 |
| BPPPPPPP        | 11779  | 0 |
| BPPPPPPPP       | 401    | 0 |
| BN              | 2812   | 0 |
| BNP             | 9913   | 0 |
| BNPP            | 19555  | 0 |
| BNPPP           | 27490  | 0 |
| BNPPPP          | 32087  | 0 |
| BNPPPPP         | 29573  | 0 |
| BNPPPPPP        | 21544  | 0 |
| BNPPPPPPP       | 8004   | 0 |
| BNPPPPPPPP      | 155    | 0 |
| BNN             | 49     | 0 |
| BNNP            | 275    | 0 |
| BNNPP           | 873    | 0 |
| BNNPPP          | 1789   | 0 |
| BNNPPPP         | 2884   | 0 |
| BNNPPPPP        | 3467   | 0 |
| BNNPPPPPP       | 3189   | 0 |
| BNNPPPPPPP      | 1840   | 0 |
| BNNPPPPPPPP     | 14     | 0 |
| BNNNP           | 1      | 0 |
| BNNNPPPP        | 1      | 0 |
| BNNNNNP         | 1      | 0 |
| BNNNNNPP        | 1      | 0 |
| BB              | 1971   | 0 |
| BBP             | 4075   | 0 |
| BBPP            | 7041   | 0 |
| BBPPP           | 11143  | 0 |
| BBPPPP          | 11437  | 0 |
| BBPPPPP         | 11128  | 0 |
| BBPPPPPP        | 8216   | 0 |
| BBPPPPPPP       | 2873   | 0 |
| BBPPPPPPPP      | 131    | 0 |
| BBN             | 93     | 0 |
| BBNP            | 299    | 0 |
| BBNPP           | 1039   | 0 |
| BBNPPP          | 2299   | 0 |
| BBNPPPP         | 3735   | 0 |
| BBNPPPPP        | 4460   | 0 |
| BBNPPPPPP       | 4140   | 0 |
| BBNPPPPPPP      | 2009   | 0 |
| BBNPPPPPPPP     | 23     | 0 |
| BBNNP           | 20     | 0 |
| BBNNPP          | 79     | 0 |
| BBNNPPP         | 301    | 0 |
| BBNNPPPP        | 636    | 0 |
| BBNNPPPPP       | 738    | 0 |
| BBNNPPPPPP      | 880    | 0 |
| BBNNPPPPPPP     | 633    | 0 |
| BBNNPPPPPPPP    | 5      | 0 |
| BBNNNP          | 3      | 0 |
| BBBP            | 4      | 0 |
| BBBPP           | 4      | 0 |
| BBBPPP          | 3      | 0 |
| BBBPPPP         | 4      | 0 |
| BBBNP           | 1      | 0 |
| BBBNPPP         | 1      | 0 |
| BBBNNP          | 1      | 0 |
| BBBBP           | 3      | 0 |
| BBBBPPP         | 2      | 0 |
| BBBBPPPP        | 1      | 0 |
| BBBBNPP         | 1      | 0 |
| BBBBBPPP        | 1      | 0 |
| BBBBBBPP        | 1      | 0 |
| R               | 70970  | 1 |
| RP              | 192547 | 2 |
| RPP             | 252064 | 3 |
| RPPP            | 268348 | 3 |
| RPPPP           | 221962 | 3 |
| RPPPPP          | 158007 | 2 |
| RPPPPPP         | 84534  | 1 |
| RPPPPPPP        | 22633  | 0 |
| RPPPPPPPP       | 1060   | 0 |
| RN              | 14423  | 0 |
| RNP             | 32920  | 0 |
| RNPP            | 60144  | 0 |
| RNPPP           | 96059  | 1 |
| RNPPPP          | 107436 | 1 |
| RNPPPPP         | 104563 | 1 |
| RNPPPPPP        | 69062  | 1 |
| RNPPPPPPP       | 20732  | 0 |
| RNPPPPPPPP      | 1028   | 0 |
| RNN             | 203    | 0 |
| RNNP            | 1056   | 0 |
| RNNPP           | 3050   | 0 |
| RNNPPP          | 6878   | 0 |
| RNNPPPP         | 11631  | 0 |
| RNNPPPPP        | 15220  | 0 |
| RNNPPPPPP       | 12681  | 0 |
| RNNPPPPPPP      | 5002   | 0 |
| RNNPPPPPPPP     | 254    | 0 |
| RNNN            | 1      | 0 |
| RNNNP           | 4      | 0 |
| RNNNPP          | 1      | 0 |
| RNNNPPP         | 5      | 0 |
| RNNNPPPP        | 2      | 0 |
| RNNNPPPPP       | 1      | 0 |
| RNNNNP          | 1      | 0 |
| RNNNNPP         | 2      | 0 |
| RB              | 15708  | 0 |
| RBP             | 44306  | 0 |
| RBPP            | 82180  | 1 |
| RBPPP           | 125978 | 1 |
| RBPPPP          | 151618 | 2 |
| RBPPPPP         | 144476 | 2 |
| RBPPPPPP        | 94000  | 1 |
| RBPPPPPPP       | 27932  | 0 |
| RBPPPPPPPP      | 1485   | 0 |
| RBN             | 1368   | 0 |
| RBNP            | 5362   | 0 |
| RBNPP           | 15999  | 0 |
| RBNPPP          | 36004  | 0 |
| RBNPPPP         | 62303  | 0 |
| RBNPPPPP        | 79905  | 1 |
| RBNPPPPPP       | 64804  | 0 |
| RBNPPPPPPP      | 23491  | 0 |
| RBNPPPPPPPP     | 1280   | 0 |
| RBNN            | 34     | 0 |
| RBNNP           | 233    | 0 |
| RBNNPP          | 1117   | 0 |
| RBNNPPP         | 3910   | 0 |
| RBNNPPPP        | 8980   | 0 |
| RBNNPPPPP       | 14328  | 0 |
| RBNNPPPPPP      | 14249  | 0 |
| RBNNPPPPPPP     | 6298   | 0 |
| RBNNPPPPPPPP    | 410    | 0 |
| RBNNNP          | 6      | 0 |
| RBNNNPP         | 2      | 0 |
| RBNNNPPP        | 3      | 0 |
| RBNNNPPPP       | 2      | 0 |
| RBNNNPPPPP      | 1      | 0 |
| RBNNNNPP        | 1      | 0 |
| RBNNNNPPP       | 1      | 0 |
| RBB             | 763    | 0 |
| RBBP            | 1433   | 0 |
| RBBPP           | 4738   | 0 |
| RBBPPP          | 11059  | 0 |
| RBBPPPP         | 20282  | 0 |
| RBBPPPPP        | 26105  | 0 |
| RBBPPPPPP       | 21218  | 0 |
| RBBPPPPPPP      | 7519   | 0 |
| RBBPPPPPPPP     | 453    | 0 |
| RBBN            | 40     | 0 |
| RBBNP           | 304    | 0 |
| RBBNPP          | 1287   | 0 |
| RBBNPPP         | 4610   | 0 |
| RBBNPPPP        | 10988  | 0 |
| RBBNPPPPP       | 18234  | 0 |
| RBBNPPPPPP      | 17827  | 0 |
| RBBNPPPPPPP     | 7268   | 0 |
| RBBNPPPPPPPP    | 402    | 0 |
| RBBNN           | 2      | 0 |
| RBBNNP          | 39     | 0 |
| RBBNNPP         | 168    | 0 |
| RBBNNPPP        | 789    | 0 |
| RBBNNPPPP       | 2327   | 0 |
| RBBNNPPPPP      | 4686   | 0 |
| RBBNNPPPPPP     | 5215   | 0 |
| RBBNNPPPPPPP    | 2659   | 0 |
| RBBNNPPPPPPPP   | 150    | 0 |
| RBBNNNPPPP      | 1      | 0 |
| RBBNNNPPPPP     | 1      | 0 |
| RBBNNNPPPPPP    | 1      | 0 |
| RBBBP           | 1      | 0 |
| RBBBPP          | 1      | 0 |
| RBBBPPP         | 2      | 0 |
| RBBBNP          | 1      | 0 |
| RBBBNPP         | 2      | 0 |
| RBBBNPPP        | 1      | 0 |
| RBBBBP          | 2      | 0 |
| RBBBBPP         | 2      | 0 |
| RR              | 8637   | 0 |
| RRP             | 22399  | 0 |
| RRPP            | 43620  | 0 |
| RRPPP           | 71470  | 1 |
| RRPPPP          | 91824  | 1 |
| RRPPPPP         | 99571  | 1 |
| RRPPPPPP        | 78830  | 1 |
| RRPPPPPPP       | 35657  | 0 |
| RRPPPPPPPP      | 4122   | 0 |
| RRN             | 1368   | 0 |
| RRNP            | 4107   | 0 |
| RRNPP           | 11489  | 0 |
| RRNPPP          | 27864  | 0 |
| RRNPPPP         | 53679  | 0 |
| RRNPPPPP        | 83375  | 1 |
| RRNPPPPPP       | 88799  | 1 |
| RRNPPPPPPP      | 50063  | 0 |
| RRNPPPPPPPP     | 7135   | 0 |
| RRNN            | 35     | 0 |
| RRNNP           | 186    | 0 |
| RRNNPP          | 841    | 0 |
| RRNNPPP         | 3292   | 0 |
| RRNNPPPP        | 8797   | 0 |
| RRNNPPPPP       | 17939  | 0 |
| RRNNPPPPPP      | 24327  | 0 |
| RRNNPPPPPPP     | 17522  | 0 |
| RRNNPPPPPPPP    | 3029   | 0 |
| RRNNNP          | 1      | 0 |
| RRNNNPP         | 1      | 0 |
| RRNNNPPPP       | 2      | 0 |
| RRNNNPPPPPP     | 1      | 0 |
| RRNNNNPP        | 1      | 0 |
| RRB             | 1118   | 0 |
| RRBP            | 5361   | 0 |
| RRBPP           | 15376  | 0 |
| RRBPPP          | 38038  | 0 |
| RRBPPPP         | 74578  | 1 |
| RRBPPPPP        | 115257 | 1 |
| RRBPPPPPP       | 120731 | 1 |
| RRBPPPPPPP      | 65672  | 0 |
| RRBPPPPPPPP     | 8391   | 0 |
| RRBN            | 392    | 0 |
| RRBNP           | 1445   | 0 |
| RRBNPP          | 4662   | 0 |
| RRBNPPP         | 16718  | 0 |
| RRBNPPPP        | 46521  | 0 |
| RRBNPPPPP       | 96948  | 1 |
| RRBNPPPPPP      | 136001 | 1 |
| RRBNPPPPPPP     | 93586  | 1 |
| RRBNPPPPPPPP    | 14421  | 0 |
| RRBNN           | 6      | 0 |
| RRBNNP          | 78     | 0 |
| RRBNNPP         | 503    | 0 |
| RRBNNPPP        | 2559   | 0 |
| RRBNNPPPP       | 9221   | 0 |
| RRBNNPPPPP      | 24702  | 0 |
| RRBNNPPPPPP     | 44521  | 0 |
| RRBNNPPPPPPP    | 37629  | 0 |
| RRBNNPPPPPPPP   | 6990   | 0 |
| RRBNNNPP        | 1      | 0 |
| RRBNNNPPP       | 1      | 0 |
| RRBNNNPPPP      | 2      | 0 |
| RRBB            | 221    | 0 |
| RRBBP           | 336    | 0 |
| RRBBPP          | 1394   | 0 |
| RRBBPPP         | 5139   | 0 |
| RRBBPPPP        | 14214  | 0 |
| RRBBPPPPP       | 30328  | 0 |
| RRBBPPPPPP      | 42966  | 0 |
| RRBBPPPPPPP     | 27875  | 0 |
| RRBBPPPPPPPP    | 3618   | 0 |
| RRBBN           | 11     | 0 |
| RRBBNP          | 90     | 0 |
| RRBBNPP         | 672    | 0 |
| RRBBNPPP        | 3055   | 0 |
| RRBBNPPPP       | 11591  | 0 |
| RRBBNPPPPP      | 31919  | 0 |
| RRBBNPPPPPP     | 58179  | 0 |
| RRBBNPPPPPPP    | 45224  | 0 |
| RRBBNPPPPPPPP   | 6508   | 0 |
| RRBBNN          | 1      | 0 |
| RRBBNNP         | 9      | 0 |
| RRBBNNPP        | 117    | 0 |
| RRBBNNPPP       | 673    | 0 |
| RRBBNNPPPP      | 3139   | 0 |
| RRBBNNPPPPP     | 10137  | 0 |
| RRBBNNPPPPPP    | 21645  | 0 |
| RRBBNNPPPPPPP   | 19947  | 0 |
| RRBBNNPPPPPPPP  | 3797   | 0 |
| RRBBNNNPPPPPP   | 1      | 0 |
| RRBBBPPPPP      | 1      | 0 |
| RRBBBNPPPP      | 1      | 0 |
| RRR             | 3      | 0 |
| RRRP            | 5      | 0 |
| RRRPP           | 11     | 0 |
| RRRPPP          | 6      | 0 |
| RRRPPPP         | 5      | 0 |
| RRRPPPPP        | 6      | 0 |
| RRRPPPPPP       | 1      | 0 |
| RRRN            | 2      | 0 |
| RRRNP           | 2      | 0 |
| RRRNPP          | 4      | 0 |
| RRRNPPP         | 3      | 0 |
| RRRNPPPP        | 1      | 0 |
| RRRNPPPPP       | 2      | 0 |
| RRRNPPPPPP      | 1      | 0 |
| RRRBP           | 6      | 0 |
| RRRBPP          | 2      | 0 |
| RRRBPPP         | 4      | 0 |
| RRRBPPPP        | 5      | 0 |
| RRRBPPPPP       | 1      | 0 |
| RRRBNP          | 1      | 0 |
| RRRBNPPPP       | 1      | 0 |
| RRRBNPPPPP      | 1      | 0 |
| RRRBNPPPPPP     | 1      | 0 |
| RRRBNNPPPPPP    | 1      | 0 |
| RRRBBPPPPP      | 1      | 0 |
| RRRBBPPPPPP     | 1      | 0 |
| RRRBBNNPPPPPPP  | 1      | 0 |
| RRRRP           | 1      | 0 |
| RRRRPP          | 1      | 0 |
| Q               | 15540  | 0 |
| QP              | 51155  | 0 |
| QPP             | 68458  | 1 |
| QPPP            | 77494  | 1 |
| QPPPP           | 64778  | 0 |
| QPPPPP          | 44757  | 0 |
| QPPPPPP         | 23088  | 0 |
| QPPPPPPP        | 6359   | 0 |
| QPPPPPPPP       | 270    | 0 |
| QN              | 2746   | 0 |
| QNP             | 6954   | 0 |
| QNPP            | 13793  | 0 |
| QNPPP           | 20458  | 0 |
| QNPPPP          | 25540  | 0 |
| QNPPPPP         | 24736  | 0 |
| QNPPPPPP        | 18145  | 0 |
| QNPPPPPPP       | 6473   | 0 |
| QNPPPPPPPP      | 245    | 0 |
| QNN             | 61     | 0 |
| QNNP            | 296    | 0 |
| QNNPP           | 697    | 0 |
| QNNPPP          | 1484   | 0 |
| QNNPPPP         | 2682   | 0 |
| QNNPPPPP        | 3733   | 0 |
| QNNPPPPPP       | 3487   | 0 |
| QNNPPPPPPP      | 2003   | 0 |
| QNNPPPPPPPP     | 50     | 0 |
| QNNNP           | 4      | 0 |
| QNNNPP          | 5      | 0 |
| QNNNPPPP        | 1      | 0 |
| QNNNNP          | 1      | 0 |
| QB              | 4408   | 0 |
| QBP             | 11633  | 0 |
| QBPP            | 21929  | 0 |
| QBPPP           | 34278  | 0 |
| QBPPPP          | 40675  | 0 |
| QBPPPPP         | 36856  | 0 |
| QBPPPPPP        | 24905  | 0 |
| QBPPPPPPP       | 8455   | 0 |
| QBPPPPPPPP      | 396    | 0 |
| QBN             | 755    | 0 |
| QBNP            | 1421   | 0 |
| QBNPP           | 4293   | 0 |
| QBNPPP          | 8699   | 0 |
| QBNPPPP         | 14521  | 0 |
| QBNPPPPP        | 19293  | 0 |
| QBNPPPPPP       | 17503  | 0 |
| QBNPPPPPPP      | 7705   | 0 |
| QBNPPPPPPPP     | 208    | 0 |
| QBNN            | 13     | 0 |
| QBNNP           | 79     | 0 |
| QBNNPP          | 360    | 0 |
| QBNNPPP         | 899    | 0 |
| QBNNPPPP        | 1984   | 0 |
| QBNNPPPPP       | 3177   | 0 |
| QBNNPPPPPP      | 3491   | 0 |
| QBNNPPPPPPP     | 2358   | 0 |
| QBNNPPPPPPPP    | 43     | 0 |
| QBNNNP          | 1      | 0 |
| QBB             | 165    | 0 |
| QBBP            | 500    | 0 |
| QBBPP           | 1001   | 0 |
| QBBPPP          | 2524   | 0 |
| QBBPPPP         | 4818   | 0 |
| QBBPPPPP        | 6532   | 0 |
| QBBPPPPPP       | 6361   | 0 |
| QBBPPPPPPP      | 2767   | 0 |
| QBBPPPPPPPP     | 121    | 0 |
| QBBN            | 44     | 0 |
| QBBNP           | 90     | 0 |
| QBBNPP          | 301    | 0 |
| QBBNPPP         | 1125   | 0 |
| QBBNPPPP        | 2591   | 0 |
| QBBNPPPPP       | 4205   | 0 |
| QBBNPPPPPP      | 4813   | 0 |
| QBBNPPPPPPP     | 2669   | 0 |
| QBBNPPPPPPPP    | 38     | 0 |
| QBBNN           | 1      | 0 |
| QBBNNP          | 9      | 0 |
| QBBNNPP         | 63     | 0 |
| QBBNNPPP        | 224    | 0 |
| QBBNNPPPP       | 535    | 0 |
| QBBNNPPPPP      | 978    | 0 |
| QBBNNPPPPPP     | 1352   | 0 |
| QBBNNPPPPPPP    | 1097   | 0 |
| QBBNNPPPPPPPP   | 13     | 0 |
| QBBBPP          | 2      | 0 |
| QBBBNP          | 1      | 0 |
| QR              | 8135   | 0 |
| QRP             | 19397  | 0 |
| QRPP            | 33772  | 0 |
| QRPPP           | 56460  | 0 |
| QRPPPP          | 77376  | 1 |
| QRPPPPP         | 83151  | 1 |
| QRPPPPPP        | 60315  | 0 |
| QRPPPPPPP       | 20569  | 0 |
| QRPPPPPPPP      | 1368   | 0 |
| QRN             | 610    | 0 |
| QRNP            | 2750   | 0 |
| QRNPP           | 8114   | 0 |
| QRNPPP          | 22551  | 0 |
| QRNPPPP         | 45224  | 0 |
| QRNPPPPP        | 66331  | 0 |
| QRNPPPPPP       | 62510  | 0 |
| QRNPPPPPPP      | 25129  | 0 |
| QRNPPPPPPPP     | 1897   | 0 |
| QRNN            | 21     | 0 |
| QRNNP           | 151    | 0 |
| QRNNPP          | 787    | 0 |
| QRNNPPP         | 2934   | 0 |
| QRNNPPPP        | 7800   | 0 |
| QRNNPPPPP       | 14529  | 0 |
| QRNNPPPPPP      | 16596  | 0 |
| QRNNPPPPPPP     | 8776   | 0 |
| QRNNPPPPPPPP    | 848    | 0 |
| QRNNN           | 1      | 0 |
| QRNNNP          | 1      | 0 |
| QRNNNPPPPP      | 2      | 0 |
| QRB             | 1022   | 0 |
| QRBP            | 4388   | 0 |
| QRBPP           | 12945  | 0 |
| QRBPPP          | 34504  | 0 |
| QRBPPPP         | 69752  | 1 |
| QRBPPPPP        | 97024  | 1 |
| QRBPPPPPP       | 86071  | 1 |
| QRBPPPPPPP      | 33524  | 0 |
| QRBPPPPPPPP     | 2542   | 0 |
| QRBN            | 107    | 0 |
| QRBNP           | 951    | 0 |
| QRBNPP          | 4455   | 0 |
| QRBNPPP         | 16649  | 0 |
| QRBNPPPP        | 43838  | 0 |
| QRBNPPPPP       | 79109  | 1 |
| QRBNPPPPPP      | 88259  | 1 |
| QRBNPPPPPPP     | 42053  | 0 |
| QRBNPPPPPPPP    | 3639   | 0 |
| QRBNN           | 10     | 0 |
| QRBNNP          | 90     | 0 |
| QRBNNPP         | 631    | 0 |
| QRBNNPPP        | 2988   | 0 |
| QRBNNPPPP       | 9621   | 0 |
| QRBNNPPPPP      | 20764  | 0 |
| QRBNNPPPPPP     | 28260  | 0 |
| QRBNNPPPPPPP    | 17215  | 0 |
| QRBNNPPPPPPPP   | 1735   | 0 |
| QRBNNNPP        | 1      | 0 |
| QRBNNNPPP       | 2      | 0 |
| QRBNNNPPPP      | 1      | 0 |
| QRBNNNPPPPP     | 1      | 0 |
| QRBB            | 58     | 0 |
| QRBBP           | 361    | 0 |
| QRBBPP          | 1403   | 0 |
| QRBBPPP         | 5452   | 0 |
| QRBBPPPP        | 15278  | 0 |
| QRBBPPPPP       | 27067  | 0 |
| QRBBPPPPPP      | 30000  | 0 |
| QRBBPPPPPPP     | 13859  | 0 |
| QRBBPPPPPPPP    | 1160   | 0 |
| QRBBN           | 12     | 0 |
| QRBBNP          | 108    | 0 |
| QRBBNPP         | 715    | 0 |
| QRBBNPPP        | 3966   | 0 |
| QRBBNPPPP       | 12792  | 0 |
| QRBBNPPPPP      | 28466  | 0 |
| QRBBNPPPPPP     | 38009  | 0 |
| QRBBNPPPPPPP    | 20590  | 0 |
| QRBBNPPPPPPPP   | 1634   | 0 |
| QRBBNN          | 1      | 0 |
| QRBBNNP         | 17     | 0 |
| QRBBNNPP        | 167    | 0 |
| QRBBNNPPP       | 920    | 0 |
| QRBBNNPPPP      | 3767   | 0 |
| QRBBNNPPPPP     | 10300  | 0 |
| QRBBNNPPPPPP    | 16430  | 0 |
| QRBBNNPPPPPPP   | 11821  | 0 |
| QRBBNNPPPPPPPP  | 895    | 0 |
| QRBBNNNPP       | 1      | 0 |
| QRBBNNNPPPP     | 2      | 0 |
| QRBBBPPP        | 1      | 0 |
| QRBBBNPPP       | 1      | 0 |
| QRR             | 535    | 0 |
| QRRP            | 1915   | 0 |
| QRRPP           | 7284   | 0 |
| QRRPPP          | 22398  | 0 |
| QRRPPPP         | 49281  | 0 |
| QRRPPPPP        | 81831  | 1 |
| QRRPPPPPP       | 93493  | 1 |
| QRRPPPPPPP      | 58578  | 0 |
| QRRPPPPPPPP     | 9976   | 0 |
| QRRN            | 162    | 0 |
| QRRNP           | 581    | 0 |
| QRRNPP          | 3045   | 0 |
| QRRNPPP         | 13209  | 0 |
| QRRNPPPP        | 39899  | 0 |
| QRRNPPPPP       | 91339  | 1 |
| QRRNPPPPPP      | 140852 | 2 |
| QRRNPPPPPPP     | 115463 | 1 |
| QRRNPPPPPPPP    | 25549  | 0 |
| QRRNN           | 5      | 0 |
| QRRNNP          | 50     | 0 |
| QRRNNPP         | 376    | 0 |
| QRRNNPPP        | 2262   | 0 |
| QRRNNPPPP       | 9353   | 0 |
| QRRNNPPPPP      | 27216  | 0 |
| QRRNNPPPPPP     | 54708  | 0 |
| QRRNNPPPPPPP    | 62012  | 0 |
| QRRNNPPPPPPPP   | 19238  | 0 |
| QRRNNNPP        | 1      | 0 |
| QRRNNNNPPP      | 1      | 0 |
| QRRB            | 117    | 0 |
| QRRBP           | 861    | 0 |
| QRRBPP          | 4531   | 0 |
| QRRBPPP         | 19234  | 0 |
| QRRBPPPP        | 58562  | 0 |
| QRRBPPPPP       | 130154 | 1 |
| QRRBPPPPPP      | 192878 | 2 |
| QRRBPPPPPPP     | 151621 | 2 |
| QRRBPPPPPPPP    | 30336  | 0 |
| QRRBN           | 28     | 0 |
| QRRBNP          | 287    | 0 |
| QRRBNPP         | 2378   | 0 |
| QRRBNPPP        | 12972  | 0 |
| QRRBNPPPP       | 51618  | 0 |
| QRRBNPPPPP      | 152706 | 2 |
| QRRBNPPPPPP     | 308837 | 4 |
| QRRBNPPPPPPP    | 323797 | 4 |
| QRRBNPPPPPPPP   | 81017  | 1 |
| QRRBNN          | 2      | 0 |
| QRRBNNP         | 40     | 0 |
| QRRBNNPP        | 461    | 0 |
| QRRBNNPPP       | 2993   | 0 |
| QRRBNNPPPP      | 14792  | 0 |
| QRRBNNPPPPP     | 55554  | 0 |
| QRRBNNPPPPPP    | 145142 | 2 |
| QRRBNNPPPPPPP   | 205470 | 3 |
| QRRBNNPPPPPPPP  | 68644  | 1 |
| QRRBNNNNPPP     | 1      | 0 |
| QRRBB           | 10     | 0 |
| QRRBBP          | 91     | 0 |
| QRRBBPP         | 793    | 0 |
| QRRBBPPP        | 4389   | 0 |
| QRRBBPPPP       | 17515  | 0 |
| QRRBBPPPPP      | 51745  | 0 |
| QRRBBPPPPPP     | 102524 | 1 |
| QRRBBPPPPPPP    | 100268 | 1 |
| QRRBBPPPPPPPP   | 23359  | 0 |
| QRRBBN          | 1      | 0 |
| QRRBBNP         | 54     | 0 |
| QRRBBNPP        | 597    | 0 |
| QRRBBNPPP       | 3975   | 0 |
| QRRBBNPPPP      | 20278  | 0 |
| QRRBBNPPPPP     | 77645  | 1 |
| QRRBBNPPPPPP    | 207543 | 3 |
| QRRBBNPPPPPPP   | 263394 | 3 |
| QRRBBNPPPPPPPP  | 71456  | 1 |
| QRRBBNN         | 1      | 0 |
| QRRBBNNP        | 12     | 0 |
| QRRBBNNPP       | 146    | 0 |
| QRRBBNNPPP      | 1270   | 0 |
| QRRBBNNPPPP     | 7744   | 0 |
| QRRBBNNPPPPP    | 35984  | 0 |
| QRRBBNNPPPPPP   | 123036 | 1 |
| QRRBBNNPPPPPPP  | 204689 | 3 |
| QRRBBNNPPPPPPPP | 81615  | 1 |
| QRRBBNNNPPPPPP  | 1      | 0 |
| QRRBBNNNPPPPPPP | 1      | 0 |
| QRRBBNNNNPPPPPP | 2      | 0 |
| QRRBBBPPP       | 1      | 0 |
| QRRBBBPPPPP     | 1      | 0 |
| QRRBBBNNPPPPPP  | 1      | 0 |
| QRRRP           | 1      | 0 |
| QRRRPP          | 2      | 0 |
| QRRRPPPP        | 2      | 0 |
| QRRRPPPPPPP     | 1      | 0 |
| QRRRNPPP        | 1      | 0 |
| QRRRBPP         | 1      | 0 |
| QRRRBPPPPP      | 2      | 0 |
| QRRRBPPPPPP     | 2      | 0 |
| QRRRBNPPPP      | 2      | 0 |
| QRRRBNPPPPPP    | 2      | 0 |
| QRRRBNNPPPP     | 2      | 0 |
| QRRRBBNPPPP     | 1      | 0 |
| QRRRBBNPPPPP    | 1      | 0 |
| QRRRBBNNPPPPPPP | 1      | 0 |
| QQ              | 779    | 0 |
| QQP             | 1562   | 0 |
| QQPP            | 1912   | 0 |
| QQPPP           | 1435   | 0 |
| QQPPPP          | 699    | 0 |
| QQPPPPP         | 304    | 0 |
| QQPPPPPP        | 57     | 0 |
| QQPPPPPPP       | 2      | 0 |
| QQN             | 76     | 0 |
| QQNP            | 201    | 0 |
| QQNPP           | 202    | 0 |
| QQNPPP          | 230    | 0 |
| QQNPPPP         | 141    | 0 |
| QQNPPPPP        | 53     | 0 |
| QQNPPPPPP       | 14     | 0 |
| QQNN            | 1      | 0 |
| QQNNP           | 1      | 0 |
| QQNNPP          | 9      | 0 |
| QQNNPPP         | 12     | 0 |
| QQNNPPPP        | 8      | 0 |
| QQNNPPPPP       | 1      | 0 |
| QQNNPPPPPP      | 1      | 0 |
| QQB             | 95     | 0 |
| QQBP            | 379    | 0 |
| QQBPP           | 452    | 0 |
| QQBPPP          | 353    | 0 |
| QQBPPPP         | 228    | 0 |
| QQBPPPPP        | 85     | 0 |
| QQBPPPPPP       | 32     | 0 |
| QQBPPPPPPP      | 5      | 0 |
| QQBN            | 7      | 0 |
| QQBNP           | 25     | 0 |
| QQBNPP          | 50     | 0 |
| QQBNPPP         | 36     | 0 |
| QQBNPPPP        | 38     | 0 |
| QQBNPPPPP       | 18     | 0 |
| QQBNPPPPPP      | 5      | 0 |
| QQBNPPPPPPP     | 2      | 0 |
| QQBNNPP         | 1      | 0 |
| QQBNNPPP        | 4      | 0 |
| QQBNNPPPP       | 3      | 0 |
| QQBNNPPPPP      | 1      | 0 |
| QQBB            | 2      | 0 |
| QQBBP           | 3      | 0 |
| QQBBPP          | 4      | 0 |
| QQBBPPP         | 29     | 0 |
| QQBBPPPP        | 19     | 0 |
| QQBBPPPPP       | 10     | 0 |
| QQBBPPPPPP      | 5      | 0 |
| QQBBPPPPPPP     | 1      | 0 |
| QQBBNP          | 3      | 0 |
| QQBBNPP         | 2      | 0 |
| QQBBNPPP        | 5      | 0 |
| QQBBNPPPP       | 3      | 0 |
| QQBBNPPPPP      | 3      | 0 |
| QQBBNNPPPP      | 1      | 0 |
| QQBBNNPPPPPP    | 1      | 0 |
| QQR             | 151    | 0 |
| QQRP            | 294    | 0 |
| QQRPP           | 421    | 0 |
| QQRPPP          | 450    | 0 |
| QQRPPPP         | 386    | 0 |
| QQRPPPPP        | 165    | 0 |
| QQRPPPPPP       | 51     | 0 |
| QQRPPPPPPP      | 3      | 0 |
| QQRN            | 11     | 0 |
| QQRNP           | 37     | 0 |
| QQRNPP          | 56     | 0 |
| QQRNPPP         | 81     | 0 |
| QQRNPPPP        | 66     | 0 |
| QQRNPPPPP       | 47     | 0 |
| QQRNPPPPPP      | 20     | 0 |
| QQRNPPPPPPP     | 2      | 0 |
| QQRNNPP         | 3      | 0 |
| QQRNNPPP        | 6      | 0 |
| QQRNNPPPP       | 12     | 0 |
| QQRNNPPPPP      | 11     | 0 |
| QQRNNPPPPPP     | 2      | 0 |
| QQRB            | 16     | 0 |
| QQRBP           | 53     | 0 |
| QQRBPP          | 123    | 0 |
| QQRBPPP         | 220    | 0 |
| QQRBPPPP        | 184    | 0 |
| QQRBPPPPP       | 106    | 0 |
| QQRBPPPPPP      | 28     | 0 |
| QQRBPPPPPPP     | 5      | 0 |
| QQRBN           | 1      | 0 |
| QQRBNP          | 2      | 0 |
| QQRBNPP         | 16     | 0 |
| QQRBNPPP        | 43     | 0 |
| QQRBNPPPP       | 66     | 0 |
| QQRBNPPPPP      | 50     | 0 |
| QQRBNPPPPPP     | 10     | 0 |
| QQRBNPPPPPPP    | 4      | 0 |
| QQRBNNP         | 1      | 0 |
| QQRBNNPPP       | 9      | 0 |
| QQRBNNPPPP      | 9      | 0 |
| QQRBNNPPPPP     | 9      | 0 |
| QQRBNNPPPPPP    | 8      | 0 |
| QQRBBP          | 3      | 0 |
| QQRBBPP         | 4      | 0 |
| QQRBBPPP        | 26     | 0 |
| QQRBBPPPP       | 23     | 0 |
| QQRBBPPPPP      | 9      | 0 |
| QQRBBPPPPPP     | 5      | 0 |
| QQRBBNP         | 1      | 0 |
| QQRBBNPP        | 2      | 0 |
| QQRBBNPPP       | 7      | 0 |
| QQRBBNPPPP      | 9      | 0 |
| QQRBBNPPPPP     | 14     | 0 |
| QQRBBNPPPPPP    | 6      | 0 |
| QQRBBNPPPPPPP   | 4      | 0 |
| QQRBBNNPPP      | 1      | 0 |
| QQRBBNNPPPP     | 3      | 0 |
| QQRBBNNPPPPP    | 3      | 0 |
| QQRBBNNPPPPPP   | 3      | 0 |
| QQRBBNNPPPPPPP  | 1      | 0 |
| QQRR            | 9      | 0 |
| QQRRP           | 21     | 0 |
| QQRRPP          | 70     | 0 |
| QQRRPPP         | 118    | 0 |
| QQRRPPPP        | 130    | 0 |
| QQRRPPPPP       | 101    | 0 |
| QQRRPPPPPP      | 40     | 0 |
| QQRRPPPPPPP     | 9      | 0 |
| QQRRNP          | 6      | 0 |
| QQRRNPP         | 17     | 0 |
| QQRRNPPP        | 29     | 0 |
| QQRRNPPPP       | 56     | 0 |
| QQRRNPPPPP      | 46     | 0 |
| QQRRNPPPPPP     | 23     | 0 |
| QQRRNPPPPPPP    | 6      | 0 |
| QQRRNNPP        | 3      | 0 |
| QQRRNNPPP       | 2      | 0 |
| QQRRNNPPPP      | 8      | 0 |
| QQRRNNPPPPP     | 7      | 0 |
| QQRRNNPPPPPP    | 6      | 0 |
| QQRRNNPPPPPPP   | 3      | 0 |
| QQRRBP          | 10     | 0 |
| QQRRBPP         | 35     | 0 |
| QQRRBPPP        | 72     | 0 |
| QQRRBPPPP       | 68     | 0 |
| QQRRBPPPPP      | 81     | 0 |
| QQRRBPPPPPP     | 36     | 0 |
| QQRRBPPPPPPP    | 6      | 0 |
| QQRRBNPP        | 6      | 0 |
| QQRRBNPPP       | 18     | 0 |
| QQRRBNPPPP      | 47     | 0 |
| QQRRBNPPPPP     | 71     | 0 |
| QQRRBNPPPPPP    | 45     | 0 |
| QQRRBNPPPPPPP   | 17     | 0 |
| QQRRBNNPP       | 1      | 0 |
| QQRRBNNPPP      | 7      | 0 |
| QQRRBNNPPPP     | 2      | 0 |
| QQRRBNNPPPPP    | 10     | 0 |
| QQRRBNNPPPPPP   | 24     | 0 |
| QQRRBNNPPPPPPP  | 11     | 0 |
| QQRRBBPPP       | 6      | 0 |
| QQRRBBPPPP      | 15     | 0 |
| QQRRBBPPPPP     | 12     | 0 |
| QQRRBBPPPPPP    | 14     | 0 |
| QQRRBBPPPPPPP   | 3      | 0 |
| QQRRBBNPP       | 1      | 0 |
| QQRRBBNPPP      | 3      | 0 |
| QQRRBBNPPPP     | 14     | 0 |
| QQRRBBNPPPPP    | 18     | 0 |
| QQRRBBNPPPPPP   | 19     | 0 |
| QQRRBBNPPPPPPP  | 8      | 0 |
| QQRRBBNNPPP     | 3      | 0 |
| QQRRBBNNPPPP    | 4      | 0 |
| QQRRBBNNPPPPP   | 4      | 0 |
| QQRRBBNNPPPPPP  | 7      | 0 |
| QQRRBBNNPPPPPPP | 13     | 0 |
| QQRRRPPPP       | 1      | 0 |
| QQRRRBNNPPP     | 1      | 0 |
| QQQ             | 1      | 0 |
| QQQP            | 11     | 0 |
| QQQPP           | 11     | 0 |
| QQQPPP          | 9      | 0 |
| QQQPPPP         | 4      | 0 |
| QQQNP           | 2      | 0 |
| QQQNPP          | 1      | 0 |
| QQQBP           | 2      | 0 |
| QQQBPP          | 4      | 0 |
| QQQBPPPP        | 1      | 0 |
| QQQBPPPPP       | 1      | 0 |
| QQQBBPPP        | 1      | 0 |
| QQQRP           | 6      | 0 |
| QQQRPP          | 2      | 0 |
| QQQRPPP         | 4      | 0 |
| QQQRNP          | 3      | 0 |
| QQQRNPP         | 2      | 0 |
| QQQRNPPP        | 1      | 0 |
| QQQRNPPPP       | 1      | 0 |
| QQQRBPPP        | 1      | 0 |
| QQQRBPPPP       | 1      | 0 |
| QQQRBNP         | 1      | 0 |
| QQQRBNPP        | 3      | 0 |
| QQQRBNPPP       | 2      | 0 |
| QQQRRPP         | 1      | 0 |
| QQQRRPPP        | 1      | 0 |
| QQQRRBPPP       | 2      | 0 |
| QQQRRBPPPP      | 1      | 0 |
| QQQRRBBNNPPPP   | 1      | 0 |
| QQQRRBBNNPPPPPP | 1      | 0 |
| QQQQP           | 1      | 0 |
| QQQQPP          | 1      | 0 |
| QQQQRP          | 2      | 0 |
| QQQQRPP         | 2      | 0 |
| QQQQQP          | 1      | 0 |
| QQQQQNPP        | 1      | 0 |
| QQQQQRP         | 1      | 0 |
| QQQQQRNP        | 1      | 0 |
| QQQQQRRBNP      | 1      | 0 |
| QQQQQRRBBNNPP   | 1      | 0 |
| QQQQQQRNPP      | 1      | 0 |
| QQQQQQQRP       | 1      | 0 |


K adjusted to: 1.4500000000000002

## new optimization round

### Optimizing with step 10

Error at start: 0.0743372813565047

| Duration | Round | Step | Params adjustments | Adj total | Curr Error | Overall AdjPerSecond | AdjPerSecond |
|----------|-------|------|--------------------|-----------|------------|----------------------|--------------|
| 0:03:54,497 | 100   | 10   | 0                  | 0         | 0.0743372813565047 | 0.0                  | 0.0          |
| 0:07:52,493 | 200   | 10   | 2                  | 2         | 0.07433718184550338 | 0.00423728813559322  | 0.008403361344537815 |
| 0:11:51,801 | 300   | 10   | 3                  | 5         | 0.07433621616269774 | 0.007032348804500703 | 0.012552301255230125 |
| 0:16:16,448 | 400   | 10   | 4                  | 9         | 0.07432576924895867 | 0.009221311475409836 | 0.015151515151515152 |
| 0:20:32,081 | 500   | 10   | 1                  | 10        | 0.07432575201598358 | 0.008116883116883116 | 0.00392156862745098  |
| 0:24:57,042 | 600   | 10   | 0                  | 10        | 0.07432575201598358 | 0.0066844919786096255 | 0.0                  |
| 0:29:23,887 | 700   | 10   | 3                  | 13        | 0.07432569318060091 | 0.00737379466817924   | 0.011278195488721804 |
| 0:33:58,283 | 800   | 10   | 0                  | 13        | 0.07432569318060091 | 0.006378802747791953  | 0.0                  |
| 0:38:11,362 | 900   | 10   | 0                  | 13        | 0.07432569318060091 | 0.005674378000872981  | 0.0                  |
| 0:42:50,703 | 1000  | 10   | 0                  | 13        | 0.07432569318060091 | 0.005058365758754864  | 0.0                  |
| 0:47:35,894 | 1100  | 10   | 2                  | 15        | 0.07432560398011873 | 0.005253940455341506  | 0.007017543859649123 |
| 0:52:23,371 | 1200  | 10   | 1                  | 16        | 0.0743253266719241  | 0.005090677696468342  | 0.003484320557491289 |
| 0:57:05,846 | 1300  | 10   | 1                  | 17        | 0.07432530318467083 | 0.004963503649635037  | 0.0035460992907801418 |
| 1:02:11,581 | 1400  | 10   | 3                  | 20        | 0.07432475953974774 | 0.005360493165371214  | 0.009836065573770493  |
| 1:07:12,454 | 1500  | 10   | 3                  | 23        | 0.07432457116949027 | 0.005704365079365079  | 0.01                  |
| 1:11:46,548 | 1600  | 10   | 5                  | 28        | 0.07432440737562755 | 0.006502554575011612  | 0.01824817518248175   |
| 1:16:09,891 | 1700  | 10   | 0                  | 28        | 0.07432440737562755 | 0.006128255635806522  | 0.0                   |
| 1:20:57,901 | 1800  | 10   | 0                  | 28        | 0.07432440737562755 | 0.005764875437512868  | 0.0                   |
| 1:25:50,263 | 1900  | 10   | 7                  | 35        | 0.0743236725036452  | 0.006796116504854369  | 0.023972602739726026  |
| 1:30:31,390 | 2000  | 10   | 8                  | 43        | 0.0743221237078759  | 0.007917510587368809  | 0.028469750889679714  |
| 1:35:35,389 | 2100  | 10   | 4                  | 47        | 0.07432199802124353 | 0.008195292066259808  | 0.013201320132013201  |
| 1:40:03,050 | 2200  | 10   | 1                  | 48        | 0.07432193488939377 | 0.007997334221926024  | 0.003745318352059925  |
| 1:43:48,592 | 2300  | 10   | 2                  | 50        | 0.07432174607967006 | 0.00802825947334618   | 0.008888888888888889  |
# Tuning Options

| Parameter                    | Value                        |
|------------------------------|------------------------------|
| adjustK                      | true                         |
| delta                        | 1.0E-8                       |
| evalParamSet                 | CURRENT                      |
| geneticParams                | null                         |
| inputFiles                   | [C:\projekte\cygwin_home\mla\jackyChessDockerTesting\tuningdata\lichess-big3-resolved.book]|
| multiThreading               | true                         |
| name                         | lichess test2                |
| optimizeRecalcOnlyDependendFens| false                        |
| outputdir                    | null                         |
| progressUpdatesInMinutes     | 0                            |
| removeDuplicateFens          | true                         |
| resetParametersBeforeTuning  | false                        |
| shuffleTuningParameter       | false                        |
| stepGranularity              | [1]                          |
| threadCount                  | 5                            |
| tuneAdjustments              | true                         |
| tuneAdjustmentsFactors       | false                        |
| tuneComplexity               | true                         |
| tuneKingAttack               | true                         |
| tuneKingSafety               | true                         |
| tuneMaterial                 | false                        |
| tuneMobility                 | true                         |
| tuneMobilityTropism          | true                         |
| tunePassedPawnEval           | true                         |
| tunePawnEval                 | true                         |
| tunePositional               | true                         |
| tunePst                      | true                         |
| tuneThreats                  | true                         |


# Dataset Information

|                |         |
|----------------|---------|
| Num Fens       | 6819087 |
| MATE White     | 2801909 |
| MATE Black     | 2605257 |
| Draws          | 1411921 |
| Duplicate Fens | 0       |


## Number of fens by Game Phase

| Phase | Count  | %  |
|-------|--------|----|
| 0     | 593109 | 8  |
| 1     | 923895 | 13 |
| 2     | 736133 | 10 |
| 3     | 619828 | 9  |
| 4     | 561207 | 8  |
| 5     | 463708 | 6  |
| 6     | 437031 | 6  |
| 7     | 530586 | 7  |
| 8     | 654432 | 9  |
| 9     | 692383 | 10 |
| 10    | 606775 | 8  |


## Number of fens by having figures

| FigureType | Count   | %   |
|------------|---------|-----|
| Rook       | 5988879 | 87  |
| Knight     | 4456890 | 65  |
| Bishop     | 4966199 | 72  |
| Queen      | 3817373 | 55  |
| Pawn       | 6819087 | 100 |


## Number of fens by Material

| Material        | Count  | % |
|-----------------|--------|---|
|                 | 33454  | 0 |
| P               | 76606  | 1 |
| PP              | 122515 | 1 |
| PPP             | 132276 | 1 |
| PPPP            | 103981 | 1 |
| PPPPP           | 68109  | 0 |
| PPPPPP          | 32877  | 0 |
| PPPPPPP         | 8662   | 0 |
| PPPPPPPP        | 323    | 0 |
| N               | 12616  | 0 |
| NP              | 33814  | 0 |
| NPP             | 55129  | 0 |
| NPPP            | 68974  | 1 |
| NPPPP           | 64043  | 0 |
| NPPPPP          | 49168  | 0 |
| NPPPPPP         | 28688  | 0 |
| NPPPPPPP        | 9006   | 0 |
| NPPPPPPPP       | 209    | 0 |
| NN              | 530    | 0 |
| NNP             | 1841   | 0 |
| NNPP            | 3202   | 0 |
| NNPPP           | 5025   | 0 |
| NNPPPP          | 5922   | 0 |
| NNPPPPP         | 6029   | 0 |
| NNPPPPPP        | 4408   | 0 |
| NNPPPPPPP       | 2177   | 0 |
| NNPPPPPPPP      | 20     | 0 |
| NNN             | 3      | 0 |
| NNNP            | 4      | 0 |
| NNNPP           | 13     | 0 |
| NNNPPP          | 4      | 0 |
| NNNPPPP         | 3      | 0 |
| NNNPPPPP        | 1      | 0 |
| NNNNPPP         | 2      | 0 |
| B               | 16616  | 0 |
| BP              | 51273  | 0 |
| BPP             | 87515  | 1 |
| BPPP            | 103420 | 1 |
| BPPPP           | 100360 | 1 |
| BPPPPP          | 76023  | 1 |
| BPPPPPP         | 42486  | 0 |
| BPPPPPPP        | 11779  | 0 |
| BPPPPPPPP       | 401    | 0 |
| BN              | 2812   | 0 |
| BNP             | 9913   | 0 |
| BNPP            | 19555  | 0 |
| BNPPP           | 27490  | 0 |
| BNPPPP          | 32087  | 0 |
| BNPPPPP         | 29573  | 0 |
| BNPPPPPP        | 21544  | 0 |
| BNPPPPPPP       | 8004   | 0 |
| BNPPPPPPPP      | 155    | 0 |
| BNN             | 49     | 0 |
| BNNP            | 275    | 0 |
| BNNPP           | 873    | 0 |
| BNNPPP          | 1789   | 0 |
| BNNPPPP         | 2884   | 0 |
| BNNPPPPP        | 3467   | 0 |
| BNNPPPPPP       | 3189   | 0 |
| BNNPPPPPPP      | 1840   | 0 |
| BNNPPPPPPPP     | 14     | 0 |
| BNNNP           | 1      | 0 |
| BNNNPPPP        | 1      | 0 |
| BNNNNNP         | 1      | 0 |
| BNNNNNPP        | 1      | 0 |
| BB              | 1971   | 0 |
| BBP             | 4075   | 0 |
| BBPP            | 7041   | 0 |
| BBPPP           | 11143  | 0 |
| BBPPPP          | 11437  | 0 |
| BBPPPPP         | 11128  | 0 |
| BBPPPPPP        | 8216   | 0 |
| BBPPPPPPP       | 2873   | 0 |
| BBPPPPPPPP      | 131    | 0 |
| BBN             | 93     | 0 |
| BBNP            | 299    | 0 |
| BBNPP           | 1039   | 0 |
| BBNPPP          | 2299   | 0 |
| BBNPPPP         | 3735   | 0 |
| BBNPPPPP        | 4460   | 0 |
| BBNPPPPPP       | 4140   | 0 |
| BBNPPPPPPP      | 2009   | 0 |
| BBNPPPPPPPP     | 23     | 0 |
| BBNNP           | 20     | 0 |
| BBNNPP          | 79     | 0 |
| BBNNPPP         | 301    | 0 |
| BBNNPPPP        | 636    | 0 |
| BBNNPPPPP       | 738    | 0 |
| BBNNPPPPPP      | 880    | 0 |
| BBNNPPPPPPP     | 633    | 0 |
| BBNNPPPPPPPP    | 5      | 0 |
| BBNNNP          | 3      | 0 |
| BBBP            | 4      | 0 |
| BBBPP           | 4      | 0 |
| BBBPPP          | 3      | 0 |
| BBBPPPP         | 4      | 0 |
| BBBNP           | 1      | 0 |
| BBBNPPP         | 1      | 0 |
| BBBNNP          | 1      | 0 |
| BBBBP           | 3      | 0 |
| BBBBPPP         | 2      | 0 |
| BBBBPPPP        | 1      | 0 |
| BBBBNPP         | 1      | 0 |
| BBBBBPPP        | 1      | 0 |
| BBBBBBPP        | 1      | 0 |
| R               | 70970  | 1 |
| RP              | 192547 | 2 |
| RPP             | 252064 | 3 |
| RPPP            | 268348 | 3 |
| RPPPP           | 221962 | 3 |
| RPPPPP          | 158007 | 2 |
| RPPPPPP         | 84534  | 1 |
| RPPPPPPP        | 22633  | 0 |
| RPPPPPPPP       | 1060   | 0 |
| RN              | 14423  | 0 |
| RNP             | 32920  | 0 |
| RNPP            | 60144  | 0 |
| RNPPP           | 96059  | 1 |
| RNPPPP          | 107436 | 1 |
| RNPPPPP         | 104563 | 1 |
| RNPPPPPP        | 69062  | 1 |
| RNPPPPPPP       | 20732  | 0 |
| RNPPPPPPPP      | 1028   | 0 |
| RNN             | 203    | 0 |
| RNNP            | 1056   | 0 |
| RNNPP           | 3050   | 0 |
| RNNPPP          | 6878   | 0 |
| RNNPPPP         | 11631  | 0 |
| RNNPPPPP        | 15220  | 0 |
| RNNPPPPPP       | 12681  | 0 |
| RNNPPPPPPP      | 5002   | 0 |
| RNNPPPPPPPP     | 254    | 0 |
| RNNN            | 1      | 0 |
| RNNNP           | 4      | 0 |
| RNNNPP          | 1      | 0 |
| RNNNPPP         | 5      | 0 |
| RNNNPPPP        | 2      | 0 |
| RNNNPPPPP       | 1      | 0 |
| RNNNNP          | 1      | 0 |
| RNNNNPP         | 2      | 0 |
| RB              | 15708  | 0 |
| RBP             | 44306  | 0 |
| RBPP            | 82180  | 1 |
| RBPPP           | 125978 | 1 |
| RBPPPP          | 151618 | 2 |
| RBPPPPP         | 144476 | 2 |
| RBPPPPPP        | 94000  | 1 |
| RBPPPPPPP       | 27932  | 0 |
| RBPPPPPPPP      | 1485   | 0 |
| RBN             | 1368   | 0 |
| RBNP            | 5362   | 0 |
| RBNPP           | 15999  | 0 |
| RBNPPP          | 36004  | 0 |
| RBNPPPP         | 62303  | 0 |
| RBNPPPPP        | 79905  | 1 |
| RBNPPPPPP       | 64804  | 0 |
| RBNPPPPPPP      | 23491  | 0 |
| RBNPPPPPPPP     | 1280   | 0 |
| RBNN            | 34     | 0 |
| RBNNP           | 233    | 0 |
| RBNNPP          | 1117   | 0 |
| RBNNPPP         | 3910   | 0 |
| RBNNPPPP        | 8980   | 0 |
| RBNNPPPPP       | 14328  | 0 |
| RBNNPPPPPP      | 14249  | 0 |
| RBNNPPPPPPP     | 6298   | 0 |
| RBNNPPPPPPPP    | 410    | 0 |
| RBNNNP          | 6      | 0 |
| RBNNNPP         | 2      | 0 |
| RBNNNPPP        | 3      | 0 |
| RBNNNPPPP       | 2      | 0 |
| RBNNNPPPPP      | 1      | 0 |
| RBNNNNPP        | 1      | 0 |
| RBNNNNPPP       | 1      | 0 |
| RBB             | 763    | 0 |
| RBBP            | 1433   | 0 |
| RBBPP           | 4738   | 0 |
| RBBPPP          | 11059  | 0 |
| RBBPPPP         | 20282  | 0 |
| RBBPPPPP        | 26105  | 0 |
| RBBPPPPPP       | 21218  | 0 |
| RBBPPPPPPP      | 7519   | 0 |
| RBBPPPPPPPP     | 453    | 0 |
| RBBN            | 40     | 0 |
| RBBNP           | 304    | 0 |
| RBBNPP          | 1287   | 0 |
| RBBNPPP         | 4610   | 0 |
| RBBNPPPP        | 10988  | 0 |
| RBBNPPPPP       | 18234  | 0 |
| RBBNPPPPPP      | 17827  | 0 |
| RBBNPPPPPPP     | 7268   | 0 |
| RBBNPPPPPPPP    | 402    | 0 |
| RBBNN           | 2      | 0 |
| RBBNNP          | 39     | 0 |
| RBBNNPP         | 168    | 0 |
| RBBNNPPP        | 789    | 0 |
| RBBNNPPPP       | 2327   | 0 |
| RBBNNPPPPP      | 4686   | 0 |
| RBBNNPPPPPP     | 5215   | 0 |
| RBBNNPPPPPPP    | 2659   | 0 |
| RBBNNPPPPPPPP   | 150    | 0 |
| RBBNNNPPPP      | 1      | 0 |
| RBBNNNPPPPP     | 1      | 0 |
| RBBNNNPPPPPP    | 1      | 0 |
| RBBBP           | 1      | 0 |
| RBBBPP          | 1      | 0 |
| RBBBPPP         | 2      | 0 |
| RBBBNP          | 1      | 0 |
| RBBBNPP         | 2      | 0 |
| RBBBNPPP        | 1      | 0 |
| RBBBBP          | 2      | 0 |
| RBBBBPP         | 2      | 0 |
| RR              | 8637   | 0 |
| RRP             | 22399  | 0 |
| RRPP            | 43620  | 0 |
| RRPPP           | 71470  | 1 |
| RRPPPP          | 91824  | 1 |
| RRPPPPP         | 99571  | 1 |
| RRPPPPPP        | 78830  | 1 |
| RRPPPPPPP       | 35657  | 0 |
| RRPPPPPPPP      | 4122   | 0 |
| RRN             | 1368   | 0 |
| RRNP            | 4107   | 0 |
| RRNPP           | 11489  | 0 |
| RRNPPP          | 27864  | 0 |
| RRNPPPP         | 53679  | 0 |
| RRNPPPPP        | 83375  | 1 |
| RRNPPPPPP       | 88799  | 1 |
| RRNPPPPPPP      | 50063  | 0 |
| RRNPPPPPPPP     | 7135   | 0 |
| RRNN            | 35     | 0 |
| RRNNP           | 186    | 0 |
| RRNNPP          | 841    | 0 |
| RRNNPPP         | 3292   | 0 |
| RRNNPPPP        | 8797   | 0 |
| RRNNPPPPP       | 17939  | 0 |
| RRNNPPPPPP      | 24327  | 0 |
| RRNNPPPPPPP     | 17522  | 0 |
| RRNNPPPPPPPP    | 3029   | 0 |
| RRNNNP          | 1      | 0 |
| RRNNNPP         | 1      | 0 |
| RRNNNPPPP       | 2      | 0 |
| RRNNNPPPPPP     | 1      | 0 |
| RRNNNNPP        | 1      | 0 |
| RRB             | 1118   | 0 |
| RRBP            | 5361   | 0 |
| RRBPP           | 15376  | 0 |
| RRBPPP          | 38038  | 0 |
| RRBPPPP         | 74578  | 1 |
| RRBPPPPP        | 115257 | 1 |
| RRBPPPPPP       | 120731 | 1 |
| RRBPPPPPPP      | 65672  | 0 |
| RRBPPPPPPPP     | 8391   | 0 |
| RRBN            | 392    | 0 |
| RRBNP           | 1445   | 0 |
| RRBNPP          | 4662   | 0 |
| RRBNPPP         | 16718  | 0 |
| RRBNPPPP        | 46521  | 0 |
| RRBNPPPPP       | 96948  | 1 |
| RRBNPPPPPP      | 136001 | 1 |
| RRBNPPPPPPP     | 93586  | 1 |
| RRBNPPPPPPPP    | 14421  | 0 |
| RRBNN           | 6      | 0 |
| RRBNNP          | 78     | 0 |
| RRBNNPP         | 503    | 0 |
| RRBNNPPP        | 2559   | 0 |
| RRBNNPPPP       | 9221   | 0 |
| RRBNNPPPPP      | 24702  | 0 |
| RRBNNPPPPPP     | 44521  | 0 |
| RRBNNPPPPPPP    | 37629  | 0 |
| RRBNNPPPPPPPP   | 6990   | 0 |
| RRBNNNPP        | 1      | 0 |
| RRBNNNPPP       | 1      | 0 |
| RRBNNNPPPP      | 2      | 0 |
| RRBB            | 221    | 0 |
| RRBBP           | 336    | 0 |
| RRBBPP          | 1394   | 0 |
| RRBBPPP         | 5139   | 0 |
| RRBBPPPP        | 14214  | 0 |
| RRBBPPPPP       | 30328  | 0 |
| RRBBPPPPPP      | 42966  | 0 |
| RRBBPPPPPPP     | 27875  | 0 |
| RRBBPPPPPPPP    | 3618   | 0 |
| RRBBN           | 11     | 0 |
| RRBBNP          | 90     | 0 |
| RRBBNPP         | 672    | 0 |
| RRBBNPPP        | 3055   | 0 |
| RRBBNPPPP       | 11591  | 0 |
| RRBBNPPPPP      | 31919  | 0 |
| RRBBNPPPPPP     | 58179  | 0 |
| RRBBNPPPPPPP    | 45224  | 0 |
| RRBBNPPPPPPPP   | 6508   | 0 |
| RRBBNN          | 1      | 0 |
| RRBBNNP         | 9      | 0 |
| RRBBNNPP        | 117    | 0 |
| RRBBNNPPP       | 673    | 0 |
| RRBBNNPPPP      | 3139   | 0 |
| RRBBNNPPPPP     | 10137  | 0 |
| RRBBNNPPPPPP    | 21645  | 0 |
| RRBBNNPPPPPPP   | 19947  | 0 |
| RRBBNNPPPPPPPP  | 3797   | 0 |
| RRBBNNNPPPPPP   | 1      | 0 |
| RRBBBPPPPP      | 1      | 0 |
| RRBBBNPPPP      | 1      | 0 |
| RRR             | 3      | 0 |
| RRRP            | 5      | 0 |
| RRRPP           | 11     | 0 |
| RRRPPP          | 6      | 0 |
| RRRPPPP         | 5      | 0 |
| RRRPPPPP        | 6      | 0 |
| RRRPPPPPP       | 1      | 0 |
| RRRN            | 2      | 0 |
| RRRNP           | 2      | 0 |
| RRRNPP          | 4      | 0 |
| RRRNPPP         | 3      | 0 |
| RRRNPPPP        | 1      | 0 |
| RRRNPPPPP       | 2      | 0 |
| RRRNPPPPPP      | 1      | 0 |
| RRRBP           | 6      | 0 |
| RRRBPP          | 2      | 0 |
| RRRBPPP         | 4      | 0 |
| RRRBPPPP        | 5      | 0 |
| RRRBPPPPP       | 1      | 0 |
| RRRBNP          | 1      | 0 |
| RRRBNPPPP       | 1      | 0 |
| RRRBNPPPPP      | 1      | 0 |
| RRRBNPPPPPP     | 1      | 0 |
| RRRBNNPPPPPP    | 1      | 0 |
| RRRBBPPPPP      | 1      | 0 |
| RRRBBPPPPPP     | 1      | 0 |
| RRRBBNNPPPPPPP  | 1      | 0 |
| RRRRP           | 1      | 0 |
| RRRRPP          | 1      | 0 |
| Q               | 15540  | 0 |
| QP              | 51155  | 0 |
| QPP             | 68458  | 1 |
| QPPP            | 77494  | 1 |
| QPPPP           | 64778  | 0 |
| QPPPPP          | 44757  | 0 |
| QPPPPPP         | 23088  | 0 |
| QPPPPPPP        | 6359   | 0 |
| QPPPPPPPP       | 270    | 0 |
| QN              | 2746   | 0 |
| QNP             | 6954   | 0 |
| QNPP            | 13793  | 0 |
| QNPPP           | 20458  | 0 |
| QNPPPP          | 25540  | 0 |
| QNPPPPP         | 24736  | 0 |
| QNPPPPPP        | 18145  | 0 |
| QNPPPPPPP       | 6473   | 0 |
| QNPPPPPPPP      | 245    | 0 |
| QNN             | 61     | 0 |
| QNNP            | 296    | 0 |
| QNNPP           | 697    | 0 |
| QNNPPP          | 1484   | 0 |
| QNNPPPP         | 2682   | 0 |
| QNNPPPPP        | 3733   | 0 |
| QNNPPPPPP       | 3487   | 0 |
| QNNPPPPPPP      | 2003   | 0 |
| QNNPPPPPPPP     | 50     | 0 |
| QNNNP           | 4      | 0 |
| QNNNPP          | 5      | 0 |
| QNNNPPPP        | 1      | 0 |
| QNNNNP          | 1      | 0 |
| QB              | 4408   | 0 |
| QBP             | 11633  | 0 |
| QBPP            | 21929  | 0 |
| QBPPP           | 34278  | 0 |
| QBPPPP          | 40675  | 0 |
| QBPPPPP         | 36856  | 0 |
| QBPPPPPP        | 24905  | 0 |
| QBPPPPPPP       | 8455   | 0 |
| QBPPPPPPPP      | 396    | 0 |
| QBN             | 755    | 0 |
| QBNP            | 1421   | 0 |
| QBNPP           | 4293   | 0 |
| QBNPPP          | 8699   | 0 |
| QBNPPPP         | 14521  | 0 |
| QBNPPPPP        | 19293  | 0 |
| QBNPPPPPP       | 17503  | 0 |
| QBNPPPPPPP      | 7705   | 0 |
| QBNPPPPPPPP     | 208    | 0 |
| QBNN            | 13     | 0 |
| QBNNP           | 79     | 0 |
| QBNNPP          | 360    | 0 |
| QBNNPPP         | 899    | 0 |
| QBNNPPPP        | 1984   | 0 |
| QBNNPPPPP       | 3177   | 0 |
| QBNNPPPPPP      | 3491   | 0 |
| QBNNPPPPPPP     | 2358   | 0 |
| QBNNPPPPPPPP    | 43     | 0 |
| QBNNNP          | 1      | 0 |
| QBB             | 165    | 0 |
| QBBP            | 500    | 0 |
| QBBPP           | 1001   | 0 |
| QBBPPP          | 2524   | 0 |
| QBBPPPP         | 4818   | 0 |
| QBBPPPPP        | 6532   | 0 |
| QBBPPPPPP       | 6361   | 0 |
| QBBPPPPPPP      | 2767   | 0 |
| QBBPPPPPPPP     | 121    | 0 |
| QBBN            | 44     | 0 |
| QBBNP           | 90     | 0 |
| QBBNPP          | 301    | 0 |
| QBBNPPP         | 1125   | 0 |
| QBBNPPPP        | 2591   | 0 |
| QBBNPPPPP       | 4205   | 0 |
| QBBNPPPPPP      | 4813   | 0 |
| QBBNPPPPPPP     | 2669   | 0 |
| QBBNPPPPPPPP    | 38     | 0 |
| QBBNN           | 1      | 0 |
| QBBNNP          | 9      | 0 |
| QBBNNPP         | 63     | 0 |
| QBBNNPPP        | 224    | 0 |
| QBBNNPPPP       | 535    | 0 |
| QBBNNPPPPP      | 978    | 0 |
| QBBNNPPPPPP     | 1352   | 0 |
| QBBNNPPPPPPP    | 1097   | 0 |
| QBBNNPPPPPPPP   | 13     | 0 |
| QBBBPP          | 2      | 0 |
| QBBBNP          | 1      | 0 |
| QR              | 8135   | 0 |
| QRP             | 19397  | 0 |
| QRPP            | 33772  | 0 |
| QRPPP           | 56460  | 0 |
| QRPPPP          | 77376  | 1 |
| QRPPPPP         | 83151  | 1 |
| QRPPPPPP        | 60315  | 0 |
| QRPPPPPPP       | 20569  | 0 |
| QRPPPPPPPP      | 1368   | 0 |
| QRN             | 610    | 0 |
| QRNP            | 2750   | 0 |
| QRNPP           | 8114   | 0 |
| QRNPPP          | 22551  | 0 |
| QRNPPPP         | 45224  | 0 |
| QRNPPPPP        | 66331  | 0 |
| QRNPPPPPP       | 62510  | 0 |
| QRNPPPPPPP      | 25129  | 0 |
| QRNPPPPPPPP     | 1897   | 0 |
| QRNN            | 21     | 0 |
| QRNNP           | 151    | 0 |
| QRNNPP          | 787    | 0 |
| QRNNPPP         | 2934   | 0 |
| QRNNPPPP        | 7800   | 0 |
| QRNNPPPPP       | 14529  | 0 |
| QRNNPPPPPP      | 16596  | 0 |
| QRNNPPPPPPP     | 8776   | 0 |
| QRNNPPPPPPPP    | 848    | 0 |
| QRNNN           | 1      | 0 |
| QRNNNP          | 1      | 0 |
| QRNNNPPPPP      | 2      | 0 |
| QRB             | 1022   | 0 |
| QRBP            | 4388   | 0 |
| QRBPP           | 12945  | 0 |
| QRBPPP          | 34504  | 0 |
| QRBPPPP         | 69752  | 1 |
| QRBPPPPP        | 97024  | 1 |
| QRBPPPPPP       | 86071  | 1 |
| QRBPPPPPPP      | 33524  | 0 |
| QRBPPPPPPPP     | 2542   | 0 |
| QRBN            | 107    | 0 |
| QRBNP           | 951    | 0 |
| QRBNPP          | 4455   | 0 |
| QRBNPPP         | 16649  | 0 |
| QRBNPPPP        | 43838  | 0 |
| QRBNPPPPP       | 79109  | 1 |
| QRBNPPPPPP      | 88259  | 1 |
| QRBNPPPPPPP     | 42053  | 0 |
| QRBNPPPPPPPP    | 3639   | 0 |
| QRBNN           | 10     | 0 |
| QRBNNP          | 90     | 0 |
| QRBNNPP         | 631    | 0 |
| QRBNNPPP        | 2988   | 0 |
| QRBNNPPPP       | 9621   | 0 |
| QRBNNPPPPP      | 20764  | 0 |
| QRBNNPPPPPP     | 28260  | 0 |
| QRBNNPPPPPPP    | 17215  | 0 |
| QRBNNPPPPPPPP   | 1735   | 0 |
| QRBNNNPP        | 1      | 0 |
| QRBNNNPPP       | 2      | 0 |
| QRBNNNPPPP      | 1      | 0 |
| QRBNNNPPPPP     | 1      | 0 |
| QRBB            | 58     | 0 |
| QRBBP           | 361    | 0 |
| QRBBPP          | 1403   | 0 |
| QRBBPPP         | 5452   | 0 |
| QRBBPPPP        | 15278  | 0 |
| QRBBPPPPP       | 27067  | 0 |
| QRBBPPPPPP      | 30000  | 0 |
| QRBBPPPPPPP     | 13859  | 0 |
| QRBBPPPPPPPP    | 1160   | 0 |
| QRBBN           | 12     | 0 |
| QRBBNP          | 108    | 0 |
| QRBBNPP         | 715    | 0 |
| QRBBNPPP        | 3966   | 0 |
| QRBBNPPPP       | 12792  | 0 |
| QRBBNPPPPP      | 28466  | 0 |
| QRBBNPPPPPP     | 38009  | 0 |
| QRBBNPPPPPPP    | 20590  | 0 |
| QRBBNPPPPPPPP   | 1634   | 0 |
| QRBBNN          | 1      | 0 |
| QRBBNNP         | 17     | 0 |
| QRBBNNPP        | 167    | 0 |
| QRBBNNPPP       | 920    | 0 |
| QRBBNNPPPP      | 3767   | 0 |
| QRBBNNPPPPP     | 10300  | 0 |
| QRBBNNPPPPPP    | 16430  | 0 |
| QRBBNNPPPPPPP   | 11821  | 0 |
| QRBBNNPPPPPPPP  | 895    | 0 |
| QRBBNNNPP       | 1      | 0 |
| QRBBNNNPPPP     | 2      | 0 |
| QRBBBPPP        | 1      | 0 |
| QRBBBNPPP       | 1      | 0 |
| QRR             | 535    | 0 |
| QRRP            | 1915   | 0 |
| QRRPP           | 7284   | 0 |
| QRRPPP          | 22398  | 0 |
| QRRPPPP         | 49281  | 0 |
| QRRPPPPP        | 81831  | 1 |
| QRRPPPPPP       | 93493  | 1 |
| QRRPPPPPPP      | 58578  | 0 |
| QRRPPPPPPPP     | 9976   | 0 |
| QRRN            | 162    | 0 |
| QRRNP           | 581    | 0 |
| QRRNPP          | 3045   | 0 |
| QRRNPPP         | 13209  | 0 |
| QRRNPPPP        | 39899  | 0 |
| QRRNPPPPP       | 91339  | 1 |
| QRRNPPPPPP      | 140852 | 2 |
| QRRNPPPPPPP     | 115463 | 1 |
| QRRNPPPPPPPP    | 25549  | 0 |
| QRRNN           | 5      | 0 |
| QRRNNP          | 50     | 0 |
| QRRNNPP         | 376    | 0 |
| QRRNNPPP        | 2262   | 0 |
| QRRNNPPPP       | 9353   | 0 |
| QRRNNPPPPP      | 27216  | 0 |
| QRRNNPPPPPP     | 54708  | 0 |
| QRRNNPPPPPPP    | 62012  | 0 |
| QRRNNPPPPPPPP   | 19238  | 0 |
| QRRNNNPP        | 1      | 0 |
| QRRNNNNPPP      | 1      | 0 |
| QRRB            | 117    | 0 |
| QRRBP           | 861    | 0 |
| QRRBPP          | 4531   | 0 |
| QRRBPPP         | 19234  | 0 |
| QRRBPPPP        | 58562  | 0 |
| QRRBPPPPP       | 130154 | 1 |
| QRRBPPPPPP      | 192878 | 2 |
| QRRBPPPPPPP     | 151621 | 2 |
| QRRBPPPPPPPP    | 30336  | 0 |
| QRRBN           | 28     | 0 |
| QRRBNP          | 287    | 0 |
| QRRBNPP         | 2378   | 0 |
| QRRBNPPP        | 12972  | 0 |
| QRRBNPPPP       | 51618  | 0 |
| QRRBNPPPPP      | 152706 | 2 |
| QRRBNPPPPPP     | 308837 | 4 |
| QRRBNPPPPPPP    | 323797 | 4 |
| QRRBNPPPPPPPP   | 81017  | 1 |
| QRRBNN          | 2      | 0 |
| QRRBNNP         | 40     | 0 |
| QRRBNNPP        | 461    | 0 |
| QRRBNNPPP       | 2993   | 0 |
| QRRBNNPPPP      | 14792  | 0 |
| QRRBNNPPPPP     | 55554  | 0 |
| QRRBNNPPPPPP    | 145142 | 2 |
| QRRBNNPPPPPPP   | 205470 | 3 |
| QRRBNNPPPPPPPP  | 68644  | 1 |
| QRRBNNNNPPP     | 1      | 0 |
| QRRBB           | 10     | 0 |
| QRRBBP          | 91     | 0 |
| QRRBBPP         | 793    | 0 |
| QRRBBPPP        | 4389   | 0 |
| QRRBBPPPP       | 17515  | 0 |
| QRRBBPPPPP      | 51745  | 0 |
| QRRBBPPPPPP     | 102524 | 1 |
| QRRBBPPPPPPP    | 100268 | 1 |
| QRRBBPPPPPPPP   | 23359  | 0 |
| QRRBBN          | 1      | 0 |
| QRRBBNP         | 54     | 0 |
| QRRBBNPP        | 597    | 0 |
| QRRBBNPPP       | 3975   | 0 |
| QRRBBNPPPP      | 20278  | 0 |
| QRRBBNPPPPP     | 77645  | 1 |
| QRRBBNPPPPPP    | 207543 | 3 |
| QRRBBNPPPPPPP   | 263394 | 3 |
| QRRBBNPPPPPPPP  | 71456  | 1 |
| QRRBBNN         | 1      | 0 |
| QRRBBNNP        | 12     | 0 |
| QRRBBNNPP       | 146    | 0 |
| QRRBBNNPPP      | 1270   | 0 |
| QRRBBNNPPPP     | 7744   | 0 |
| QRRBBNNPPPPP    | 35984  | 0 |
| QRRBBNNPPPPPP   | 123036 | 1 |
| QRRBBNNPPPPPPP  | 204689 | 3 |
| QRRBBNNPPPPPPPP | 81615  | 1 |
| QRRBBNNNPPPPPP  | 1      | 0 |
| QRRBBNNNPPPPPPP | 1      | 0 |
| QRRBBNNNNPPPPPP | 2      | 0 |
| QRRBBBPPP       | 1      | 0 |
| QRRBBBPPPPP     | 1      | 0 |
| QRRBBBNNPPPPPP  | 1      | 0 |
| QRRRP           | 1      | 0 |
| QRRRPP          | 2      | 0 |
| QRRRPPPP        | 2      | 0 |
| QRRRPPPPPPP     | 1      | 0 |
| QRRRNPPP        | 1      | 0 |
| QRRRBPP         | 1      | 0 |
| QRRRBPPPPP      | 2      | 0 |
| QRRRBPPPPPP     | 2      | 0 |
| QRRRBNPPPP      | 2      | 0 |
| QRRRBNPPPPPP    | 2      | 0 |
| QRRRBNNPPPP     | 2      | 0 |
| QRRRBBNPPPP     | 1      | 0 |
| QRRRBBNPPPPP    | 1      | 0 |
| QRRRBBNNPPPPPPP | 1      | 0 |
| QQ              | 779    | 0 |
| QQP             | 1562   | 0 |
| QQPP            | 1912   | 0 |
| QQPPP           | 1435   | 0 |
| QQPPPP          | 699    | 0 |
| QQPPPPP         | 304    | 0 |
| QQPPPPPP        | 57     | 0 |
| QQPPPPPPP       | 2      | 0 |
| QQN             | 76     | 0 |
| QQNP            | 201    | 0 |
| QQNPP           | 202    | 0 |
| QQNPPP          | 230    | 0 |
| QQNPPPP         | 141    | 0 |
| QQNPPPPP        | 53     | 0 |
| QQNPPPPPP       | 14     | 0 |
| QQNN            | 1      | 0 |
| QQNNP           | 1      | 0 |
| QQNNPP          | 9      | 0 |
| QQNNPPP         | 12     | 0 |
| QQNNPPPP        | 8      | 0 |
| QQNNPPPPP       | 1      | 0 |
| QQNNPPPPPP      | 1      | 0 |
| QQB             | 95     | 0 |
| QQBP            | 379    | 0 |
| QQBPP           | 452    | 0 |
| QQBPPP          | 353    | 0 |
| QQBPPPP         | 228    | 0 |
| QQBPPPPP        | 85     | 0 |
| QQBPPPPPP       | 32     | 0 |
| QQBPPPPPPP      | 5      | 0 |
| QQBN            | 7      | 0 |
| QQBNP           | 25     | 0 |
| QQBNPP          | 50     | 0 |
| QQBNPPP         | 36     | 0 |
| QQBNPPPP        | 38     | 0 |
| QQBNPPPPP       | 18     | 0 |
| QQBNPPPPPP      | 5      | 0 |
| QQBNPPPPPPP     | 2      | 0 |
| QQBNNPP         | 1      | 0 |
| QQBNNPPP        | 4      | 0 |
| QQBNNPPPP       | 3      | 0 |
| QQBNNPPPPP      | 1      | 0 |
| QQBB            | 2      | 0 |
| QQBBP           | 3      | 0 |
| QQBBPP          | 4      | 0 |
| QQBBPPP         | 29     | 0 |
| QQBBPPPP        | 19     | 0 |
| QQBBPPPPP       | 10     | 0 |
| QQBBPPPPPP      | 5      | 0 |
| QQBBPPPPPPP     | 1      | 0 |
| QQBBNP          | 3      | 0 |
| QQBBNPP         | 2      | 0 |
| QQBBNPPP        | 5      | 0 |
| QQBBNPPPP       | 3      | 0 |
| QQBBNPPPPP      | 3      | 0 |
| QQBBNNPPPP      | 1      | 0 |
| QQBBNNPPPPPP    | 1      | 0 |
| QQR             | 151    | 0 |
| QQRP            | 294    | 0 |
| QQRPP           | 421    | 0 |
| QQRPPP          | 450    | 0 |
| QQRPPPP         | 386    | 0 |
| QQRPPPPP        | 165    | 0 |
| QQRPPPPPP       | 51     | 0 |
| QQRPPPPPPP      | 3      | 0 |
| QQRN            | 11     | 0 |
| QQRNP           | 37     | 0 |
| QQRNPP          | 56     | 0 |
| QQRNPPP         | 81     | 0 |
| QQRNPPPP        | 66     | 0 |
| QQRNPPPPP       | 47     | 0 |
| QQRNPPPPPP      | 20     | 0 |
| QQRNPPPPPPP     | 2      | 0 |
| QQRNNPP         | 3      | 0 |
| QQRNNPPP        | 6      | 0 |
| QQRNNPPPP       | 12     | 0 |
| QQRNNPPPPP      | 11     | 0 |
| QQRNNPPPPPP     | 2      | 0 |
| QQRB            | 16     | 0 |
| QQRBP           | 53     | 0 |
| QQRBPP          | 123    | 0 |
| QQRBPPP         | 220    | 0 |
| QQRBPPPP        | 184    | 0 |
| QQRBPPPPP       | 106    | 0 |
| QQRBPPPPPP      | 28     | 0 |
| QQRBPPPPPPP     | 5      | 0 |
| QQRBN           | 1      | 0 |
| QQRBNP          | 2      | 0 |
| QQRBNPP         | 16     | 0 |
| QQRBNPPP        | 43     | 0 |
| QQRBNPPPP       | 66     | 0 |
| QQRBNPPPPP      | 50     | 0 |
| QQRBNPPPPPP     | 10     | 0 |
| QQRBNPPPPPPP    | 4      | 0 |
| QQRBNNP         | 1      | 0 |
| QQRBNNPPP       | 9      | 0 |
| QQRBNNPPPP      | 9      | 0 |
| QQRBNNPPPPP     | 9      | 0 |
| QQRBNNPPPPPP    | 8      | 0 |
| QQRBBP          | 3      | 0 |
| QQRBBPP         | 4      | 0 |
| QQRBBPPP        | 26     | 0 |
| QQRBBPPPP       | 23     | 0 |
| QQRBBPPPPP      | 9      | 0 |
| QQRBBPPPPPP     | 5      | 0 |
| QQRBBNP         | 1      | 0 |
| QQRBBNPP        | 2      | 0 |
| QQRBBNPPP       | 7      | 0 |
| QQRBBNPPPP      | 9      | 0 |
| QQRBBNPPPPP     | 14     | 0 |
| QQRBBNPPPPPP    | 6      | 0 |
| QQRBBNPPPPPPP   | 4      | 0 |
| QQRBBNNPPP      | 1      | 0 |
| QQRBBNNPPPP     | 3      | 0 |
| QQRBBNNPPPPP    | 3      | 0 |
| QQRBBNNPPPPPP   | 3      | 0 |
| QQRBBNNPPPPPPP  | 1      | 0 |
| QQRR            | 9      | 0 |
| QQRRP           | 21     | 0 |
| QQRRPP          | 70     | 0 |
| QQRRPPP         | 118    | 0 |
| QQRRPPPP        | 130    | 0 |
| QQRRPPPPP       | 101    | 0 |
| QQRRPPPPPP      | 40     | 0 |
| QQRRPPPPPPP     | 9      | 0 |
| QQRRNP          | 6      | 0 |
| QQRRNPP         | 17     | 0 |
| QQRRNPPP        | 29     | 0 |
| QQRRNPPPP       | 56     | 0 |
| QQRRNPPPPP      | 46     | 0 |
| QQRRNPPPPPP     | 23     | 0 |
| QQRRNPPPPPPP    | 6      | 0 |
| QQRRNNPP        | 3      | 0 |
| QQRRNNPPP       | 2      | 0 |
| QQRRNNPPPP      | 8      | 0 |
| QQRRNNPPPPP     | 7      | 0 |
| QQRRNNPPPPPP    | 6      | 0 |
| QQRRNNPPPPPPP   | 3      | 0 |
| QQRRBP          | 10     | 0 |
| QQRRBPP         | 35     | 0 |
| QQRRBPPP        | 72     | 0 |
| QQRRBPPPP       | 68     | 0 |
| QQRRBPPPPP      | 81     | 0 |
| QQRRBPPPPPP     | 36     | 0 |
| QQRRBPPPPPPP    | 6      | 0 |
| QQRRBNPP        | 6      | 0 |
| QQRRBNPPP       | 18     | 0 |
| QQRRBNPPPP      | 47     | 0 |
| QQRRBNPPPPP     | 71     | 0 |
| QQRRBNPPPPPP    | 45     | 0 |
| QQRRBNPPPPPPP   | 17     | 0 |
| QQRRBNNPP       | 1      | 0 |
| QQRRBNNPPP      | 7      | 0 |
| QQRRBNNPPPP     | 2      | 0 |
| QQRRBNNPPPPP    | 10     | 0 |
| QQRRBNNPPPPPP   | 24     | 0 |
| QQRRBNNPPPPPPP  | 11     | 0 |
| QQRRBBPPP       | 6      | 0 |
| QQRRBBPPPP      | 15     | 0 |
| QQRRBBPPPPP     | 12     | 0 |
| QQRRBBPPPPPP    | 14     | 0 |
| QQRRBBPPPPPPP   | 3      | 0 |
| QQRRBBNPP       | 1      | 0 |
| QQRRBBNPPP      | 3      | 0 |
| QQRRBBNPPPP     | 14     | 0 |
| QQRRBBNPPPPP    | 18     | 0 |
| QQRRBBNPPPPPP   | 19     | 0 |
| QQRRBBNPPPPPPP  | 8      | 0 |
| QQRRBBNNPPP     | 3      | 0 |
| QQRRBBNNPPPP    | 4      | 0 |
| QQRRBBNNPPPPP   | 4      | 0 |
| QQRRBBNNPPPPPP  | 7      | 0 |
| QQRRBBNNPPPPPPP | 13     | 0 |
| QQRRRPPPP       | 1      | 0 |
| QQRRRBNNPPP     | 1      | 0 |
| QQQ             | 1      | 0 |
| QQQP            | 11     | 0 |
| QQQPP           | 11     | 0 |
| QQQPPP          | 9      | 0 |
| QQQPPPP         | 4      | 0 |
| QQQNP           | 2      | 0 |
| QQQNPP          | 1      | 0 |
| QQQBP           | 2      | 0 |
| QQQBPP          | 4      | 0 |
| QQQBPPPP        | 1      | 0 |
| QQQBPPPPP       | 1      | 0 |
| QQQBBPPP        | 1      | 0 |
| QQQRP           | 6      | 0 |
| QQQRPP          | 2      | 0 |
| QQQRPPP         | 4      | 0 |
| QQQRNP          | 3      | 0 |
| QQQRNPP         | 2      | 0 |
| QQQRNPPP        | 1      | 0 |
| QQQRNPPPP       | 1      | 0 |
| QQQRBPPP        | 1      | 0 |
| QQQRBPPPP       | 1      | 0 |
| QQQRBNP         | 1      | 0 |
| QQQRBNPP        | 3      | 0 |
| QQQRBNPPP       | 2      | 0 |
| QQQRRPP         | 1      | 0 |
| QQQRRPPP        | 1      | 0 |
| QQQRRBPPP       | 2      | 0 |
| QQQRRBPPPP      | 1      | 0 |
| QQQRRBBNNPPPP   | 1      | 0 |
| QQQRRBBNNPPPPPP | 1      | 0 |
| QQQQP           | 1      | 0 |
| QQQQPP          | 1      | 0 |
| QQQQRP          | 2      | 0 |
| QQQQRPP         | 2      | 0 |
| QQQQQP          | 1      | 0 |
| QQQQQNPP        | 1      | 0 |
| QQQQQRP         | 1      | 0 |
| QQQQQRNP        | 1      | 0 |
| QQQQQRRBNP      | 1      | 0 |
| QQQQQRRBBNNPP   | 1      | 0 |
| QQQQQQRNPP      | 1      | 0 |
| QQQQQQQRP       | 1      | 0 |


K adjusted to: 1.4500000000000002

## new optimization round

### Optimizing with step 1

Error at start: 0.07433183174689109

| Duration | Round | Step | Params adjustments | Adj total | Curr Error | Overall AdjPerSecond | AdjPerSecond |
|----------|-------|------|--------------------|-----------|------------|----------------------|--------------|
| 0:05:15,107 | 100   | 1    | 60                 | 60        | 0.07430866727480515 | 0.1910828025477707   | 0.1910828025477707 |
| 0:10:03,814 | 200   | 1    | 61                 | 121       | 0.0743008826285039  | 0.20066334991708126  | 0.21180555555555555 |
| 0:14:12,839 | 300   | 1    | 57                 | 178       | 0.07429454763762389 | 0.20892018779342722  | 0.22983870967741934 |
| 0:18:45,582 | 400   | 1    | 51                 | 229       | 0.07428384527373294 | 0.20355555555555555  | 0.1875              |
| 0:22:23,722 | 500   | 1    | 57                 | 286       | 0.07426250560514866 | 0.21295606850335072  | 0.26146788990825687 |
| 0:26:20,049 | 600   | 1    | 64                 | 350       | 0.07423985102499853 | 0.22165927802406588  | 0.2711864406779661  |
| 0:30:33,640 | 700   | 1    | 45                 | 395       | 0.07422622728603746 | 0.215493726132024    | 0.17786561264822134 |
| 0:34:43,352 | 800   | 1    | 33                 | 428       | 0.07421459490657224 | 0.2054728756601056   | 0.13253012048192772 |
| 0:38:41,498 | 900   | 1    | 30                 | 458       | 0.07421182238627277 | 0.1973287376130978   | 0.12605042016806722 |
| 0:43:34,586 | 1000  | 1    | 40                 | 498       | 0.074204987265588   | 0.19051262433052793  | 0.136986301369863   |
| 0:49:41,124 | 1100  | 1    | 47                 | 545       | 0.07420095379926073 | 0.18288590604026847  | 0.1284153005464481  |
| 0:55:56,226 | 1200  | 1    | 41                 | 586       | 0.07419432615955684 | 0.17461263408820024  | 0.10933333333333334 |
| 1:02:18,335 | 1300  | 1    | 33                 | 619       | 0.07417771967902968 | 0.16559657570893527  | 0.08638743455497382 |
| 1:08:05,484 | 1400  | 1    | 45                 | 664       | 0.07417367332323602 | 0.16254589963280294  | 0.12968299711815562 |
| 1:13:36,056 | 1500  | 1    | 33                 | 697       | 0.07417066050345267 | 0.1578708946772367   | 0.1                 |
| 1:17:24,012 | 1600  | 1    | 37                 | 734       | 0.07414606720231594 | 0.15808744346327805  | 0.16299559471365638 |
| 1:20:54,608 | 1700  | 1    | 63                 | 797       | 0.07398826919645751 | 0.16419447878038732  | 0.3                 |
| 1:24:58,828 | 1800  | 1    | 47                 | 844       | 0.07396889422612749 | 0.16555511965476657  | 0.19262295081967212 |
| 1:28:36,568 | 1900  | 1    | 61                 | 905       | 0.07396286660496162 | 0.1702407825432656   | 0.28110599078341014 |
| 1:32:29,605 | 2000  | 1    | 54                 | 959       | 0.07395865783119251 | 0.17282393224004325  | 0.2317596566523605  |
| 1:36:03,839 | 2100  | 1    | 45                 | 1004      | 0.07395154725742305 | 0.17421481867083116  | 0.2102803738317757  |
| 1:39:16,208 | 2200  | 1    | 47                 | 1051      | 0.07393955585249617 | 0.1764607118871726   | 0.24479166666666666 |
| 1:42:47,955 | 2300  | 1    | 50                 | 1101      | 0.07392529635152477 | 0.17853089022215016  | 0.23696682464454977 |
| 1:46:51,816 | 2400  | 1    | 45                 | 1146      | 0.0739142711179887  | 0.17875526438933084  | 0.18518518518518517 |
| 1:50:56,792 | 2500  | 1    | 35                 | 1181      | 0.0739059055076282  | 0.17743389423076922  | 0.14344262295081966 |
| 1:54:54,333 | 2600  | 1    | 22                 | 1203      | 0.07390466945965166 | 0.17449956483899043  | 0.09282700421940929 |
| 1:59:00,483 | 2700  | 1    | 32                 | 1235      | 0.07390158034592854 | 0.17296918767507002  | 0.13008130081300814 |
| 2:03:06,568 | 2800  | 1    | 24                 | 1259      | 0.07390048701892837 | 0.17045762252910912  | 0.0975609756097561  |
| 2:07:24,281 | 2900  | 1    | 27                 | 1286      | 0.07389865751089644 | 0.16823652537938252  | 0.10505836575875487 |
