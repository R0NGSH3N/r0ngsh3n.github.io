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

## Install and Config Nerd Font

Nerd font is required by couple of plug in (vim-devicon)

1. Download font from [here](https://www.nerdfonts.com/font-downloads)

2. Unzip and copy to ~/.fonts

3. Run the command fc-cache -fv to manually rebuild the font cache

4. run command fc-list | grep <fontName> to verify nerd font install correctly

### Set Guifont for Nerd Font

if you are using neovim, then you need change the font of the terminal and then restart vim, if you use vim then you need to use

~~~bash
:set guifont=JetBrains\ Mono\ Nerd\ Font:h10
~~~

to implement the font

### verify the font install correctly

in vim :

~~~bash
:echo g:WebDevIconsUnicodeDecorateFileNodesDefaultSymbol
~~~

if you see the icon then you are fine, it you see some weird icon, then it is not.

## Set up vimrc or init.vim file

The config file is in `~/.vimrc` or `~/.config/nvim/init.vim`, basic config

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

### install plugin management tool : vundle

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
Plugin 'JamshedVesuna/vim-markdown-preview;
Plugin 'ycm-core/YouCompleteMe'
~~~

### Map the \<leader\> key to comma

~~~bash
let mapleader = ","
~~~

### Config NERDTree

~~~bash
noremap  <leader>t : NERDTreeToggle<CR>
~~~

"O" - expand all; "o" expand current node

### activate `Vimdevicon`

  Once install the vim-devicon, you need use following command to activate in .vimrc

~~~vimscript
let g:webdevicons_enable = 1
let g:webdevicons_enable_nerdtree = 1                                               
let g:webdevicons_enable_airline_statusline = 1    
~~~

### setup `vim-markdown-preview`

install grip:

~~~bash
pip3 install grip
~~~

in .vimrc:

~~~vimscript
let vim_markdown_preview_github=1
~~~

then \<c-p\> to launch preview

### Set up coc.nvim

1. Install nodejs

    ~~~bash
    curl -sL install-node.now.sh/lts | sudo bash
    ~~~

2. add following in vimrc/init.vim if you use vundle

    ~~~bash
    Plugin 'neoclide/coc.nvim'
    ~~~

    then resource and run `:PluginInstall`

3. Check health

    run :checkhealth, if you see lot of `ok` then you should be fine.
    run :CocInfo to see the information

4. Install java

    run :CocInstall coc-java

5. Setup JAVA_HOME 

    This is same step as set up for VS Code, the tricky thing is some time the
    openjdk doesn't work. It complains that JAVA_HOME is not pointing to JDK

    I downloaded oracle jdk and install fixed this issue. [Here](https://linuxhint.com/install_java_linux_mint/) is the
    instruction how to install on linux mint.

6. update the init.vim with coc.nvim config.

    you can find the coc.nvim configuration [here](https://github.com/neoclide/coc.nvim#example-vim-configuration)

    I didn't completely copy-paste since there are some keymaps are conflicting.

7. Some useful shortcuts:

    'gd' - goto declare. \<c-o\> come back
    'gy' - goto class definition
    'gi' - goto implement (from interface to concrete object)
    'gr' - goto reference
    '[g' - goto next error. ']g' - goto prev error
    'K'  - show document of a class

### Setup ctags

Install 'ctags'

~~~bash
sudo apt  install exuberant-ctags
~~~

In your java project directory, run `ctags`

~~~bash
ctags -R --languages=Java
~~~

update `.vimrc` set:

~~~bash
set tags=tags;/
~~~

### Setup `YouCompleteMe` plugin

1. update `~/.config/nvim/init.nvim`: add Plugin 'ycm-core/YouCompleteMe' 

2. install cmake

~~~bash
sudo apt install cmake
~~~

3. call `install.py`

~~~bash
cd /home/rongshen/.vim/bundle/YouCompleteMe
python3 ./install.py --java-completer
~~~

4. restart nvim

### setup surround

install:

~~~bash
git clone https://tpope.io/vim/surround.git  ~/.vim/bundle/surround
~~~

shortcuts:

- add surround on the line: `yss` + character
- add surround on the word: `ysiw` + character: ysiw + "
- change surround: `cs` + original character + replace character
- remove surround: `ds` + character
- multiple line surround: shift + v + shift + s + character"  V + select lines + S + "


### easy motion

leader + f : jump to character
leader + w : jump to word
leader + s : jump to 2 characters - you type 2 in command line
leader + L : move to line

### move to pathogen

1. Install Pathogen:

~~~bash
mkdir -p ~/.vim/autoload ~/.vim/bundle && \
curl -LSso ~/.vim/autoload/pathogen.vim https://tpo.pe/pathogen.vim
~~~

2. enable Pathogen

in `~/.vimrc` add "execute pathogen#infect()"

3. Install plugins

clone whatever plugins you want into `~/.vim/bundle` folder

~~~bash
git clone https://github.com/preservim/nerdtree.git ~/.vim/bundle/nerdtree
~~~

Here is the plugins that I installed

~~~bash
total 36
drwxrwxr-x  9 rongshen rongshen 4096 Mar 15 23:07 ./
drwxrwxr-x  5 rongshen rongshen 4096 Mar 15 22:16 ../
drwxrwxr-x 10 rongshen rongshen 4096 Mar 15 22:21 nerdtree/
drwxrwxr-x  6 rongshen rongshen 4096 Mar 15 22:33 surround/
drwxrwxr-x  8 rongshen rongshen 4096 Mar 15 22:52 vim-airline/
drwxrwxr-x  8 rongshen rongshen 4096 Mar 15 22:53 vim-airline-themes/
drwxrwxr-x  5 rongshen rongshen 4096 Mar 15 23:05 vim-buffet/
drwxrwxr-x  5 rongshen rongshen 4096 Mar 15 22:56 vim-colorschemes/
drwxrwxr-x  8 rongshen rongshen 4096 Mar 15 23:07 vim-easymotion/
~~~
