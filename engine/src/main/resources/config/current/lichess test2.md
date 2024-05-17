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
| stepGranularity              | [2, 1]                       |
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


K adjusted to: 1.4300000000000002

## new optimization round

### Optimizing with step 2

Error at start: 0.07389726656533373

| Duration | Round | Step | Params adjustments | Adj total | Curr Error | Overall AdjPerSecond | AdjPerSecond |
|----------|-------|------|--------------------|-----------|------------|----------------------|--------------|
| 0:03:18,005 | 100   | 2    | 37                 | 37        | 0.07388596755057653 | 0.18781725888324874  | 0.18781725888324874 |
# Tuning Options

| Parameter                    | Value                        |
|------------------------------|------------------------------|
| adjustK                      | false                        |
| delta                        | 1.0E-8                       |
| evalParamSet                 | CURRENT                      |
| geneticParams                | null                         |
| inputFiles                   | [C:\projekte\cygwin_home\mla\jackyChessDockerTesting\tuningdata\lichess-big3-resolved.book]|
| k                            | 1.43                         |
| multiThreading               | true                         |
| name                         | lichess test2                |
| optimizeRecalcOnlyDependendFens| false                        |
| outputdir                    | null                         |
| progressUpdatesInMinutes     | 0                            |
| removeDuplicateFens          | true                         |
| resetParametersBeforeTuning  | false                        |
| shuffleTuningParameter       | false                        |
| stepGranularity              | [2, 1]                       |
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
| Bishop     | 4966199 | 72  |
| Pawn       | 6819087 | 100 |
| Queen      | 3817373 | 55  |
| Knight     | 4456890 | 65  |
| Rook       | 5988879 | 87  |


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


setting K from Input Tuner Parameter to: 1.43

K: 1.43

## new optimization round

### Optimizing with step 2

Error at start: 0.07388596755057653

