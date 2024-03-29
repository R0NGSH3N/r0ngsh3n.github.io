---
layout: post
title: Build Neovim as IDE (>0.5)
tags: Linux vim neovim lua java LSP IDE Intellij
categories: common
---

- [Neovim installatoin](#neovim-installatoin)
- [Create `init.vim`](#create-initvim)
- [What I expect neovim as IDE](#what-i-expect-neovim-as-ide)
- [Install language Server](#install-language-server)
- [Install Java Language Server](#install-java-language-server)
- [Install Plugin Manager](#install-plugin-manager)
- [Plugins](#plugins)
- [Plugin Setup](#plugin-setup)
- [Plugin shortcuts](#plugin-shortcuts)

I have been using vim for more than 20 years now, and I decide to take a look of neovim and build neovim as my major (hope only also) IDE.

## Neovim Installation 

the new neovim support neovim-lsp, but that version need to install from git, I have arch linux, so it makes it easier:

1. git clone the neovim into local `/tmp/` - since neovim-git is nightly build, the normal neovim is different, install this version.

~~~bash
cd /tmp
git clone https://aur.archlinux.org/neovim-git.git
~~~

2. `makepkg`

~~~bash
cd /tmp/neovim-git
makepkg -si
~~~

3. after installation, verify the version is `0.5.0`

~~~bash
nvim --version
~~~

you also need create `init.nvim` file in `.config/nvim/` folder

## What I expect neovim as IDE

1. go to definition/Declaration
2. go back last cursor
3. find all the usage of current method/variable
4. autocomplete
5. highlight compile error with error message
6. import organization
7. extract method
8. code template
9. debugging function
10. autoformatting
11. surround with try...catch

| functions | shortcuts|
|-----------|----------|
|go back/next cursor position | CTRL-o/CTRL-i|
|goto definition|gd|
|goto declariation|gD|
|format code | \<leader\>i|
|optimize import | \<leader\>o|
|action menu|\<leader\>af|
|peek method|\<leader\>ah|

## Install language Server

List of language Server can be find in [here](https://microsoft.github.io/language-server-protocol/implementors/servers/)

## Install Java Language Server

Installing java language server is different as other language, I installed the plugin called `nvim-jdtls`, you can find links [here](https://github.com/mfussenegger/nvim-jdtls), following are the steps to install this plug with Eclipse java language server. 

1. Install the plug, add in `init.nvim`

~~~vim
Plug 'mfussenegger/nvim-jdtls'
~~~

**The following `nvim-jdtls` plug doesn't require `nvim-lspconfig` plugin**

2. There are couple of ways to download eclipse jdtls server, but I found easiest way is to download the jar file and extract in the folder. Download latest LSP serve from [eclipse jdtls snapshot link](https://download.eclipse.org/jdtls/snapshots/?d)

I downloaded the latest version: jdt-language-server-latest.tar.gz, then I created `jdk-language-server` from `~/.config/nvim/` folder, copy the gz file over and

~~~zsh
tar -zxvf jdt-language-server-latest.tar.gz
~~~

3. create launch jdtls script, in my `jdk-language-server` folder, I created file called `start_jdtls` and copy the following content from [here](https://github.com/mfussenegger/nvim-jdtls), and modify to the following

~~~bash
#!/usr/bin/env bash

# NOTE:
# This doesn't work as is on Windows. You'll need to create an equivalent `.bat` file instead
#
# NOTE:
# If you're not using Linux you'll need to adjust the `-configuration` option
# to point to the `config_mac' or `config_win` folders depending on your system.

# the JAR path need to point to exclips jdtls plugins folder!!!
JAR="/home/rongshen/.config/nvim/jdt-language-server/plugins/org.eclipse.equinox.launcher_*.jar"
GRADLE_HOME=$HOME/gradle /usr/lib/jvm/java-15-openjdk/bin/java \
  -Declipse.application=org.eclipse.jdt.ls.core.id1 \
  -Dosgi.bundles.defaultStartLevel=4 \
  -Declipse.product=org.eclipse.jdt.ls.core.product \
  -Dlog.protocol=true \
  -Dlog.level=ALL \
  -Xms1g \
  -Xmx2G \
  -javaagent:/home/rongshen/.config/nvim/jdt-language-server/lombok.jar \
  -jar $(echo "$JAR") \
  -configuration "/home/rongshen/.config/nvim/jdt-language-server/config_linux" \
  -data "${1:-$HOME/workspace}" \
  --add-modules=ALL-SYSTEM \
  --add-opens java.base/java.util=ALL-UNNAMED \
  --add-opens java.base/java.lang=ALL-UNNAMED
~~~

- The "JAR" option need to point to `Plugins` folder that you extract the lsp server.
- The `/usr/lib/jvm/java-15-openjdk/bin/java` need to point to your jdk folder.
- The `configuration` need to point to `config_linux` folder that you extract the lsp server.
- You need to point to `lombok.jar` to enable lombok feature

After all, run `chmod +x start_jdtls` to assign execution permission. To verify everything is fine, just run the `start_jdtls` command, you should see the following output from screen:

~~~bash
Content-Length: 127

{"jsonrpc":"2.0","method":"window/logMessage","params":{"type":3,"message":"Jun 29, 2021, 12:39:19 PM Main thread is waiting"}}
~~~

4. Setup `init.nvim`

Add autogroup commands in the `init.nvim` file, on the `nvim-jdtls` website, they said to put following auto command in `init.nvim`:

~~~vimrc
if has('nvim-0.5')
  augroup lsp
    au!
    au FileType java lua require('jdtls').start_or_attach({cmd = {'java-lsp.sh'}})
  augroup end
endif
~~~

where the `java-lsp.sh` is our `start_jdtls` script, to make this work, you also need add `start_jdtls` script in the `$PATH`

for me, I updated like following

~~~vimrc
if has('nvim-0.5')
  augroup lsp
    au!
    au FileType java lua require('jdtls').start_or_attach({cmd = {'/home/rongshen/.config/nvim/jdt-language-server/start_jdtls'}})
  augroup end
endif
~~~

Sometime, you will see error like "client 1 exit 13 with signal 0" error, I usually try to run `start_jdtls` script, if failed, then I delete `~/workspace/` folder and then rebuild from there.

## Install Plugin Manager

I will recommend to use [vim-plug](https://github.com/junegunn/vim-plug).

1. Download `vim-plug` manager:

~~~bash
cd ~/.config/nvim/
curl -fLo ~/.vim/autoload/plug.vim --create-dirs \
    https://raw.githubusercontent.com/junegunn/vim-plug/master/plug.vim
~~~

This will download the `plugin.vim` file in your `~/.config/nvim/autoload/`

2. in `init.vim` add following:

~~~bash
" Vim-Plug init
if ! filereadable(expand('~/.config/nvim/autoload/plug.vim'))
	echo "Downloading junegunn/vim-plug to manage plugins..."
	silent !mkdir -p ~/.config/nvim/autoload/
	silent !curl "https://raw.githubusercontent.com/junegunn/vim-plug/master/plug.vim" > ~/.config/nvim/autoload/plug.vim
endif

" vim-plug manage start
call plug#begin('~/.vim/plugged')


" vim-plug manage end
call plug#begin('~/.vim/plugged')
~~~

## Commands for `vim-plug`

:PlugInstall <br>
:PlugUpdate
## Plugins

|  Plugin Names | Description | Link |
|---------------|-------------|------|
|  TreeSitter   | language highligher|
|  nvim-lspinstall | help install neovim native lsp server |
|  nvim-lspconfig | code complete and other |
|  lspkind-nvim | vs-code pictograms|
|  lspsaga.nvim | auto-import, function find etc|
|  nvim-compe | auto complete|
|  lsp_signature.nvim | show function signature when implement |
|  Friendly Snippets | code snippets |
|  Neoformatter | formatter |
|  nvim-web-devicons| nvim-tree.lua file icons |
|  nvim-tree.lua | nerdtree replacer |
|  nvim-bufferline.lua| tab line customization |
|  dashboard-nvim| dashboard |
|  telescope.nvim| fuzzy search | [show case](https://github.com/nvim-telescope/telescope.nvim/wiki/Showcase)|
|  nvim-lua/plenary.nvim| telescope.nvim dependency |
|  nvim-lua/popup.nvim | telescope.nvim dependency|
|  Tabular | line up the chunk of code "tablurize" |
|  Indent Blankline| vertical line for indent, use lua branch |
|  commentary.vim | shortcut for commentary |
|  Tagbar | class outline side window need ctag|
|  nvim-autopairs | replace of auto-pairs |
|  sneak.vim | replace of easy-motion |
|  split-term | split terminal for vim|
|  markdown-preview.nvim | markdown preview for neovim|
|  vim-buffet| buffer labeling|

## Plugin Setup

1. dashboard-nvim setup: you need use `let g:dashboard_default_executive ='telescope'`, so dashboard will use telescope for all the functions on the dashbaord

2. tree-sitter Setup

Add following in `init.vim`:

3.10/27/2021 update for `nvim-tree` plugin

you need add following in `init.vim`

~~~bash
lua require'nvim-tree'.setup {}
~~~

~~~vim
lua <<EOF
require'nvim-treesitter.configs'.setup {
  ensure_installed = {"java"}, -- one of "all", "maintained" (parsers with maintainers), or a list of languages
  highlight = {
    enable = true,              -- false will disable the whole extension
    disable = { },  -- list of language that will be disabled
  },
}
EOF
~~~

## Plugin shortcuts

default VIM shortcuts:

### fzf

~~~vim

nnoremap <leader>ff <cmd>Telescope find_files<cr>
nnoremap <leader>fg <cmd>Telescope live_grep<cr>
nnoremap <leader>fb <cmd>Telescope buffers<cr> "list current buffer and search
nnoremap <leader>fh <cmd>Telescope help_tags<cr>
~~~

### tab

~~~vim

nnoremap  <silent> <tab>  :if &modifiable && !&readonly && &modified <CR> :write<CR> :endif<CR>:bnext<CR>
nnoremap  <silent> <s-tab>  :if &modifiable && !&readonly && &modified <CR> :write<CR> :endif<CR>:bprevious<CR>
nnoremap  <silent> <A-F4>  :if &modifiable && !&readonly && &modified <CR> :write<CR> :endif<CR>:bw<CR>
~~~

### nvim-tree

~~~vim
r: rename file
x to add/remove file/directory to cut clipboard
c to add/remove file/directory to copy clipboard
y will copy name to system clipboard
p to paste from clipboard. Cut clipboard has precedence over copy (will prompt for confirmation)
gy will copy absolute path to system clipboard
d to delete a file (will prompt for confirmation)
a to create new file
~~~

### nvim-jdtls

~~~vim
nnoremap <leader>o <Cmd>lua require'jdtls'.organize_imports()<CR>
nnoremap <leader>i <Cmd>lua vim.lsp.buf.formatting()<CR>
nnoremap gD <Cmd>lua vim.lsp.buf.declaration()<CR>
nnoremap gd <Cmd>lua vim.lsp.buf.definition()<CR>
nnoremap gi <Cmd>lua vim.lsp.buf.implementation()<CR>
nnoremap gK <Cmd>lua vim.lsp.buf.formatting()<CR>
nnoremap gs <Cmd>lua vim.lsp.buf.signature_help()<CR>

nnoremap <leader>af <Cmd>lua require"jdtls".code_action()<CR>
nnoremap <leader>ah <Cmd>lua vim.lsp.buf.hover()<CR>
nnoremap <leader>ar <Cmd>lua vim.lsp.buf.rename()<CR>
nnoremap <leader>ai <Cmd>lua vim.lsp.buf.incoming_calls()<CR>
nnoremap <leader>ao <cmd>lua vim.lsp.buf.outgoing_calls()<CR>

<C-o> go back to previous location

~~~

## Install "YouCompleteMe" - out-of-date

1. Plug 'Valloric/YouCompleteMe', { 'do': './install.py' }
2. install cmake
~~~bash
sudo apt install cmake 
~~~
3. run install.py

The `install.py` is in `~/.config/nvim/plugged/YouCompleteMe/`
~~~bash
python3 ./install.py --all
~~~

## Install "nvim-cmp"

1.  Install plugins in `init.vim`

~~~bash
Plug 'hrsh7th/cmp-nvim-lsp'
Plug 'hrsh7th/cmp-buffer'
Plug 'hrsh7th/cmp-path'
Plug 'hrsh7th/cmp-cmdline'
Plug 'hrsh7th/nvim-cmp'
Plug 'hrsh7th/cmp-vsnip'
Plug 'hrsh7th/vim-vsnip'
~~~

2. copy paste the config

~~~bash
lua <<EOF
require'nvim-treesitter.configs'.setup {
  ensure_installed = {"java"}, -- one of "all", "maintained" (parsers with maintainers), or a list of languages
  highlight = {
    enable = true,              -- false will disable the whole extension
    disable = { },  -- list of language that will be disabled
  },
}
  -- Setup nvim-cmp.
  local cmp = require'cmp'

  cmp.setup({
    snippet = {
      -- REQUIRED - you must specify a snippet engine
      expand = function(args)
        vim.fn["vsnip#anonymous"](args.body) -- For `vsnip` users.
        -- require('luasnip').lsp_expand(args.body) -- For `luasnip` users.
        -- vim.fn["UltiSnips#Anon"](args.body) -- For `ultisnips` users.
        -- require'snippy'.expand_snippet(args.body) -- For `snippy` users.
      end,
    },
    mapping = {
      ['<C-d>'] = cmp.mapping(cmp.mapping.scroll_docs(-4), { 'i', 'c' }),
      ['<C-f>'] = cmp.mapping(cmp.mapping.scroll_docs(4), { 'i', 'c' }),
      ['<C-Space>'] = cmp.mapping(cmp.mapping.complete(), { 'i', 'c' }),
      ['<C-y>'] = cmp.config.disable, -- Specify `cmp.config.disable` if you want to remove the default `<C-y>` mapping.
      ['<C-e>'] = cmp.mapping({
        i = cmp.mapping.abort(),
        c = cmp.mapping.close(),
      }),
      ['<CR>'] = cmp.mapping.confirm({ select = true }),
    },
    sources = cmp.config.sources({
      { name = 'nvim_lsp' },
      { name = 'vsnip' }, -- For vsnip users.
      -- { name = 'luasnip' }, -- For luasnip users.
      -- { name = 'ultisnips' }, -- For ultisnips users.
      -- { name = 'snippy' }, -- For snippy users.
    }, {
      { name = 'buffer' },
    })
  })

  -- Use buffer source for `/` (if you enabled `native_menu`, this won't work anymore).
  cmp.setup.cmdline('/', {
    sources = {
      { name = 'buffer' }
    }
  })

  -- Use cmdline & path source for ':' (if you enabled `native_menu`, this won't work anymore).
  cmp.setup.cmdline(':', {
    sources = cmp.config.sources({
      { name = 'path' }
    }, {
      { name = 'cmdline' }
    })
  })

  -- Setup lspconfig.
  local capabilities = require('cmp_nvim_lsp').update_capabilities(vim.lsp.protocol.make_client_capabilities())
  -- Replace <YOUR_LSP_SERVER> with each lsp server you've enabled.
  require('lspconfig')['jdtls'].setup {
    capabilities = capabilities
  }
EOF

~~~

3. add lsp server:

~~~bash
  -- Replace <YOUR_LSP_SERVER> with each lsp server you've enabled.
  require('lspconfig')['jdtls'].setup {
    capabilities = capabilities
  }
~~~


4. Run "PlugInstall"