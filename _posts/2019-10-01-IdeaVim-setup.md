---
layout: post
title: IdeaVim IdeaVimrc Setup
tags: Vim Intellij
categories: common
---

Daily Idea Vim Shortcuts Cheat Sheet:

| shortcuts  | descriptions   |
|---|---|
| Space + L  | show recent location  |
| Space + g  | go to Declaration|
| Space + f  | show up search window with  `files` tab active |
| Space + c  | show up search window with `class` tab active |
| Space + s  | show up search window with `symbol` tab active |
| ***Space + ;***  | ***list all methods and variables in current file*** |
| Space + i  | goto implementation -- this is used on interface or abstract class |
| Space + I  | pop `selectIn` window -- this window help to jump between different idea windows |
| Space + e  | pop recent files -- different as switcher (Ctrl + tab)|
| Space + t  | pop surround with window, I changed to vmap |
| Space + T  | pop refactory this window, in visual mode |
| Space + rr  | rename |
| Space + rg  | pop generate window|

## Set command you can use in IdeaVim: [link](https://github.com/JetBrains/ideavim/wiki/%22set%22-commands)

## Emulated Vim Plugins
    
    vim-easymotion
    vim-surround
    vim-multiple-cursors
    vim-commentary
    argtextobj.vim
    vim-textobj-entire
    ReplaceWithRegister
    vim-exchange
    vim-highlightedyank

~~~bash
## Split
<leader>q - close content
<leader>ps - split horizontal
<leader>pv - split vertical
<leader>wc - unsplit
<leader>wn - next split window - same as <C-w>w
<leader>x - goto next error
<leader>X - goto previous error

## Navigation
<leader>h - move back
<leader>l - move forward
<leader>g - go to declaration
<leader>i - go to implementation

[b - previous tab
]b - next tab



<leader>u - find usage

gd, <c-]> - go to definition
<C-o - go back
`. - go to previous update position

## pop up window
<leader>f - open file
<leader>c - open class
<leader>s - open symbol
<leader>; - open current file structure
<leader>d - show error message
<leader>I - show selectIn popup
<leader>e - popup recent Files
<leader>b - show bookmark 


## Refactoring
visual mode: T - popup refactoring menu
<leader>rg: pop up generate window
<leader>rc: pop up inspector window

## run & debug
,b - debug
,r - run 
,c - run class
,d - debug class
,t - run tests
,T - rerun failed Tests
~~~