| Duration | Round | Step | Params adjustments | Adj total | Curr Error | Overall AdjPerSecond | AdjPerSecond |
|----------|-------|------|--------------------|-----------|------------|----------------------|--------------|
| 0:03:20,817 | 100   | 2    | 15                 | 15        | 0.07388472895973965 | 0.075                | 0.075        |
| 0:06:47,998 | 200   | 2    | 47                 | 62        | 0.07388003682550257 | 0.15233415233415235  | 0.22705314009661837 |
| 0:10:35,316 | 300   | 2    | 41                 | 103       | 0.07387633632929824 | 0.16220472440944883  | 0.18061674008810572 |
| 0:14:10,262 | 400   | 2    | 30                 | 133       | 0.07386998641293847 | 0.1564705882352941   | 0.14018691588785046 |
| 0:17:34,397 | 500   | 2    | 30                 | 163       | 0.07386535080033498 | 0.15464895635673626  | 0.14705882352941177 |
| 0:20:54,338 | 600   | 2    | 34                 | 197       | 0.07385440875621047 | 0.15709728867623604  | 0.1708542713567839  |
| 0:24:48,208 | 700   | 2    | 31                 | 228       | 0.07384488659342993 | 0.1532258064516129   | 0.13304721030042918 |
| 0:28:04,208 | 800   | 2    | 22                 | 250       | 0.0738376649570389  | 0.14845605700712589  | 0.11282051282051282 |
| 0:31:05,056 | 900   | 2    | 22                 | 272       | 0.0738359115477022  | 0.1459227467811159   | 0.12222222222222222 |
| 0:34:31,419 | 1000  | 2    | 20                 | 292       | 0.07383421582493185 | 0.14099468855625302  | 0.0970873786407767  |
| 0:38:04,920 | 1100  | 2    | 25                 | 317       | 0.07383237498106784 | 0.13879159369527144  | 0.11737089201877934 |
| 0:42:14,536 | 1200  | 2    | 16                 | 333       | 0.07383012297327848 | 0.1314127861089187   | 0.0642570281124498  |
| 0:46:44,524 | 1300  | 2    | 22                 | 355       | 0.073823441399315   | 0.12660485021398002  | 0.08178438661710037 |
| 0:50:27,290 | 1400  | 2    | 34                 | 389       | 0.07381861224699861 | 0.12851007598282127  | 0.15315315315315314 |
| 0:53:30,495 | 1500  | 2    | 32                 | 421       | 0.07381388526405656 | 0.1311526479750779   | 0.17486338797814208 |
| 0:56:34,088 | 1600  | 2    | 24                 | 445       | 0.07378626754640769 | 0.13111373011196228  | 0.13114754098360656 |
| 0:59:22,266 | 1700  | 2    | 48                 | 493       | 0.07362597830948821 | 0.13840539023020776  | 0.2857142857142857  |
| 1:02:32,901 | 1800  | 2    | 37                 | 530       | 0.07360837174582029 | 0.14125799573560768  | 0.19473684210526315 |
| 1:05:29,856 | 1900  | 2    | 39                 | 569       | 0.07360509263485339 | 0.14482056502926954  | 0.2215909090909091  |
| 1:08:31,533 | 2000  | 2    | 43                 | 612       | 0.07360150104983411 | 0.14886888834833373  | 0.23756906077348067 |
| 1:11:15,507 | 2100  | 2    | 53                 | 665       | 0.0735938526399221  | 0.15555555555555556  | 0.32515337423312884 |
| 1:14:11,307 | 2200  | 2    | 27                 | 692       | 0.07359025480032706 | 0.1554706807458998   | 0.15428571428571428 |
| 1:18:05,234 | 2300  | 2    | 17                 | 709       | 0.07358832702227884 | 0.15133404482390608  | 0.07296137339055794 |
| 1:21:47,283 | 2400  | 2    | 24                 | 733       | 0.07358586286622563 | 0.14937843896474423  | 0.10810810810810811 |
| 1:25:34,760 | 2500  | 2    | 24                 | 757       | 0.07358225586507558 | 0.14744838332684068  | 0.10572687224669604 |
| 1:29:12,218 | 2600  | 2    | 14                 | 771       | 0.0735813350709696  | 0.14405829596412556  | 0.06451612903225806 |
| 1:33:11,600 | 2700  | 2    | 22                 | 793       | 0.07357997788288306 | 0.14183509211232337  | 0.09205020920502092 |
| 1:37:13,821 | 2800  | 2    | 23                 | 816       | 0.07357845750986125 | 0.1398937082118978   | 0.09504132231404959 |
| 1:41:18,718 | 2900  | 2    | 14                 | 830       | 0.0735770132087946  | 0.13655807831523528  | 0.05737704918032787 |
| 1:45:21,485 | 3000  | 2    | 20                 | 850       | 0.07356709430882663 | 0.13447239360860624  | 0.08264462809917356 |
| 1:49:04,968 | 3100  | 2    | 38                 | 888       | 0.07356254287570352 | 0.1356968215158924   | 0.17040358744394618 |
| 1:53:06,397 | 3200  | 2    | 29                 | 917       | 0.0735609877316597  | 0.13513115237253168  | 0.12033195020746888 |
| 1:57:00,026 | 3300  | 2    | 24                 | 941       | 0.07355182999388396 | 0.13406468157857246  | 0.10300429184549356 |
| 2:00:40,031 | 3400  | 2    | 43                 | 984       | 0.07347206477362575 | 0.13593037712391215  | 0.19545454545454546 |
| 2:04:41,490 | 3500  | 2    | 37                 | 1021      | 0.07345372251401182 | 0.13647908033685335  | 0.15352697095435686 |
| 2:08:19,371 | 3600  | 2    | 40                 | 1061      | 0.07345083189094787 | 0.13781010520846862  | 0.18433179723502305 |
| 2:12:15,162 | 3700  | 2    | 43                 | 1104      | 0.07344847253165895 | 0.1391304347826087   | 0.1829787234042553  |
| 2:15:44,433 | 3800  | 2    | 55                 | 1159      | 0.0734421459088596  | 0.14231335952848723  | 0.2631578947368421  |
| 2:19:31,231 | 3900  | 2    | 25                 | 1184      | 0.07343799209646144 | 0.14144068808983395  | 0.11061946902654868 |
| 2:23:15,407 | 4000  | 2    | 23                 | 1207      | 0.07343536934064616 | 0.1404304828388598   | 0.10267857142857142 |
| 2:26:47,201 | 4100  | 2    | 34                 | 1241      | 0.07343034063789175 | 0.14091063926422165  | 0.16113744075829384 |
| 2:30:45,062 | 4200  | 2    | 21                 | 1262      | 0.07342771606553775 | 0.13954002653693057  | 0.08860759493670886 |
| 2:34:37,608 | 4300  | 2    | 12                 | 1274      | 0.07342686305931205 | 0.1373288778700011   | 0.05172413793103448 |
| 2:38:48,872 | 4400  | 2    | 13                 | 1287      | 0.07342637315881874 | 0.13507556675062973  | 0.05179282868525897 |
| 2:43:08,190 | 4500  | 2    | 26                 | 1313      | 0.07342365403122407 | 0.1341438496117695   | 0.10038610038610038 |
| 2:47:17,887 | 4600  | 2    | 10                 | 1323      | 0.07342220675163237 | 0.13181229451031184  | 0.040160642570281124 |
| 2:51:31,897 | 4700  | 2    | 17                 | 1340      | 0.0734171602576573  | 0.13021086386162667  | 0.06692913385826772  |
| 2:55:39,091 | 4800  | 2    | 29                 | 1369      | 0.07341401265269083 | 0.12991079901309546  | 0.11740890688259109  |
| 2:59:48,332 | 4900  | 2    | 28                 | 1397      | 0.07341215280629795 | 0.12949573600296627  | 0.11244979919678715  |
| 3:03:58,183 | 5000  | 2    | 13                 | 1410      | 0.07340906861210145 | 0.12774053270520022  | 0.05220883534136546  |
| 3:08:05,870 | 5100  | 2    | 30                 | 1440      | 0.07338345873772714 | 0.12760301284891448  | 0.1214574898785425   |
| 3:11:59,075 | 5200  | 2    | 35                 | 1475      | 0.07336548315541641 | 0.1280604271574926   | 0.15021459227467812  |
| 3:16:02,470 | 5300  | 2    | 27                 | 1502      | 0.0733640256639101  | 0.12769937085529673  | 0.1111111111111111   |
| 3:19:54,189 | 5400  | 2    | 45                 | 1547      | 0.07336145629593455 | 0.1289811572452893   | 0.19480519480519481  |
| 3:23:40,477 | 5500  | 2    | 50                 | 1597      | 0.0733574154492436  | 0.1306873977086743   | 0.22123893805309736  |
| 3:27:31,256 | 5600  | 2    | 15                 | 1612      | 0.07335588050539497 | 0.1294675126495864   | 0.06521739130434782  |
| 3:31:13,545 | 5700  | 2    | 17                 | 1629      | 0.07335450362932736 | 0.12854099266156396  | 0.07657657657657657  |
| 3:35:03,588 | 5800  | 2    | 23                 | 1652      | 0.07335151479601311 | 0.12803224056420986  | 0.1                  |
| 3:38:53,125 | 5900  | 2    | 21                 | 1673      | 0.07335045518782635 | 0.12738902002588898  | 0.09170305676855896  |
| 3:42:44,893 | 6000  | 2    | 7                  | 1680      | 0.07334992674879053 | 0.1257108650104759   | 0.030303030303030304 |
| 3:46:58,694 | 6100  | 2    | 14                 | 1694      | 0.07334933739377612 | 0.12439418416801293  | 0.05533596837944664  |
| 3:51:17,056 | 6200  | 2    | 18                 | 1712      | 0.07334829693100055 | 0.12337849524358604  | 0.06976744186046512  |
| 3:55:35,939 | 6300  | 2    | 14                 | 1726      | 0.0733476836561709  | 0.12210824195259994  | 0.05426356589147287  |
| 3:59:51,723 | 6400  | 2    | 19                 | 1745      | 0.0733427761499396  | 0.12125634076853589  | 0.07450980392156863  |
| 4:03:54,952 | 6500  | 2    | 26                 | 1771      | 0.07334017734461044 | 0.12101954352876862  | 0.10699588477366255  |
| 4:07:58,364 | 6600  | 2    | 29                 | 1800      | 0.07333863850851031 | 0.12098400322624009  | 0.11934156378600823  |
| 4:12:14,150 | 6700  | 2    | 15                 | 1815      | 0.07333416470959378 | 0.11992863750495573  | 0.058823529411764705 |
| 4:16:21,555 | 6800  | 2    | 20                 | 1835      | 0.07332870918935963 | 0.11930303621351017  | 0.08097165991902834  |
| 4:20:34,830 | 6900  | 2    | 29                 | 1864      | 0.07331498009364526 | 0.119227325060765    | 0.11462450592885376  |
| 4:24:35,686 | 7000  | 2    | 28                 | 1892      | 0.07331397100757173 | 0.11918110236220472  | 0.11666666666666667  |
| 4:28:57,027 | 7100  | 2    | 34                 | 1926      | 0.07331186069541257 | 0.11936043629152206  | 0.13026819923371646  |
| 4:33:02,686 | 7200  | 2    | 41                 | 1967      | 0.07330982152395657 | 0.12007080942497864  | 0.1673469387755102   |
| 4:37:22,788 | 7300  | 2    | 7                  | 1974      | 0.07330939384891873 | 0.11861555101550295  | 0.026923076923076925 |
| 4:41:31,960 | 7400  | 2    | 11                 | 1985      | 0.07330849231088904 | 0.11751820496122195  | 0.04417670682730924  |
| 4:45:51,251 | 7500  | 2    | 16                 | 2001      | 0.07330778480377075 | 0.11666958194857442  | 0.06177606177606178  |
| 4:49:56,182 | 7600  | 2    | 21                 | 2022      | 0.0733063654245652  | 0.11623361692343068  | 0.0860655737704918   |
| 4:54:08,209 | 7700  | 2    | 4                  | 2026      | 0.07330624257004464 | 0.11480054397098821  | 0.015873015873015872 |
| 4:58:47,072 | 7800  | 2    | 14                 | 2040      | 0.07330574097741505 | 0.11379483460701735  | 0.050359712230215826 |
| 5:03:17,270 | 7900  | 2    | 17                 | 2057      | 0.07330514471394184 | 0.11304061108974006  | 0.06296296296296296  |
| 5:08:00,954 | 8000  | 2    | 13                 | 2070      | 0.07330447512152173 | 0.11201298701298701  | 0.045936395759717315 |
| 5:12:45,119 | 8100  | 2    | 4                  | 2074      | 0.07330327046952724 | 0.11052491340261124  | 0.014084507042253521 |
| 5:17:23,310 | 8200  | 2    | 9                  | 2083      | 0.07330250752167103 | 0.10938402562621435  | 0.03237410071942446  |
| 5:21:55,414 | 8300  | 2    | 23                 | 2106      | 0.07330131357483444 | 0.10903442920010355  | 0.08455882352941177  |
| 5:26:49,352 | 8400  | 2    | 6                  | 2112      | 0.07330075033605142 | 0.10770564536692336  | 0.020477815699658702 |
| 5:31:24,429 | 8500  | 2    | 10                 | 2122      | 0.07329968570832253 | 0.10671897002615167  | 0.03636363636363636  |
| 5:35:51,761 | 8600  | 2    | 24                 | 2146      | 0.07328665955614541 | 0.10649595553570543  | 0.0898876404494382   |
| 5:40:17,126 | 8700  | 2    | 18                 | 2164      | 0.07328613548873429 | 0.10599010628397904  | 0.06792452830188679  |
| 5:44:50,351 | 8800  | 2    | 27                 | 2191      | 0.07328465573478271 | 0.10589656839052683  | 0.0989010989010989   |
| 5:49:05,745 | 8900  | 2    | 30                 | 2221      | 0.07328339900395257 | 0.10603962759608498  | 0.11764705882352941  |
| 5:52:57,600 | 9000  | 2    | 7                  | 2228      | 0.07328222468064603 | 0.10520848089908863  | 0.030303030303030304 |
| 5:56:39,144 | 9100  | 2    | 7                  | 2235      | 0.07328171592316315 | 0.1044441329034067   | 0.03167420814479638  |
| 6:00:35,790 | 9200  | 2    | 5                  | 2240      | 0.07328134536463321 | 0.1035359371388953   | 0.0211864406779661   |
| 6:04:21,889 | 9300  | 2    | 13                 | 2253      | 0.07328086333823212 | 0.10306024427061891  | 0.05752212389380531  |
| 6:08:03,934 | 9400  | 2    | 2                  | 2255      | 0.07328048284886127 | 0.10211474890187022  | 0.009009009009009009 |
| 6:12:17,402 | 9500  | 2    | 14                 | 2269      | 0.07327989472155767 | 0.10158033755652057  | 0.05533596837944664  |
| 6:16:25,350 | 9600  | 2    | 8                  | 2277      | 0.07327961425821597 | 0.10081912773965021  | 0.032388663967611336 |
| 6:20:30,088 | 9700  | 2    | 9                  | 2286      | 0.07327938832541879 | 0.10013140604467806  | 0.036885245901639344 |
| 6:24:42,240 | 9800  | 2    | 10                 | 2296      | 0.07327724212283324 | 0.09947144961441816  | 0.03968253968253968  |
| 6:28:54,579 | 9900  | 2    | 10                 | 2306      | 0.0732768567131968  | 0.09882574783577612  | 0.03968253968253968  |
| 6:32:48,539 | 10000 | 2    | 19                 | 2325      | 0.07327588296858667 | 0.09865071283095724  | 0.0815450643776824   |
| 6:37:09,660 | 10100 | 2    | 4                  | 2329      | 0.07327573203836317 | 0.09773805027487516  | 0.01532567049808429  |
| 6:41:21,771 | 10200 | 2    | 4                  | 2333      | 0.07327559857513016 | 0.09688135874756032  | 0.015873015873015872 |
| 6:45:28,868 | 10300 | 2    | 22                 | 2355      | 0.07326437658836399 | 0.09680203880302532  | 0.08906882591093117  |
| 6:49:36,677 | 10400 | 2    | 15                 | 2370      | 0.0732633575191822  | 0.096435546875       | 0.06072874493927125  |
| 6:53:43,574 | 10500 | 2    | 21                 | 2391      | 0.07326234477836127 | 0.09632195947306933  | 0.08536585365853659  |
| 6:57:33,016 | 10600 | 2    | 22                 | 2413      | 0.07326163847963169 | 0.09631965511735589  | 0.09606986899563319  |
| 7:01:40,375 | 10700 | 2    | 2                  | 2415      | 0.0732614543640916  | 0.09545454545454546  | 0.008097165991902834 |
| 7:05:14,142 | 10800 | 2    | 5                  | 2420      | 0.0732613332184781  | 0.09484988633691306  | 0.023474178403755867 |
| 7:09:09,248 | 10900 | 2    | 6                  | 2426      | 0.07326118770953914 | 0.09421725115538468  | 0.02553191489361702  |
| 7:13:08,169 | 11000 | 2    | 5                  | 2431      | 0.07326107317115756 | 0.0935431737725104   | 0.02100840336134454  |
| 7:16:56,796 | 11100 | 2    | 1                  | 2432      | 0.07326105788865683 | 0.09276777540433323  | 0.0043859649122807015 |
| 7:21:09,576 | 11200 | 2    | 7                  | 2439      | 0.07326068121037459 | 0.09214552873172391  | 0.027777777777777776  |
| 7:25:24,113 | 11300 | 2    | 6                  | 2445      | 0.07326055296961607 | 0.09149079479119893  | 0.023622047244094488  |
| 7:29:43,646 | 11400 | 2    | 5                  | 2450      | 0.07326002146784846 | 0.09079790979505614  | 0.019305019305019305  |
| 7:34:01,268 | 11500 | 2    | 5                  | 2455      | 0.07325955373759094 | 0.09012150802099776  | 0.019455252918287938  |
| 7:38:10,167 | 11600 | 2    | 5                  | 2460      | 0.07325905236506647 | 0.08948708621316842  | 0.020161290322580645  |
| 7:42:03,576 | 11700 | 2    | 19                 | 2479      | 0.07325800312656831 | 0.08942033690437542  | 0.0815450643776824    |
| 7:46:17,141 | 11800 | 2    | 6                  | 2485      | 0.07325628716802356 | 0.08882296171855453  | 0.023715415019762844  |
| 7:50:38,103 | 11900 | 2    | 5                  | 2490      | 0.07325592049446057 | 0.08817904950775551  | 0.019230769230769232  |
| 7:54:50,871 | 12000 | 2    | 12                 | 2502      | 0.07324807892882404 | 0.08782028782028782  | 0.047619047619047616  |
| 7:58:55,271 | 12100 | 2    | 12                 | 2514      | 0.0732471172305775  | 0.08748912476074473  | 0.04918032786885246   |
| 8:03:04,884 | 12200 | 2    | 16                 | 2530      | 0.07324664198579897 | 0.08728953905603092  | 0.0642570281124498    |
| 8:07:15,691 | 12300 | 2    | 19                 | 2549      | 0.07324611990930752 | 0.08719001197195143  | 0.076                 |
| 8:11:28,046 | 12400 | 2    | 3                  | 2552      | 0.07324604566207184 | 0.08654661376199681  | 0.011904761904761904  |
| 8:15:10,571 | 12500 | 2    | 3                  | 2555      | 0.07324595586967536 | 0.08599798047795355  | 0.013513513513513514  |
| 8:19:01,222 | 12600 | 2    | 2                  | 2557      | 0.07324588868765688 | 0.08540128920209746  | 0.008695652173913044  |
| 8:23:03,743 | 12700 | 2    | 4                  | 2561      | 0.07324579371850463 | 0.08484908723453599  | 0.01652892561983471   |
| 8:26:50,399 | 12800 | 2    | 0                  | 2561      | 0.07324579371850463 | 0.08421571851364683  | 0.0                   |
| 8:31:02,716 | 12900 | 2    | 4                  | 2565      | 0.0732457259292225  | 0.08365403430956884  | 0.015873015873015872  |
| 8:35:17,416 | 13000 | 2    | 5                  | 2570      | 0.0732456437879666  | 0.08312578840120322  | 0.01968503937007874   |
| 8:39:34,092 | 13100 | 2    | 3                  | 2573      | 0.0732455055181956  | 0.08253672932572015  | 0.01171875            |
| 8:43:44,780 | 13200 | 2    | 5                  | 2578      | 0.07324519584186309 | 0.08203920570264765  | 0.02                  |
| 8:47:56,136 | 13300 | 2    | 2                  | 2580      | 0.07324510452549088 | 0.08144967798964516  | 0.00796812749003984   |
| 8:52:11,764 | 13400 | 2    | 11                 | 2591      | 0.07324470055669141 | 0.0811437161379224   | 0.043137254901960784  |
| 8:56:26,994 | 13500 | 2    | 7                  | 2598      | 0.0732441177427031  | 0.08071832473746349  | 0.027450980392156862  |
| 9:00:38,702 | 13600 | 2    | 4                  | 2602      | 0.07324373938097953 | 0.08021456316665639  | 0.01593625498007968   |
| 9:04:41,203 | 13700 | 2    | 19                 | 2621      | 0.07323680849521323 | 0.08019950429913406  | 0.07851239669421488   |
| 9:08:53,844 | 13800 | 2    | 7                  | 2628      | 0.07323591802590693 | 0.07979837852609845  | 0.027777777777777776  |
| 9:12:59,812 | 13900 | 2    | 18                 | 2646      | 0.07323532449343169 | 0.07974923897646101  | 0.07317073170731707   |
| 9:17:07,033 | 14000 | 2    | 11                 | 2657      | 0.07323500201087017 | 0.07948902052294621  | 0.044534412955465584  |
| 9:21:16,604 | 14100 | 2    | 8                  | 2665      | 0.07323474249479739 | 0.07913647701627272  | 0.0321285140562249    |
| 9:24:57,063 | 14200 | 2    | 1                  | 2666      | 0.07323473115335656 | 0.07865234835968846  | 0.004545454545454545  |
| 9:28:42,415 | 14300 | 2    | 4                  | 2670      | 0.07323460493055414 | 0.07824863724283454  | 0.017777777777777778  |
| 9:32:44,232 | 14400 | 2    | 5                  | 2675      | 0.0732344874448468  | 0.07784309160749622  | 0.02074688796680498   |
| 9:36:25,512 | 14500 | 2    | 1                  | 2676      | 0.07323447486107368 | 0.07737458435738037  | 0.004524886877828055  |
| 9:40:32,554 | 14600 | 2    | 3                  | 2679      | 0.0732343281380799  | 0.0769120349104272   | 0.012145748987854251  |
| 9:44:48,751 | 14700 | 2    | 1                  | 2680      | 0.07323431247761335 | 0.07637938896488829  | 0.00390625            |
| 9:48:56,694 | 14800 | 2    | 4                  | 2684      | 0.07323425070149854 | 0.07595653158252208  | 0.016194331983805668  |
| 9:53:01,870 | 14900 | 2    | 4                  | 2688      | 0.07323394336750787 | 0.07554593743852056  | 0.0163265306122449    |
| 9:57:08,537 | 15000 | 2    | 5                  | 2693      | 0.073233245092814   | 0.07516467567265826  | 0.02032520325203252   |
| 10:01:09,971 | 15100 | 2    | 9                  | 2702      | 0.07323273672720707 | 0.07491197427153512  | 0.03734439834024896   |
| 10:05:07,243 | 15200 | 2    | 8                  | 2710      | 0.07323221271708844 | 0.07464125375271986  | 0.03375527426160337   |
| 10:09:16,256 | 15300 | 2    | 1                  | 2711      | 0.07323215113890386 | 0.07416019258124522  | 0.004016064257028112  |
| 10:13:13,904 | 15400 | 2    | 12                 | 2723      | 0.0732265861887573  | 0.07400864294838692  | 0.05063291139240506   |
| 10:17:30,716 | 15500 | 2    | 8                  | 2731      | 0.07322352181314057 | 0.07371120107962213  | 0.03125               |
| 10:21:34,772 | 15600 | 2    | 12                 | 2743      | 0.07322317192984099 | 0.07355070520727194  | 0.04918032786885246   |
| 10:25:39,659 | 15700 | 2    | 7                  | 2750      | 0.07322291785683302 | 0.07325714590159567  | 0.028688524590163935  |
| 10:29:46,517 | 15800 | 2    | 7                  | 2757      | 0.07322278845383547 | 0.07296353146668078  | 0.028455284552845527  |
| 10:33:29,444 | 15900 | 2    | 5                  | 2762      | 0.07322267851947972 | 0.07266699992107133  | 0.02252252252252252   |
| 10:37:02,164 | 16000 | 2    | 2                  | 2764      | 0.07322260169681714 | 0.07231437392077861  | 0.009433962264150943  |
| 10:40:58,718 | 16100 | 2    | 5                  | 2769      | 0.07322245842134963 | 0.07200062405741328  | 0.0211864406779661    |
| 10:44:41,494 | 16200 | 2    | 0                  | 2769      | 0.07322245842134963 | 0.07158553294899304  | 0.0                   |
| 10:48:46,905 | 16300 | 2    | 4                  | 2773      | 0.07322230151512772 | 0.07123773313466578  | 0.0163265306122449    |
| 10:53:00,434 | 16400 | 2    | 6                  | 2779      | 0.07322217897324465 | 0.07092904543134253  | 0.023715415019762844  |
| 10:57:22,241 | 16500 | 2    | 4                  | 2783      | 0.07322195925361738 | 0.07055930226661934  | 0.01532567049808429   |
| 11:01:50,479 | 16600 | 2    | 2                  | 2785      | 0.07322171771061546 | 0.07013346764039285  | 0.007462686567164179  |
| 11:06:06,636 | 16700 | 2    | 4                  | 2789      | 0.07322097622600937 | 0.06978431666916879  | 0.015625              |
| 11:10:19,083 | 16800 | 2    | 11                 | 2800      | 0.07322049641811854 | 0.06961883686814689  | 0.04365079365079365   |
| 11:14:42,648 | 16900 | 2    | 9                  | 2809      | 0.07322006217545485 | 0.06938886418655205  | 0.034220532319391636  |
| 11:18:58,633 | 17000 | 2    | 1                  | 2810      | 0.073219440708437   | 0.06897736756836369  | 0.00392156862745098   |
| 11:23:13,842 | 17100 | 2    | 9                  | 2819      | 0.07321462944241704 | 0.0687678384114361   | 0.03529411764705882   |
| 11:27:43,133 | 17200 | 2    | 11                 | 2830      | 0.07321353120590864 | 0.06858444611395197  | 0.040892193308550186  |
| 11:32:02,857 | 17300 | 2    | 11                 | 2841      | 0.07321311706194475 | 0.06842155965512259  | 0.04247104247104247   |
| 11:36:23,253 | 17400 | 2    | 6                  | 2847      | 0.07321298630524091 | 0.0681377593758227   | 0.023076923076923078  |
| 11:40:45,111 | 17500 | 2    | 4                  | 2851      | 0.0732129057561009  | 0.06780830063027708  | 0.01532567049808429   |
| 11:44:34,897 | 17600 | 2    | 6                  | 2857      | 0.07321262685780555 | 0.06758291148223494  | 0.026200873362445413  |
| 11:48:23,385 | 17700 | 2    | 2                  | 2859      | 0.07321257718721005 | 0.06726584005834882  | 0.008771929824561403  |
| 11:52:36,828 | 17800 | 2    | 9                  | 2868      | 0.07321235440879109 | 0.06707830479932642  | 0.03557312252964427   |
| 11:56:41,057 | 17900 | 2    | 1                  | 2869      | 0.07321233226597366 | 0.06672093023255814  | 0.004098360655737705  |
| 12:00:53,521 | 18000 | 2    | 7                  | 2876      | 0.07321207830454922 | 0.06649249763022218  | 0.027777777777777776  |
| 12:05:21,147 | 18100 | 2    | 11                 | 2887      | 0.07321175375458963 | 0.06633579191654604  | 0.04119850187265917   |
| 12:09:50,882 | 18200 | 2    | 5                  | 2892      | 0.07321135551406652 | 0.06604247545101621  | 0.01858736059479554   |
| 12:14:15,465 | 18300 | 2    | 2                  | 2894      | 0.07321115685296495 | 0.06569061400522075  | 0.007575757575757576  |
| 12:18:40,809 | 18400 | 2    | 5                  | 2899      | 0.07321081986967724 | 0.06541064981949458  | 0.018867924528301886  |
| 12:23:07,341 | 18500 | 2    | 6                  | 2905      | 0.07321048375029772 | 0.06515352008432951  | 0.022556390977443608  |
| 12:27:27,847 | 18600 | 2    | 6                  | 2911      | 0.07321021980448005 | 0.06490958146587286  | 0.023076923076923078  |
| 12:31:46,060 | 18700 | 2    | 2                  | 2913      | 0.0732100032987995  | 0.06458120870837583  | 0.007751937984496124  |
| 12:36:06,529 | 18800 | 2    | 8                  | 2921      | 0.07320556753038829 | 0.06438742670722568  | 0.03076923076923077   |
| 12:40:27,026 | 18900 | 2    | 5                  | 2926      | 0.0732050196332391  | 0.06413010125805461  | 0.019230769230769232  |
| 12:44:52,287 | 19000 | 2    | 12                 | 2938      | 0.07320467649380656 | 0.06401987274470496  | 0.045283018867924525  |
| 12:49:08,295 | 19100 | 2    | 20                 | 2958      | 0.07320400120343012 | 0.06409811909508538  | 0.078125              |
| 12:53:29,255 | 19200 | 2    | 10                 | 2968      | 0.07320376053582213 | 0.06395311254282575  | 0.038461538461538464  |
| 12:57:31,284 | 19300 | 2    | 3                  | 2971      | 0.07320367754439029 | 0.06368566590212428  | 0.012396694214876033  |
| 13:01:35,836 | 19400 | 2    | 0                  | 2971      | 0.07320367754439029 | 0.06335430216440985  | 0.0                   |
| 13:05:40,340 | 19500 | 2    | 4                  | 2975      | 0.07320342647211253 | 0.06310988544760289  | 0.01639344262295082   |
| 13:09:58,589 | 19600 | 2    | 0                  | 2975      | 0.07320342647211253 | 0.0627663614498502   | 0.0                   |
| 13:13:56,220 | 19700 | 2    | 2                  | 2977      | 0.07320337396834306 | 0.06249475186833487  | 0.008438818565400843  |
| 13:18:15,758 | 19800 | 2    | 5                  | 2982      | 0.07320304156845356 | 0.06226119636705293  | 0.019305019305019305  |
| 13:22:38,784 | 19900 | 2    | 3                  | 2985      | 0.07320295924874713 | 0.06198347107438017  | 0.011406844106463879  |
| 13:27:08,932 | 20000 | 2    | 1                  | 2986      | 0.07320271799727081 | 0.06165854464359462  | 0.003703703703703704  |
| 13:31:29,339 | 20100 | 2    | 3                  | 2989      | 0.07320261807018813 | 0.0613896362628109   | 0.011538461538461539  |
| 13:35:51,748 | 20200 | 2    | 6                  | 2995      | 0.07320245001367778 | 0.06118363261220404  | 0.022900763358778626  |
| 13:40:06,356 | 20300 | 2    | 3                  | 2998      | 0.07320239080711763 | 0.06092752916311019  | 0.011811023622047244  |
| 13:44:19,344 | 20400 | 2    | 1                  | 2999      | 0.07320232187987628 | 0.060636082411694534 | 0.003968253968253968  |
| 13:48:28,352 | 20500 | 2    | 19                 | 3018      | 0.07319810707325061 | 0.06071457310694456  | 0.07630522088353414   |
| 13:52:42,276 | 20600 | 2    | 10                 | 3028      | 0.07319747650044664 | 0.06060606060606061  | 0.039525691699604744  |
| 13:56:56,751 | 20700 | 2    | 4                  | 3032      | 0.07319740312231518 | 0.06037916202007328  | 0.015748031496062992  |
| 14:01:05,048 | 20800 | 2    | 9                  | 3041      | 0.07319713586397358 | 0.06026077996195307  | 0.036290322580645164  |
| 14:05:20,983 | 20900 | 2    | 2                  | 3043      | 0.07319707695147895 | 0.059996056782334384 | 0.00784313725490196   |
| 14:09:14,228 | 21000 | 2    | 0                  | 3043      | 0.07319707695147895 | 0.059720532244769794 | 0.0                   |
| 14:13:13,695 | 21100 | 2    | 0                  | 3043      | 0.07319707695147895 | 0.059441720547731135 | 0.0                   |
| 14:17:13,591 | 21200 | 2    | 1                  | 3044      | 0.07319698249006215 | 0.059183792506756365 | 0.0041841004184100415 |
| 14:21:11,704 | 21300 | 2    | 0                  | 3044      | 0.07319698249006215 | 0.0589111880939018   | 0.0                   |
| 14:25:11,764 | 21400 | 2    | 1                  | 3045      | 0.07319697007999645 | 0.05865808788118125  | 0.004166666666666667  |
| 14:29:38,971 | 21500 | 2    | 1                  | 3046      | 0.07319695723228012 | 0.05837709379431944  | 0.003745318352059925  |
| 14:33:55,661 | 21600 | 2    | 2                  | 3048      | 0.07319692754316409 | 0.058129112234194716 | 0.0078125             |
| 14:38:18,751 | 21700 | 2    | 1                  | 3049      | 0.07319681859690506 | 0.057857983225169836 | 0.0038022813688212928 |
| 14:42:33,096 | 21800 | 2    | 6                  | 3055      | 0.07319643552206659 | 0.0576926708590637   | 0.023622047244094488  |
| 14:46:52,100 | 21900 | 2    | 0                  | 3055      | 0.07319643552206659 | 0.05741186198601819  | 0.0                   |
| 14:51:00,360 | 22000 | 2    | 5                  | 3060      | 0.07319635682345008 | 0.05723905723905724  | 0.020161290322580645  |
| 14:55:11,503 | 22100 | 2    | 0                  | 3060      | 0.07319635682345008 | 0.05697157006944574  | 0.0                   |
| 14:59:16,493 | 22200 | 2    | 11                 | 3071      | 0.07319278671821518 | 0.05691674697901994  | 0.045081967213114756  |
| 15:03:30,375 | 22300 | 2    | 3                  | 3074      | 0.07319258497349251 | 0.056705404906843754 | 0.011857707509881422  |
| 15:07:44,715 | 22400 | 2    | 3                  | 3077      | 0.07319250190226903 | 0.056496034077555816 | 0.011811023622047244  |
| 15:11:52,100 | 22500 | 2    | 4                  | 3081      | 0.07319240560443319 | 0.0563130574645416   | 0.016194331983805668  |
| 15:16:03,453 | 22600 | 2    | 5                  | 3086      | 0.07319234346747074 | 0.056146862434728814 | 0.0199203187250996    |
| 15:19:55,469 | 22700 | 2    | 3                  | 3089      | 0.07319214137065336 | 0.05596521424042033  | 0.01293103448275862   |
| 15:23:40,609 | 22800 | 2    | 6                  | 3095      | 0.07319198245422939 | 0.055846264886322626 | 0.02666666666666667   |
| 15:27:29,453 | 22900 | 2    | 3                  | 3098      | 0.07319194300590484 | 0.055670362450358495 | 0.013157894736842105  |
| 15:31:23,278 | 23000 | 2    | 4                  | 3102      | 0.07319178111829505 | 0.05550883095037847  | 0.017167381974248927  |
| 15:35:00,384 | 23100 | 2    | 3                  | 3105      | 0.07319174668086008 | 0.055347593582887704 | 0.013824884792626729  |
| 15:39:13,717 | 23200 | 2    | 3                  | 3108      | 0.07319169987422239 | 0.05515234326477739  | 0.011857707509881422  |
| 15:43:23,948 | 23300 | 2    | 4                  | 3112      | 0.07319158427064815 | 0.05497941805204671  | 0.016                 |
| 15:47:35,898 | 23400 | 2    | 0                  | 3112      | 0.07319158427064815 | 0.05473573124615249  | 0.0                   |
| 15:51:47,212 | 23500 | 2    | 4                  | 3116      | 0.07319109074558554 | 0.05456423906001016  | 0.01593625498007968   |
| 15:55:59,702 | 23600 | 2    | 5                  | 3121      | 0.07319093672524254 | 0.05441168779093081  | 0.01984126984126984   |
| 16:00:15,856 | 23700 | 2    | 1                  | 3122      | 0.07319092284506332 | 0.05418727761867569  | 0.00390625            |
| 16:04:29,491 | 23800 | 2    | 1                  | 3123      | 0.07319091254199474 | 0.053966717931880626 | 0.003952569169960474  |
# Tuning Options

