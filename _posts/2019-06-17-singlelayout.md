---
layout: post
title: Most useful web page layout how to
tags: CSS html angular javascript
categories: common
---

As backend developer, I always don't know the CSS and html stuff, with recently working on Angular project, have to deal with the layout with CSS and html, so I write this to come up simple guide for backend developer, it is not that confusing...

## Top-Down layout 

First look the HTML:

~~~html
<header>this is header</header>
<main>this is main</main>
<footer>this is footer</footer>
~~~

defined 3 tag matching the `header`, `main` and `footer`

First look the CSS:

~~~CSS

header,footer{
    width: 1200px;
    height: 100px;
    margin: 0 auto;
    background: black;
}

main{
    width: 1200px;
    height: 600px;
    background: red;
    margin: 0 auto;
}
~~~

the `header` and `footer` style are same: fixed length and no margin, you can ignore the backgound.

notice that the `header`, `footer` are fixed length and is same size of middle, if you want to make the `header` and `footer` crossing the screen, then just change the `width` to `100%. 

## Centered Layout

~~~HTML
<div id="external">outside div
  <div id="internal">centered inside div</div>
</div>
~~~

~~~CSS

html, body {
  margin: 0;
  padding: 0;
  height: 100%;
}
#external{
  height: 100%;
  margin: 0;
  border: 10px solid yellow;
}
#internal{
  position: absolute;
  top: 50%;
  left: 50%;
  right: 0;
  bottom: 0;
  transform: translate(-50%, -50%);
  border: 10px solid red;
}
~~~