---
layout: post
title: Install and Setup Zsh on Manjaro
tags: Linux Manjaro Zsh shell
categories: common
---

## Install Zsh on Linux

### Manjaro

1. Install Zsh

    ~~~bash
    sudo pacman -Sy zsh
    ~~~

2. Install Plugins

    zsh-autosuggestions : Autocomplete plugin.
    zsh-syntax-highlighting： syntax highlight plugins.

    ~~~bash
    sudo pacman -S zsh-autosuggestions zsh-syntax-highlighting zsh-theme-powerlevel10k zsh-completions
    ~~~

3. Change shell

    ~~~bash
    chsh -s /usr/bin/zsh
    ~~~

    This step is optional, just make sure there is `.zshrc` file in your home directory.

4. Update `.zshrc` filre

    Add following lines into your `.zshrc` file:

    ~~~bash
    source /usr/share/zsh/plugins/zsh-syntax-highlighting/zsh-syntax-highlighting.zsh
    source /usr/share/zsh/plugins/zsh-autosuggestions/zsh-autosuggestions.zsh
    source /usr/share/zsh-theme-powerlevel10k/powerlevel10k.zsh-theme
    ~~~

5. Start Zsh and config `powerlevel10k` theme

    Type `zsh` in terminal, you will see screen like this:

![picture 1](https://r0ngsh3n.github.io/static/img/1215/2021-01-21_10-10.png)

after set of question & selection, you will have terminal like this:

![picture2](https://r0ngsh3n.github.io/static/img/1215/2021-01-21_10-20.png)

## Zsh on Ubuntu

1. Install Oh My Zsh

~~~bash
sudo apt install zsh
~~~

2. You can change the theme in the `.zshrc`

~~~bash
ZSH_THEME="agnoster"
~~~

3. Install `powerlevel10k`

~~~bash
git clone --depth=1 https://github.com/romkatv/powerlevel10k.git ${ZSH_CUSTOM:-$HOME/.oh-my-zsh/custom}/themes/powerlevel10k
p10k configure <- this command start the config
~~~

4. Install `zsh-autosuggestions`

~~~bash
git clone https://github.com/zsh-users/zsh-autosuggestions.git $ZSH_CUSTOM/plugins/zsh-autosuggestions
~~~


5. Install `zsh-syntax-higlighting`

~~~bash
git clone https://github.com/zsh-users/zsh-syntax-highlighting.git $ZSH_CUSTOM/plugins/zsh-syntax-highlighting
~~~

6. Config the  `zsh-syntax-higlighting` and `zsh-autosuggestions`

~~~bash
#plugins=(git z)
plugins=(git zsh-autosuggestions zsh-syntax-highlighting)
~~~