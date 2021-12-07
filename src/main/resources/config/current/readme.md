Another simple configuration.

It configures many aspects and ideas of the CPW engine evaluation.




Testing notes:


73% better than default with current changes.


## experimental mit pst + mobility + leicht geänderte werte aus CPW:     

Motor                     Punkte                              Ja                             Ja                             Ja                             Ja    S-B
1: Jacky17 Eval MinPst       69,0/90 ······························ 0=1==11==1==11==111===0=0===== 1=1===11===111=11=1=1111111111 1111=1=11=11=111=111111=011111  2428,5
2: Jacky17 Eval Experimental 52,5/90 1=0==00==0==00==000===1=1===== ······························ ====01=1===101100==1=1=1==0=1= ====1111=1=1=1=1==1=11=1=11111  1959,0
3: Jacky17 Eval Current      37,5/90 0=0===00===000=00=0=0000000000 ====10=0===010011==0=0=0==1=0= ······························ 111=01====1===11======11=101==  1437,0
4: Jacky17 Eval Default      21,0/90 0000=0=00=00=000=000000=100000 ====0000=0=0=0=0==0=00=0=00000 000=10====0===00======00=010== ······························  1011,0
                                                                 

## experimental mit pst + mobility + geänderte mat werte + tropism + king attacks

revision 94f25c6e

Motor                     Punkte                              Ja                             Ja                             Ja                             Ja    S-B
1: Jacky17 Eval MinPst       63,0/90 ······························ ==0=010===1111==111===0==110=0 0110==1110111==0111====01110== 1111=111111=11111=11111111==1=  2251,0
2: Jacky17 Eval Experimental 56,0/90 ==1=101===0000==000===1==001=1 ······························ 1000==11=1==1==1=0=0==11=1==11 11111111110111=1111=111==11010  2039,5
3: Jacky17 Eval Current      43,5/90 1001==0001000==1000====10001== 0111==00=0==0==0=1=1==00=0==00 ······························ =1====1==1=1=111=====01=111=1=  1723,7
4: Jacky17 Eval Default      17,5/90 0000=000000=00000=00000000==0= 00000000001000=0000=000==00101 =0====0==0=0=000=====10=000=0= ······························  882,25


mit cutechess:

Finished game 360 (jc0917_ELMINPST vs jc0917_ELDEFAULT): 1/2-1/2 {Draw by 3-fold repetition}
Rank Name                          Elo     +/-   Games   Score    Draw
1 jc0917_ELMINPST               147      40     180   70.0%   41.1%
2 jc0917_ELEXP                   56      39     180   58.1%   41.7%
3 jc0917_ELCURR                  -8      37     180   48.9%   46.7%
4 jc0917_ELDEFAULT             -209      40     180   23.1%   39.4%

## pawn evaluations min.material

current == experimental from   revision 94f25c6e


seeems like experimental is now a bit worser thant current.


Rank Name                          Elo     +/-   Games   Score    Draw
1 jc0917_ELMINPST               123      39     180   66.9%   42.8%
2 jc0917_ELCURR                  58      37     180   58.3%   46.7%
3 jc0917_ELEXP                   37      36     180   55.3%   50.6%
4 jc0917_ELDEFAULT             -247      43     180   19.4%   34.4%




