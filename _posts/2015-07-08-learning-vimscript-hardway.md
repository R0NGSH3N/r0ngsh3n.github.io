---
layout: post
title: Vim Learning vimscript hardway
tags: Linux Vim
categories: common
---

- `echom` and `echoe` - print message and error message, later can use `:message` to show all previous printed message.

- `se[t] number` | `se[t] nonumber` | `se[t] number!` | `se[t] number?` -- check which option is on

- `se[t] numberwidth=2` (default is 4)

- `map <c-d> ddp` -- this map ctrl-d to `dd` and then `p`
  `map _ ddkkp` -- this map _ to delete line and then go up 2 line and then paste

   nmap: (nnoremap) map in normal mode
   vmap: (vnoremap) in visual mode
   imap: (inoremap) map in insert mode: this could be use for intellij live template
          imap soul System.out.println("");
          imap <c-d> <esc>ddi : need <esc> to escape the insert mode then delete line

    VERY USEFUL: imap <c-u> <esc>viwU : convert a just finished type word to all upper case.

   nunmap U - unmap U in normal mode

   use \*noremap!!!!!

-  repeat insertion:
    0. i - insert mode
    1. `Ctrl+o` issue normal command without leaving insert mode
    2. 80 - number of repetition
    3. i - insert
    4. type the charactors you want to repetition insert
    5. `Esc` - leave the insert mode
