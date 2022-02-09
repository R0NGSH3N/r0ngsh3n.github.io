---
layout: post
title: Git Branch best Practice
tags: git
categories: common
---

## Create Branch

~~~bash
    git branch pending-merge-branch
~~~

## Switch to new Branch

    By default, the `master` is the current active branch, to work on new branch, you need to switch to new branch

    you can use following command to show current branch:
~~~bash
    git branch
    git branch --show-current
~~~

    now switch to new branch:

~~~bash
    git checkout pending-merge-branch 
~~~

## Update file

    This is the content will update in branch "update-article" not on master

