---
layout: post
title: Implement CSS Grid
tags: HTML CSS 
categories: common
---

1. Basic Concept

   - Container : direct of all the grid items
    ~~~Html
    <div class="container">
        <div class="item item-1"> </div>
        <div class="item item-2"> </div>
        <div class="item item-3"> </div>
    </div>
    ~~~

    CSS Properties:

      - display:
      ~~~CSS
        .container {
            display: grid | inline-grid;
        }
      ~~~

   - item : direct Children of `Container`

    ~~~Html
    <div class="container">
        <div class="item"> </div>
        <div class="item">
          <p class="sub-item"> </p>
        </div>
        <div class="item"> </div>
    </div>
    ~~~

   - Line : The dividing lines that make up the structure of the grid. They can be either vertical (“column grid lines”) or horizontal (“row grid lines”)

   - Track: Column or Row
   - Area:  Multiple columns and Rows (Block)
