---
layout: post
title: IdeaVim IdeaVimrc Setup
tags: Vim Intellij
categories: common
---

Daily Idea Vim Shortcuts Cheat Sheet:

| shortcuts  | descriptions   |
|---|---|
| \<L\> + L  | show recent location  |
| \<L\> + g  | go to Declaration|
| \<L\> + f  | show up search window with  `files` tab active |
| \<L\> + c  | show up search window with `class` tab active |
| \<L\> + s  | show up search window with `symbol` tab active |
| ***\<L\> + ;***  | ***list all methods and variables in current file*** |
| \<L\> + i  | goto implementation -- this is used on interface or abstract class |
| \<L\> + I  | pop `selectIn` window -- this window help to jump between different idea windows |
| \<L\> + e  | pop recent files -- different as switcher (Ctrl + tab)|
| \<L\> + t  | pop surround with window, I changed to vmap |
| \<L\> + T  | pop refactory this window, in visual mode |
| \<L\> + rr  | rename |
| \<L\> + rg  | pop generate window|
| \<L\> + q | close content|
| \<L\> + ps|  split horizontal |
| \<L\> + pv|  split vertical |
| \<L\> + wc|  unsplit |
| \<L\> + wn|  next split window - same as <C-w>w |
| \<L\> + x | goto next error | 
| \<L\> + X | goto previous error |
| \<L\> + h | move back |
| \<L\> + l | move forward |
| \<L\> + g | go to declaration |
| \<L\> + i | go to implementation |
|[b | previous tab |
|]b | next tab |
|\<L\> + u | find usage |
|\<L\> + f | open file |
|\<L\> + c | open class |
|\<L\> + s | open symbol |
|\<L\> + ; | open current file structure |
|\<L\> + d | show error message |
|\<L\> + I | show selectIn popup |
|\<L\> + e | popup recent Files |
|\<L\> + b | show bookmark |
|visual mode: T | popup refactoring menu |
|<leader>rg | pop up generate window |
|<leader>rc | pop up inspector window|
|,b | debug |
|,r | run |
|,c | run class |
|,d | debug class |
|,t | run tests |
|,T | rerun failed Tests |

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
