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

## Install Nerd Font

Nerd font is required by couple of plug in (vim-devicon)

1. Download font from [here](https://www.nerdfonts.com/font-downloads)

2. Unzip and copy to ~/.fonts

3. Run the command fc-cache -fv to manually rebuild the font cache

4. run command fc-list | grep <fontName> to verify nerd font install correctly

## Set Guifont

if you are using neovim, then you need change the font of the terminal and then restart vim, if you use vim then you need to use 

~~~bash
:set guifont=JetBrains\ Mono\ Nerd\ Font:h10
~~~

to implement the font

## verify the font install correctly

in vim :

~~~bash
:echo g:WebDevIconsUnicodeDecorateFileNodesDefaultSymbol
~~~

if you see the icon then you are fine, it you see some weird icon, then it is not.


### The config file is in `~/.vimrc` or `~/.config/nvim/init.vim`, basic config

my vim config is [here](https://raw.githubusercontent.com/R0NGSH3N/r0ngsh3n-vim/main/nvim/init.vim)

### set vim color pattern

mkdir `~/.vim/colors`, copy the `*.vim` file into this folder, then update ~/.vimrc file:

then copy the color vim file into the folder:

~~~bash
#neovim
wget https://raw.githubusercontent.com/AlessandroYorba/Alduin/master/colors/alduin.vim -O ~/.config/nvim/colors/alduin.vim

#vim:
wget https://raw.githubusercontent.com/AlessandroYorba/Alduin/master/colors/alduin.vim -O ~/.vim/colors/alduin.vim
~~~

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
Plugin 'bagrat/vim-buffet'                                                          
Plugin 'vim-airline/vim-airline'                                                    
Plugin 'vim-airline/vim-airline-themes'                                             
Plugin 'easymotion/vim-easymotion'                                                  
Plugin 'ryanoasis/vim-devicons'      
~~~

### change the <leader> key to comma

~~~bash
let mapleader = ","
~~~

### NERDTree

~~~bash
noremap  <leader>t : NERDTreeToggle<CR>
~~~

"O" - expand all; "o" expand current node

## activate `Vimdevicon`

  Once install the vim-devicon, you need use following command to activate in .vimrc

~~~vimscript
let g:webdevicons_enable = 1
let g:webdevicons_enable_nerdtree = 1                                               
let g:webdevicons_enable_airline_statusline = 1    
~~~

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
