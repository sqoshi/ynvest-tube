# ynvest-tube

<div align="center" style="display: flex;flex-direction: row">
  <p>ynvest-tube</p>
  <img src="ynvest_tube/app/src/main/ynvest_tube_logo-playstore.png" width="100"/>
</div>

## Table of contests

- [Introduction](#introduction)
- [General](#general)
    - [Server](#server)
- [Installation](#installation)
- [Launch](#launch)
- [Technologies](#technologies)

## Introduction

`YnvestTube` is a mobile economy-based multiplayer game. User can incarnate into investor and take part in auctions.
Auction trade _objects_ are real `youtube` videos, more precisely auction object is a `youtube` video rental for few
days. Game server observes them on [YouTube](https://www.youtube.com) and stores live data about views, likes and
dislikes in server database. Videos data is updated once per hour, so we can clearly say it is actual. Each user may win
auction by beating other players' bids. At the end of renting user cash is increased by value depending on the `growth`
of views during the rental. Well, users may use a lot of more or less intelligent strategies like raising the price of
an auction item they don't even want to rent, just to make it difficult for others to play ;).

## General

### Server

Application communicates with server written in `Django`, `Python`. List of endpoints, periodic tasks and more details are
available in repository created specially for this
purpose [YnvestTubeServer](https://github.com/sqoshi/ynvest-tube-server).
We used `Redis` as a `periodic task` caller.


## Installation

## Launch

## Technologies

- kotlin
- retrofit2



