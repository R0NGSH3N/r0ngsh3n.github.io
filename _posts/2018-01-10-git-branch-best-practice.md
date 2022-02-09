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

## Update file in branch `update-article`

    "This is the content will update in branch "update-article" not on master"

    after this, just `add` and `commit` in current branch


## diff between branches

~~~bash
❯ git diff master..update-article
diff --git a/_posts/2018-01-10-git-branch-best-practice.md b/_posts/2018-01-10-git-branch-best-practice.md
index fd71f44..6f17938 100644
--- a/_posts/2018-01-10-git-branch-best-practice.md
+++ b/_posts/2018-01-10-git-branch-best-practice.md
@@ -29,4 +29,5 @@ categories: common
 
 ## Update file
 
+    This is the content will update in branch "update-article" not on master

~~~

explain in picture:

    a/file <- master
     \ 
     b/file <- update-article


## Update file in branch `master`

    now we switch back the master branch, and updat the same file to simulate 2
    people working on different branch on same file

~~~bash
    git checkout master
~~~
