# Jacky Chess

A simple UCI chess engine written in Java.

This project is mainly a self educational project to 
learn the various algorithms around chess computers. 
Inspiration was mainly given by the great chess programming wiki
https://www.chessprogramming.org/ where you can get a good overview about all used
algorithms.

## Goals of the engine

- educational purpose: main focus is for me to learn about chess programming
- the code is very simple and clear since the main purpose is to understand the algorithms.
- the code should be exchangeable: the different implementations for search algorithms and
  related implementations are implemented by interfaces and exchangeable. This is needed to compare the algorithms, too.
- the engine should be at least so good that it can defeat me (a mediocre amateur chess player, I hope.. :)
- having fun to program it :)

## Requirements

The chess engine should work under any UCI chess client.
It was mainly tested with Arena http://www.playwitharena.de/ 
and works find with this client. It was also tested with pyChess.

## Implementation

The chess engine uses 

- a simple Board using 64 bytes for coding the figures.
- Move Generator using a "mailbox" 10x12 Board  
- a simple Evaluation function which mainly checks material
and basic positional things like mobility and captures
- Search Algorithm is Mtdf with an iterative deepening algorithm with Alpha Beta Negamax.
- Caching of generated moves



Still a lot is missing to have a really good, competing engine and the engine
will probable never reach game strength of its competitors.
Currently missing is

- no opening books. so opening game may be "boring" since the engine will produce
always the same moves for a given board. I am not planning to add opening books since
  I don´t feel it interesting to implemented this. I am more interested to add here
  some "human" behaviour with maybe some random chossing.
  
- special end game handling: the engine currently does not play well in endgame situations. This
is something I will try to investigate and to tackle with special evaluation functions...

- not all chess rules are implemented: the 50 move rule is not implemented.

- no quescence handling: so the engine can choose "bad moves" due to horizon effects. This is something I will try to implement but I think this will take care and time. Maybe I start with a first simple approach.

              
## Experimental Implementations
   
### Zobrist
Zobrist Hashing is implenmented by the Board3.
Its not clear if it is beneficial, looks like it could getting beneficial on depths >=8 compared to no caching.

It is at least much better than the simple java hash based cache implementation (Board2)


### table based move generation

in branch experimental/tableBasedMoveGenerator

This implementation uses a pre-filled move table with all possible moves, instead of a 12x8 field. But it does not look like an improvement.
First simple tests show that it is slightly slower than the current move generator implementation.

### mtdf

The mtdf implementation currently is slower than the normal alpha/beta pruning, probably because it does not use
zobrist hashing. It should be retested with the NegaMaxAlphaBetaTT implementation if it then works better.



                                                                 
### todo/bugs

- draw (stellungswiederholung): the engine seems to run into this situation due to the current eval wheight. need unit tests
and analysis how to better handle this
  
