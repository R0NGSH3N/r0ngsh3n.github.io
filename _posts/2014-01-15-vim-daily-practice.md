---
layout: post
title: Vim Daily Practice
tags: Linux Vim
categories: common
---

## Install/config gnome vim

if `vim --version` give you `-clipboard`, then you need to install gnome vim:

~~~bash
sudo pacman -S gvim # arch
sudo apt install vim-gnome #ubuntu
~~~

### The config file is in `~/.vimrc`, basic config

~~~bash
set nocompatible "not compatible with vi
syntax enable·
syntax on »· "set highlight
set showmode»· "show it is n/i/v mode at the bottom
set showcmd» "show incomplete command
"set mouse=a » "enable mouse
set encoding=utf-8
set t_Co=256 » "set color 256

" indent
filetype indent on "based on different type of file, use different indent policy
set autoindent   "autoindent
set tabstop=2» "tab how many space vim show
set shiftwidth=4 ">> << == 4 charactors
set softtabstop=2 "tab=2space

"set line number
"set number
set relativenumber "show the current line number and relative line number for other
set cursorline    "draw line in current line

"text
set textwidth=80
set nowrap
set laststatus=2 "show status line
"set linebreak "only break when not in word
"set wrpmargin=2
set scrolloff=5
set ruler

"search
set showmatch "show match on parens
set hlsearch  "set highlight on searh
set incsearch "set incremental search
set ignorecase smartcase

"spelling
set spell spelllang=en_us

set nobackup noswapfile undofile "no backup
set noerrorbells
set visualbell
set autochdir "change current work directory to the file current in edit

set history=1000 "1000 history
set autoread

set list lcs=trail:·,tab:»· "listchars

set wildmenu
set wildmode=longest:list,full

nnoremap :W :w
nnoremap :Q :q
"set line number relative or not
nnoremap <c-l> : set relativenumber! set number! \| set number? set relativenumber? <CR>
nnoremap <c-c> : set cursorline! \| set cursorline? <CR>

~~~

### set vim color pattern

mkdir `~/.vim/colors`, copy the `*.vim` file into this folder, then update ~/.vimrc file:

~~~bash
" color scheme
let g:alduin_Shout_Dragon_aspect = 1
colorscheme alduin
~~~

** I used color scheme called : alduin

### install plugin managment tool : vundle

~~~bash
git clone https://github.com/VundleVim/Vundle.vim.git ~/.vim/bundle/Vundle.vim
~~~

Add bunch of plugins in `~/.vimrc`, then run `:PluginInstall` to install them

~~~bash
Plugin 'VundleVim/Vundle.vim'
Plugin 'tpope/vim-fugitive' 
Plugin 'airblade/vim-gitgutter' 
Plugin 'jiangmiao/auto-pairs'
Plugin 'tpope/vim-surround'
Plugin 'scrooloose/nerdtree'
Plugin 'scrooloose/nerdcommenter'
~~~

### change the <leader> key to comma

~~~bash
let mapleader = ","
~~~

### Config NERDTree


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
