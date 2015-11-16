# How to Git #

## To Do ##

Here's a quick list of things to add to this document. Please edit the file to complete missing parts.

  1. How to install eclipse with git
  1. How to import the project into eclipse via git
  1. What to do when a merge has conflicts

## Introduction ##

This guide is intended to be short. Please edit the file if you see there's a point missing.

First things first, get familiar with git. It was developed by Linus Torvalds, and introduced [here](http://en.wikipedia.org/wiki/Git_(software)).

## Branching ##

We're going to use branches to keep the project sane. If there was only one branch called "master", and everyone was working on it, it would be a mess. So we will have these branches:

  1. master
  1. web-develop
  1. and-develop
  1. web-feature-`*`
  1. and-feature-`*`

**master** is the main branch and will have working versions.

**develop** branches will be used to store working versions which are under development.

**feature** branches will be the branches that are being worked on, with no guarantee to work. master, web-develop and and-develop branches are already created on the source. I'll try to explain by examples.


![http://imgur.com/uAYCNA3.jpg](http://imgur.com/uAYCNA3.jpg)

This shows the route to merge.




## How to add a feature to Android ##

  1. You need to checkout and-develop. On eclipse, right click the project name, team, switch to: and-develop.
> > ![http://i.imgur.com/kx36CMV.png](http://i.imgur.com/kx36CMV.png)
  1. You need to create a new branch named and-feature-x, x being the feature you're supposed to develop. Right click project > team > switch to > new branch.
> > ![http://i.imgur.com/3xXUyu9.png](http://i.imgur.com/3xXUyu9.png)
    * At this point you have to see "Source: and-develop."
    * You can enter branch name and-feature-x.
    * You can tick checkout new branch
    * Click finish, now you're in and-feature-x.
> > ![http://i.imgur.com/mAI3Epy.png](http://i.imgur.com/mAI3Epy.png)
  1. Do your development in the feature branch.
  1. COMMIT your development to the feature branch. (Team>Commit)
    * **The commit messages are extremely important**, don't skip them. Write something meaningful.
> > ![http://i.imgur.com/qgXHUdc.png](http://i.imgur.com/qgXHUdc.png)
  1. Click Commit, don't push it yet.
  1. Merge with and-develop to see if there were any changes in the and-develop since you pulled it. So merge with origin/and-develop, not with the local one. Hopefully, it will be with no errors.
> > ![http://i.imgur.com/6BkR6u1.png](http://i.imgur.com/6BkR6u1.png)
> > ![http://i.imgur.com/9WLjJSR.png](http://i.imgur.com/9WLjJSR.png)
  1. After the merge, test your development (do your unit tests here)
  1. Switch to local and-develop (and merge it with origin/and-develop)
  1. Merge with local and-feature-whatever
  1. Push and-develop to origin
  1. Here, if you're done developing you can switch to master, merge with origin/master and then merge with local and-develop. If not, you can go back to and-feature-x branch and continue developing (step 3)
  1. After merging and-develop into master (step 11), you need to push master to origin as well.
  1. You can delete your feature branch after you're done with merges and pushs.
> > ![http://i.imgur.com/RvrX2gc.png](http://i.imgur.com/RvrX2gc.png)
  1. No rebase please.

### Quick Guide ###
  1. Switch to and-develop
  1. Switch to new branch and-feature-x from and-develop
  1. Code
  1. Commit to and-feature-x
  1. DON'T PUSH, just commit
  1. Merge origin/and-develop into and-feature-x
  1. Test
  1. Switch to and-develop
  1. Merge and-feature-x into and-develop
  1. Push and-develop into origin
  1. Switch to master, merge with origin/master, merge with and-develop
  1. Push master into origin
  1. Delete branch and-feature-x (if no more needed)
  1. DON'T REBASE

This was a sum of several techniques on git. If you have a better way please bring it to one of the meetings. More reading [here](http://nvie.com/posts/a-successful-git-branching-model/) and video lecturer put on moodle is [right here](https://www.youtube.com/watch?v=1ffBJ4sVUb4).