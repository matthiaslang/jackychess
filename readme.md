# Jacky Chess

A simple UCI chess engine written in Java.

This project is mainly a self educational project to 
learn about various algorithms around chess programming. 
Inspiration was mainly given by the great chess programming wiki
https://www.chessprogramming.org/ where you can get a good overview about all used
algorithms, but also by many great open source engines by studying the sources.

## Goals of the engine

- educational purpose: main focus is to learn about chess programming
- the code is simple and clear since the main purpose is to understand the algorithms.
- the code should be exchangeable: the different implementations for search algorithms and
  related implementations are implemented by interfaces and exchangeable. This is needed to compare the algorithms, too.
- the evaluation function is configurable to switch parameters for experimentation
- the engine should be at least so good that it can defeat me (a mediocre amateur chess player, I hope.. :)
- having fun to program it :)

## Requirements

Java 8 & maven to compile the engine.

The chess engine should work under any UCI chess client.
It was mainly tested with Arena http://www.playwitharena.de/ and with cutechess https://github.com/cutechess/cutechess
and works fine with these clients. However - any UCI complient UI should work.

Simply add the engine to your prefered UCI Gui Client and then you should be ready to use it.
You should have several UCI options able to set in the UI then.

## Rating

The engine Version 0.9.14 has a ELO rating of 1455 in the CCLR Blitz index, see http://ccrl.chessdom.com/ccrl/404/



## License

    Jacky Chess 
    Copyright (C) 2021  Matthias Lang

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
- an configurable Evaluation function considering material, mobility and several other simple evaluations
- Search Algorithm is PVS with an iterative deepening algorithm using Alpha Beta Negamax.
- Aspiration windowing
- null move pruning and static null move pruning
- razoring
- late move reduction
- Move sorting by PV Moves, killer moves, history heuristic, captures sorted by BLIND
- Transposition Table using zobrist hashing to cache Scores
- basic quiescence search with delta cut off



Still a lot is missing to have a really good, competing engine and the engine
will probable never reach game strength of its competitors.
Nevertheless the goal is to make it stronger with each version.

## UCI Parameter

The engine has several UCI parameter. Find more information under [UCI Parameter](docs/uciparameter.md)


# Versions

see [Version History](docs/versionhistory.md)                     


## todo 
           
- optimizations on all ends...
- make evaluation better by considering more aspects
- optimization of evaluation parameter
- pruning optimization
- tt cache optimization
- make end-game stronger
- lazy staged move generation
- 

