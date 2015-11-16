<a href='Hidden comment: 
=Meeting on XX.XX.XXXX X=

*Location:* X

*Time:* XX:XX-XX:XX

==Attendance==

# XX

==What was done==

* XX

==Action Items==

* XX
----
COPY THIS BELOW TO ADD A MEETING NOTE. ALSO UPDATE "THE SIDEBAR (TOC)
'></a>


# Customer Meeting on 18.12.2014 Th #

**Location:** CMPE Roof

**Time:** 09:00-10:00

## Attendance ##

  1. Suzan Üsküdarlı
  1. Mustafa Tuğrul Özşahin
  1. Kıvanç Yazan
  1. Serkan Buğur
  1. Burçin Camcı
  1. Yunus Emre Tekin
  1. Jawid Ahmed Zafar
  1. Fazilet Çilli

## What was done ##

  * We talked about poster presentations we're to make in January. We will look for best practices of preparing the poster, and search for a template.
  * What are the success criteria and how would you follow it if you are to publish the application? Think about it.
  * What is the server problem? How can we resolve it?
    * It is "PermGen Memory error", Can actually mailed us a detailed description. [Here](http://frankkieviet.blogspot.com.tr/2006/10/classloader-leaks-dreaded-permgen-space.html) and [here](http://people.apache.org/~markt/presentations/2010-11-04-Memory-Leaks-60mins.pdf) are the links with details.
    * To handle the error, we need to close the connections that we left open. Fazilet already started working on it.
  * Feedback from customer for the last milestone presentation are below.
    * Wrong picture: Is it by mistake or a vandalism?
    * Search and recommendation was to be done.
    * Work on emotion factor is required. Being able to give an emotional feedback. Sad, happy, missing, no-emotion (there is usually no emotion on facts)
    * Timeline is good. We can improve other pages by looking at it.
    * You could have people to change teams if there exist an emergency request for help.
    * Interface is great in overall. Map on landing page could be smaller, though. (Still, priority is on requirements)
  * Our items to discuss are as follows.
    1. How can we express emotion in stories?
      * Sad, happy, angry, embarrassing,.
      * Observer's emotion and author's emotion.
      * We may have an "emotion wheel" to show percentages of people who said "happy", who said "sad", but due to limited time, this function goes to future work. This is a colorful spectrum. We can build the foundation of it, and explain that it can be done.
      * There can be a checklist instead of a dropdown to pick more than one emotion.
      * Author enters emotion upon writing, reader enters emotion as he/she would rate the story.
      * Checkboxes could even have emoticons.
    1. How can we accomplish search for user names? Would it be ok if we put a dropdown, and let users choose for what they search for? What about semantic search?
      * As for semantic search, Freebase have a great API, which includes DBpedia data as well. We might utilize it.
      * Semantic search is not required for user search, do it only for stories and places.
      * You should dedicate one or two people to work on semantic search asap.
      * Don't make user choose what he/she searches for. Bring three columns for search results.
      * We can hold another meeting specifically for semantic search.
      * If you have any problems/questions, contact Tuğrul. He is more than willing to give feedback.
    1. Should we have upvote/downvotes for comments as in Reddit?
      * Could be. We could implement a very basic approach.
      * We can also let user sort the comments in reverse chronological order so that user can check what's new.
      * In short, two sorting: one for ratings, one for new.
    1. We're planning a change for database password, would that be ok?
      * Yes. Do it asap.
      * Tuğrul will coordinate password reset. We will be given a new password through email. Alp will be mailed.
      * Make use of .gitignore.
    1. On report count: When a story should be hidden automatically? Should we look at number of subscriptions of author? Should we have a safe period of say, three days, in which a story never gets deleted?
      * Implement as it is, but write about moderation on "Concerns" or "Future Work."
      * Reports might have timeouts. For example, look for reports from last 24 hours.
      * There might be exceptions for users with low number of subscribers.
    1. Functions of Android application may not be exactly the same with web application.
      * That makes sense. But be careful not to scare away users. Don't let them think "Oh cool story, let me write a comment when I go home back."

## Action Items ##

  * Alp will be mailed by Kıvanç for password change.
  * Meeting notes will be gone over by members and issues will be opened.

---


# Meeting on 15.12.2014 M #

**Location:** CMPE Roof

**Time:** 20:00-23:00

## Attendance ##

  1. Serkan Bugur
  1. Uğur Kalkan
  1. Fazilet Çilli
  1. Burçin Camcı
  1. Kıvanç Yazan
  1. Jawad Ahmed Zafer
  1. Yunus Emre Tekin

## What was done ##

  * We talked about Issues we have, we wanted to clearly see what is in front of us.
    * We briefly went over Android, which had these awaiting developments. All of these items have issues defined.
      1. Show story
      1. Edit Profile
      1. Photo upload
      1. Subscription
    * We talked about overall to do list. Here're the items we ended up with:
      1. Delete/Edit story. (We have to be careful with isDeleted=1. We might experience some bugs related to pages' not checking isDeleted==1)
      1. Delete/Edit comment
      1. Unit Tests and Javadoc
      1. Personal Logs
      1. One increment per commit
      1. Database password change
      1. Gamification
        1. Adding new story = +10 points
        1. Adding new comment = +1 points to commenter, +2 points to story owner
        1. Remembering story = +1 points for story owner
        1. Rating a story = +1 points for story owner
        1. We wanted to make it harder to level up as user collects points. Hence we decided on (n+2)^2 formula. This means, to become a level 1 user (n=1), you will need 9 points, which you can earn through writing your own story. (Remember users start with level 0)
      1. Recommendations, can be seen on three possible places. These will be brought by tags.
        1. Home page's news feed. Half of it will be reserved for recommendations (other half is subscriptions)
        1. After adding a story, user will see related stories (this might be merged with the next item)
        1. On the story page, user may see "related stories".
      1. Semantic search

## Action Items ##

  * We've arranged a customer meeting for Thursday morning. Here is the list of items that we'll discuss.
    1. How can we express emotion in stories?
    1. How can we accomplish search for user names? Would it be ok if we put a dropdown, and let users choose for what they search for?
    1. Should we have upvote/downvotes for comments?
    1. We're planning a change for database password, would that be ok?
    1. On report count: When a story should be hidden automatically? Should we look at number of subscriptions of author? Should we have a safe period of say, three days, in which a story never gets deleted?



---


# Meeting 12 (Customer Meeting) - 16.10.2014 Th #

**Location:** CMPE Roof

**Time:** 09.15-09.45

## Attendance ##

  1. Suzan Üsküdarlı (Customer)
  1. Tuğrul Özşahin (Customer)
  1. Serkan Bugur
  1. Uğur Kalkan
  1. Fazilet Çilli
  1. Burçin Camcı
  1. Kıvanç Yazan

## What was done ##

  * Project plan was discussed.
  * Requirements were discussed.
  * Overall situation of the project was discussed.

## Action Items ##

  * Members are to expand the project plan.
  * Members are to revise the requirements.

---

# Meeting 11 - 13.10.2014 M #
**Location:** CMPE Lounge

**Time:** 18:00-20:00

## Attendance ##

  1. Serkan Bugur
  1. Uğur Kalkan
  1. Fazilet Çilli
  1. Burçin Camcı
  1. Kıvanç Yazan

## What was done ##

  * Database designed was completed.
  * Project plan was completed.
  * Team was divided into two smaller subteams as follows:
    1. Android Team: Burçin, Yunus Emre, Serkan
    1. Web Team: Fazilet, Uğur, Kıvanç

## Action Items ##

  * Each member is required to make sure he/she is comfortable with the IDE Eclipse and code versioning system, git.

---


# Meeting 10 - 08.10.2014 W #

**Location:** CMPE Lounge

**Time:** 18:00-20:00

## Attendance ##

  1. Serkan Bugur
  1. Uğur Kalkan
  1. Fazilet Çilli
  1. Burçin Camcı
  1. Kıvanç Yazan

## What was done ##

  * Database was designed by group members.
  * Mockups were recreated on the board.

## Action Items ##

  * Every member will make sure they can access the repo.

---

# Meeting 9 - 29.09.2014 M #

**Location:** CMPE Lounge

**Time:** 18:00-20:00

## Attendance ##

  1. Serkan Bugur
  1. Uğur Kalkan
  1. Fazilet Çilli
  1. Burçin Camcı
  1. Yunus Emre Tekin
  1. Kıvanç Yazan

## What was done ##

  * We revised requirements.
  * We revised scenarios.
  * We revised use cases.
  * We uploaded homework on RAM.
  * We uploaded homework on revision for requirements and scenarios.
  * We edited our wiki.

## Action Items ##

  * All members are required to add meeting hours to their personal logs.
  * All members are required to upload their personal logs as their homeworks until wednesday night.
  * Serkan is to upload a brand new visual for our project homepage.


---

# Meeting 8 - 26.09.2014 Fr #

**Location:** CMPE Lounge

**Time:** 17:00

## Attendance ##

  1. Serkan Bugur
  1. Uğur Kalkan
  1. Fazilet Çilli
  1. Burçin Camcı
  1. Yunus Emre Tekin
  1. Kıvanç Yazan

## What was done ##

  * We talked about project in general.
  * We fixed a meeting time and place.
  * We agreed on a way to communicate.
  * We selected a communicator person.
  * We discussed about homeworks.
  * We created a responsibility assignment matrix.
  * We created our personal log files.

## Action Items ##

  * As selected for being communicator, Kıvanç is to write a kick-off mail and check it with other members before sending.
  * All members are required to attend the regular meetings (6 PM on mondays) from 29.09.2014 on.
  * All members are required to read requirements and scenarios, and think about ways of improving before coming to the meeting.
  * The next meeting will be about updating requirements and scenarios.


---


# Meeting 7 - 05.05.2014 M #

**Location:** ETA B3

**Time:** 16:00

## Attendance ##

  1. Serkan Bugur
  1. Naqibullah Danishjo
  1. Uğur Kalkan
  1. Esma Özelbaş
  1. Yunus Emre Tekin
  1. Kıvanç Yazan

## What was done ##

  * We talked about new homework.
  * We discussed about how can we do it.
  * We completed the homework.

## Action Items ##

  * We set up Eclipse to our computers and connected to the source via git.
  * We distributed method developing and testing as follows:

| **Method** | **Dev** | **Test** |
|:-----------|:--------|:---------|
| Sum        | Kıvanç  | Uğur     |
| Subtract   | Yunus Emre | Naqibullah |
| Multiply   | Uğur    | Serkan   |
| Divide     | Naqibullah | Kıvanç   |
| Power      | Serkan  | Esma     |
| Modulus    | Esma    | Yunus Emre |

  * We first developed our code with intended mistakes. Then testers issued the bugs.
  * Developers uptaded code with mistakes. Then testers issued bugs again.
  * Developers reversed their code in time.
  * Finally developers fixed their code so that it works.


---


# Meeting 6 - 20.03.2014 Th #

**Location:** ETA Building Entrance

**Time:** 17:05

## Attendance ##

  1. Serkan Bugur
  1. Naqibullah Danishjo
  1. Uğur Kalkan
  1. Esma Özelbaş
  1. Yunus Emre Tekin
  1. Kıvanç Yazan

## What was done ##

  * We talked about last homework.
  * We discussed about new homework.
  * We distributed action items.

## Action Items ##

  * Web: Naqibullah
  * Mobile Mockup: Yunus Emre
  * Use Case Update: Serkan
  * Class Diagram: Esma
  * Sequence Diagram 1: Kıvanç
  * Sequence Diagram 2: Uğur
  * All action items are due to monday night.


---


# Meeting 5 - 06.03.2014 Th #

**Location:** ETA A4

**Time:** 17:00

## Attendance ##

  1. Utku Alhan
  1. Serkan Bugur
  1. Naqibullah Danishjo
  1. Uğur Kalkan
  1. Esma Özelbaş
  1. Yunus Emre Tekin
  1. Kıvanç Yazan

## What was done ##

  * We talked about possible use cases.
  * We talked about five possible scenarios.
  * We shared the action items.

## Action Items ##

  * Creating UML and Scenario #1, Kıvanç, Due 8-Mar Sat 09:00
  * Scenario #2 and #3, Uğur, Due 8-Mar Sat 09:00
  * Scenario #4 and #5, Naqibullah, Due 8-Mar Sat 09:00
  * Web Mockup, Utku, Due 11-Mar Mon 19:00
  * Android Mockup, Yunus Emre, Due 11-Mar Mon 19:00
  * Updating Requirements, Serkan, Due 11-Mar Mon 19:00
  * Submitting Use Case Scenarios, Esma, Due 11-Mar Mon 19:00


---


# Meeting 4 - 28.02.2014 Fr #

**Location:** CMPE Lounge

**Time:** 17:30

## Attendance ##

  1. Utku Alhan
  1. Serkan Bugur
  1. Naqibullah Danishjo
  1. Uğur Kalkan
  1. Esma Özelbaş
  1. Yunus Emre Tekin
  1. Kıvanç Yazan

## What was done ##

  * We created a [google document](https://docs.google.com/document/d/1KbukJfgajJmaF1OHqhQtXQ27HGwDr3Uw4vm-a3-MlMY/) to edit the domain analysis and requirements page.
  * Actions items are distributed.

## Action Items ##

  * Domain Analysis: Uğur (Due 3-Mar 19:00)
  * Summary: Esma (Due 3-Mar 19:00)
  * Nonfunctional Requirements: Naqibullah (Due 3-Mar 19:00)
  * Functional Requirements - Places & Moderation : Yunus Emre (Due 3-Mar 19:00)
  * Functional Requirements - Users & Search and Sort : Kıvanç (Due 3-Mar 19:00)
  * Functional Requirements - Subscription & Recommendation : Serkan (Due 3-Mar 19:00)
  * Functional Requirements - Gamification : Utku (Due 3-Mar 19:00)
  * Items to dictionary will be added as members edit their parts.
  * Review and Submit: Utku (Due 3-Mar 23:00)


---


# Meeting 3 (Customer Meeting) - 28.02.2014 Fr #

**Location:** ETA 46

**Time:** 16:30

## Attendance ##

  1. Hande Alemdar (Assistant)
  1. Utku Alhan
  1. Serkan Bugur
  1. Naqibullah Danishjo
  1. Uğur Kalkan
  1. Yunus Emre Tekin
  1. Kıvanç Yazan

## What was done ##

  * Customer meeting with the assistant was done.
  * Following items were discussed.

  1. We should use details at the facebook account of the user to have an idea about his/her interests.
  1. Users should be able to subscribe to other users to follow up their stories in a faster manner.
  1. Users should be ranked according to their contributions to the system. These ranks might be named as extinct animals such as dinosaur, and/or historical figures such as Heredotus.
  1. Newcomers should face a tutorial, which introduces them to system and encourages them to enter new stories into the system.
  1. We should come up with a nice project name.
  1. We should show some stories to the guest users in order to let them have an idea about the system.
  1. Places should be created by users.
  1. Places should have timelines that show what existed at that place in the past.
  1. Users should be asked to fill in the blanks in the timelines of the places.
  1. Users should be awarded for their contributions.

## Action Items ##

  * We decided to meet as team after the customer meeting.


---


# Meeting 2 - 27.02.2014 Th #

**Location:** ETA A4

**Time:** 17.45

## Attendance ##

  1. Utku Alhan
  1. Naqibullah Danishjo
  1. Uğur Kalkan
  1. Esma Özelbaş
  1. Yunus Emre Tekin
  1. Kıvanç Yazan

## What was done ##

  * We talked about basics of the project. e.g. What should be on the page, which buttons should exist, what kind of gamification should exist and so on. This was a preparation for customer meeting, which will take place on 28.02.2014 Friday.
  * We created a [facebook group](https://www.facebook.com/groups/608759525870327/) to communicate.
  * We shared our new action items.

## Action Items ##

  * Customer Meeting will take place on 28-Feb. All group members will attend the meeting.
  * We decided to meet on 28-Feb after the customer meeting.
  * Domain analysis, functional requirements, non-functional requirements will be written at the meeting at 28-Feb by all group members.
  * Summary from Domain Analysis and requirements will be merged and uploaded into Moodle at the 28-Feb meeting.
  * Homepage of the project will be edited by group members until 2-Mar. Discussion about this topic will continue on facebook group.



---


# Meeting 1 - 20.02.2014 Th #

**Location:** ETA A4

**Time:** 17.21

**Attendees:** Kıvanç Yazan, Utku Alhan, Serkan Bugur, Uğur Kalkan, Naqibullah Danishjo


## What was done ##

  * Google Code Project page was created.
  * Project owners were added.
  * Meeting time was decided as after-ps hours.

## Action Items ##

  * Research topics are shared as follows:
    1. Crowdsourcing: Uğur (23.02.2014)
    1. Gamification: Utku (23.02.2014)
    1. Recommendation systems: Serkan (23.02.2014)
    1. Android / Mobile Application Development: Kıvanç (23.02.2014)
    1. Web Application Development: Naqibullah (23.02.2014)
  * Those who were not able to attend the first meeting (Vildan, Hafize Esma, Yunus Emre) were assigned to edit wiki pages for now.


---