| Parameter                    | Value                        |
|------------------------------|------------------------------|
| adjustK                      | false                        |
| delta                        | 1.0E-8                       |
| evalParamSet                 | CURRENT                      |
| geneticParams                | null                         |
| inputFiles                   | [C:\projekte\cygwin_home\mla\jackyChessDockerTesting\tuningdata\lichess-big3-resolved.book]|
| k                            | 1.43                         |
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
| Bishop     | 4966199 | 72  |
| Pawn       | 6819087 | 100 |
| Queen      | 3817373 | 55  |
| Knight     | 4456890 | 65  |
| Rook       | 5988879 | 87  |


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


setting K from Input Tuner Parameter to: 1.43

K: 1.43

## new optimization round

### Optimizing with step 1

Error at start: 0.07319761044395637

| Duration | Round | Step | Params adjustments | Adj total | Curr Error | Overall AdjPerSecond | AdjPerSecond |
|----------|-------|------|--------------------|-----------|------------|----------------------|--------------|
| 0:04:24,454 | 100   | 1    | 21                 | 21        | 0.07319553444407562 | 0.07954545454545454  | 0.07954545454545454 |
| 0:08:21,721 | 200   | 1    | 19                 | 40        | 0.07319430518216873 | 0.07984031936127745  | 0.08016877637130802 |
| 0:13:21,468 | 300   | 1    | 26                 | 66        | 0.07319364532267274 | 0.08239700374531835  | 0.08695652173913043 |
| 0:19:34,105 | 400   | 1    | 12                 | 78        | 0.07319174559606348 | 0.06643952299829642  | 0.03225806451612903 |
| 0:25:08,415 | 500   | 1    | 21                 | 99        | 0.07319009226685803 | 0.0656498673740053   | 0.06287425149700598 |
| 0:30:51,962 | 600   | 1    | 29                 | 128       | 0.07318753360014711 | 0.06915180983252296  | 0.08454810495626822 |
| 0:36:47,472 | 700   | 1    | 22                 | 150       | 0.07318667359149351 | 0.06796556411418214  | 0.061971830985915494 |
| 0:43:00,873 | 800   | 1    | 9                  | 159       | 0.07318601979535788 | 0.06162790697674419  | 0.024128686327077747 |
| 0:48:45,826 | 900   | 1    | 11                 | 170       | 0.07318561890924145 | 0.05811965811965812  | 0.03197674418604651  |
| 0:55:09,005 | 1000  | 1    | 4                  | 174       | 0.07318547268591712 | 0.05259975816203144  | 0.010443864229765013 |
| 1:01:26,355 | 1100  | 1    | 13                 | 187       | 0.07318506400280927 | 0.050732501356483994 | 0.034482758620689655 |
| 1:06:34,292 | 1200  | 1    | 10                 | 197       | 0.07318472548546555 | 0.049323985978968456 | 0.03257328990228013  |
# Tuning Options

