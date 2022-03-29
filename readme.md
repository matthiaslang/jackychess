# Jacky Chess

A simple UCI chess engine written in Java named after my dog "Jacky"!
                                                  
The engine is written from scratch as an educational project to learn chess programming.
It started as a simple proof of concept experiment within a weekend, but is now derived to a rather stable playing UCI engine.

I got a lot of inspiration from other resources in the web, mainly the great https://www.chessprogramming.org/ wiki
and several open source engines, just to name a few of them: cpw, stockfish, fruit, chess22k.


## Goals of the engine

- educational purpose: main focus is to learn about chess programming
- the code is (hopefully) simple and clear since the main purpose is to understand the algorithms.
- the code should be flexible: it uses interfaces and configurations to exchange different implementations for testing.
- the evaluation function is configurable to switch parameters for experimentation
- having fun to program it :)

## Requirements
                                   
## Building

At least Java 8 & maven to compile the engine.
However, it should run under any newer Java version. It is mainly tested with Java 11 under Linux.         
           
## Usage


 - You need a Java JRE Environment greater than Java 8 to run the engine.
 - You need an UCI Gui Client to register the engine for usage. The chess engine should work with any UCI chess client.
It was mainly tested with [Arena](http://www.playwitharena.de/) and with [cutechess](https://github.com/cutechess/cutechess)
and works fine with these clients. However - any UCI complient UI should work.

Simply add the engine to your prefered UCI Gui Client and then you should be ready to use it. Most clients should be fine
to select the jar file directly.
You should have a few UCI options able to set in the UI then.

## Rating

 - The engine Version 0.10.0 has an rating of 2078 ELO in the CCRL Blitz index, see http://ccrl.chessdom.com/ccrl/404/


## License

    Jacky Chess 
    Copyright (C) 2022  Matthias Lang

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses/.

## Implementation

The chess engine uses following technics/algorithms 

- Bitboards
- Move Generator using a magic bitboards  
- a configurable Evaluation function considering material, mobility and several other simple evaluations
- Negamax with PVS in an iterative deepening algorithm using Alpha Beta Pruning.
- Aspiration windowing
- null move pruning and static null move pruning
- razoring
- late move reduction
- separate move generation for quiescence
- Move sorting by PV Moves, Hash Moves, killer moves, history heuristic, counter moves, captures sorted by SEE
- Transposition Table using zobrist hashing to cache Scores
- basic quiescence search with delta cut off
- separate eval cache



Still a lot is missing to have a really good, competing engine and the engine
will probable never reach game strength of its competitors.
Nevertheless the goal is to make it stronger with each version.

## UCI Parameter

The engine has some UCI parameter and several options mainly to set for develop purpose. Find more information under [UCI Parameter](docs/uciparameter.md)


# Versions

see [Version History](docs/versionhistory.md)                     


## todo 
           
- optimizations on all ends...
- make evaluation better by considering more aspects
- tuning of evaluation parameter
- pruning optimization
- tt cache optimization
- make end-game stronger with special end game evaluations
- lazy staged move generation
- Multithreading with Lazy SMP

