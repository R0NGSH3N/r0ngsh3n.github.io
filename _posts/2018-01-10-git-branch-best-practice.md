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

## Update file on master branch

    update this file on master branche now....after update on `update-article`
    branch

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

    and then update the same article


 "-" present `master` branch and "+" present `update-article` branch


## Check the log on each branch

~~~bash
     git log master..update-article
commit b859357a06bbaafa899a24ee9aae3bf5370f27fc (HEAD -> update-article)
Author: r0ngsh3n <rong.shen@outlook.com>
Date:   Wed Feb 9 12:31:24 2022 -0500

    update artile in branch update-article

commit f2a9f51697612357177553095844515b48c189a8
Author: r0ngsh3n <rong.shen@outlook.com>
Date:   Wed Feb 9 12:22:38 2022 -0500

    update artile on new branch

~~~

## show git history graphic
~~~bash
git log --all --oneline --graph --decorate
* e9b25df (master) update article on master branch
| * b859357 (HEAD -> update-article) update artile in branch update-article
| * f2a9f51 update artile on new branch
|/  

~~~


## Merge from `update-article` to `master` 

1. You need to flip back to the master branch
2. run `git merge update-artcle` command
3. Because we update the article on both branch, merge will fail:

~~~bash
❯ git merge update-article
Auto-merging _posts/2018-01-10-git-branch-best-practice.md
CONFLICT (content): Merge conflict in _posts/2018-01-10-git-branch-best-practice.md
Automatic merge failed; fix conflicts and then commit the result.

~~~

4. The conflict part will be save in the file itself!

"<<<<<< HEAD" and "=====" determine between content is from `master` branch
">>>>>> update-article" determine content from `update-artcle` branch

5. resolve the issue, remove those ">" and "=" then do `add` and `commit`, it
   will push to mater branch