| Parameter                    | Value                        |
|------------------------------|------------------------------|
| adjustK                      | false                        |
| delta                        | 1.0E-8                       |
| evalParamSet                 | CURRENT                      |
| geneticParams                | null                         |
| inputFiles                   | [C:\projekte\cygwin_home\mla\jackyChessDockerTesting\tuningdata\lichess-big3-resolved.book]|
| k                            | 1.43                         |
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
| Bishop     | 4966199 | 72  |
| Pawn       | 6819087 | 100 |
| Queen      | 3817373 | 55  |
| Knight     | 4456890 | 65  |
| Rook       | 5988879 | 87  |


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


setting K from Input Tuner Parameter to: 1.43

K: 1.43

## new optimization round

### Optimizing with step 1

Error at start: 0.07318617605455792

| Duration | Round | Step | Params adjustments | Adj total | Curr Error | Overall AdjPerSecond | AdjPerSecond |
|----------|-------|------|--------------------|-----------|------------|----------------------|--------------|
| 0:04:10,860 | 100   | 1    | 19                 | 19        | 0.07318440472556752 | 0.076                | 0.076        |
| 0:08:28,427 | 200   | 1    | 23                 | 42        | 0.07318350425743143 | 0.08267716535433071  | 0.08949416342412451 |
| 0:13:53,760 | 300   | 1    | 14                 | 56        | 0.0731831557462938  | 0.06722689075630252  | 0.043076923076923075 |
| 0:18:03,534 | 400   | 1    | 18                 | 74        | 0.07318133423914977 | 0.06832871652816251  | 0.07228915662650602  |
| 0:21:51,708 | 500   | 1    | 8                  | 82        | 0.0731809331333804  | 0.06254767353165523  | 0.03508771929824561  |
| 0:26:16,161 | 600   | 1    | 12                 | 94        | 0.07317935910221234 | 0.05964467005076142  | 0.045454545454545456 |
| 0:30:02,780 | 700   | 1    | 7                  | 101       | 0.07317887183394835 | 0.0560488346281909   | 0.030973451327433628 |
| 0:34:24,757 | 800   | 1    | 3                  | 104       | 0.07317870043929234 | 0.050387596899224806 | 0.011494252873563218 |
| 0:38:53,539 | 900   | 1    | 7                  | 111       | 0.07317856422491925 | 0.04757822546078011  | 0.026119402985074626 |
| 0:44:06,527 | 1000  | 1    | 4                  | 115       | 0.07317850465004924 | 0.04346182917611489  | 0.012779552715654952 |
| 0:48:49,725 | 1100  | 1    | 7                  | 122       | 0.07317833579598333 | 0.041652441106179584 | 0.024734982332155476 |
| 0:53:16,320 | 1200  | 1    | 2                  | 124       | 0.07317831157733193 | 0.03879849812265332  | 0.007518796992481203 |
| 0:57:26,302 | 1300  | 1    | 5                  | 129       | 0.07317752708468003 | 0.03743470690655833  | 0.020080321285140562 |
| 1:02:02,017 | 1400  | 1    | 8                  | 137       | 0.07317735916813167 | 0.036818059661381346 | 0.02909090909090909  |
| 1:07:20,121 | 1500  | 1    | 14                 | 151       | 0.07317698470836494 | 0.03738549145828175  | 0.0440251572327044   |
| 1:12:26,082 | 1600  | 1    | 9                  | 160       | 0.07317568031525623 | 0.03681546249424758  | 0.029411764705882353 |
| 1:17:17,926 | 1700  | 1    | 39                 | 199       | 0.07316613371775492 | 0.042915678240241534 | 0.13402061855670103  |
| 1:22:52,261 | 1800  | 1    | 33                 | 232       | 0.07316162952463326 | 0.04666130329847144  | 0.09880239520958084  |
| 1:27:23,644 | 1900  | 1    | 18                 | 250       | 0.07316119938976803 | 0.04768262445164982  | 0.06642066420664207  |
