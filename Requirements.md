# Requirements #

## Summary ##
Dutluk is a platform created to let people describe how a particular part of the world used to be at a given time. People will be able to read and share their stories from this time and location with the aid of pictures, videos and audio recordings. Naturally, the same location will have different stories changing over time and different aspects from different views depending on each person. When looking at any memory, the map will show its location. Additionally, a time tunnel will be established, which will allow people to see these changes over time with the help of other people's stories on the same location. These stories can also be semantically searched by location, rating, categories and so on. Furthermore, new stories can be recommended to the user based on the user's search parameters over time and other factors like their age, the place they lived and the stories they already liked.

There will be a rating system for the stories and badges that go along with a user's activity and experience. Gamification will be used to motivate the users toward contribution. In addition to these, the user can give feedback related to a memory, such as "like", "dislike" or "I remember that". This feedback system not only will provide corroboration for the stories, but also will allow people to reminisce with others with a similar or the exact same story and will facilitate them to follow each other.


## Glossary (Dictionary) ##
**Guest User:** Guest User is an unregistered user of the system, who may register if he/she wishes. Guest User is able to read last added stories.

**Registered User (Member):** Registered User, also known as Member, is a user of the system who is registered in the database. He/she may login to the system and post stories.

**Story:** Story is a post by a registered member, about a place and a time. Story is supposed to include the memory of the user who writes the story about the place he/she is writing about.

**Badges:** Badges are a tool of gamification, which are awarded to users who accomplish a certain task (such as sharing a certain number of stories). Once a badge has been given to a user, it is permanent.

**Experience Points:** Again a tool of gamification, experience points are awarded to users who contribute to the site in some way. Experience points can never decrease.

**Levels:** Accumulating enough experience points will let users to reach higher levels. Level can never decrease.

**Leaderboards:** To motivate content contribution via competition, some statistics (such as most contributing users) will be kept track of. The table of the best users is the leaderboard for that statistic.

**Semantic Search:** Searching by taking into account the context of the search terms and the intent of the user, instead of considering individual words separately.

**Collaborative Filtering:** To recommend items based on similarity measures between users and items.

## Functional Requirements ##

  * **1. Users**
    * _**1.1. Guests**_
      * 1.1.1. Guests shall be able to read last added stories.
      * 1.1.2. Guests shall be able to register by writing an e-mail address and a desired password.
    * _**1.2. Members**_
      * 1.2.1. Members shall login to the system by using their e-mail address and passwords.
      * 1.2.2. Members shall be able to edit their details, including their email  addresses and passwords.
      * 1.2.3. Members shall be able to reset their password with an e-mail sent from the system, in case of a forgotten password.
      * 1.2.4. Members shall be presented with a tutorial page upon their first successful login.
  * **2. Places**
    * _**2.1. Timeline**_
      * 2.1.1. Every landmark (place) shall have its own timeline.
      * 2.1.2. Users will be encouraged about sharing stories of previous history of these places.
    * _**2.2. Story**_
      * 2.2.1. There shall be a categorization about stories according to place, theme, time, age, sex and language. Place means where the story occured. Theme is going to be like entertainment, culture, sport, etc. Time means when the story occured. Users’ age and sex will be important about categorization of stories. Story’s language is also an important category.
      * 2.2.2. Users shall be able to comment on any stories and they can see every comment that written under the story like YouTube comments.
      * 2.2.3. Every user shall be able to rate stories over one to five, to help with gamification and recommendation.
      * 2.2.4. Apart from the rating system, every story shall have “I remember that” and “like” buttons.
      * 2.2.5. Users shall be able to add picture to stories about how places used to be.
  * **3. Search and Sort**
    * 3.1. Stories shall be searched through a map interface.
    * 3.2. Stories shall be searched by providing a text input about the place, story or author being searched about.
    * 3.3. If a search returns nothing, system shall still provide some results by making use of semantic search.
    * 3.4. Places shall be sorted according to their popularity, date that earliest story corresponds to, or date that latest post corresponds to.
    * 3.5. Users shall be sorted according to their popularity, their stories’ popularity, or number of stories they wrote.
  * **4. Subscription**
    * 4.1. The system shall provide a subscription system.
    * 4.2. Only the registered users shall subscribe to the authors to get notifications when the subscribed authors post a new memory.
  * **5. Recommendation**
    * 5.1. The system shall provide a recommendation system.
    * 5.2. Only registered users shall be able to see recommendations.
    * 5.3.The recommended memories shall have same or similar author, location, subject and tag information.
    * 5.4. The recommendations shall be shown to registered users under a specific recommendations tab.
    * 5.5. There shall be a rating option on memories, locations and authors. (0-5 stars)
    * 5.6. There shall be an option, “I remember that”, to verify the memory’s authenticity.
    * 5.7.The system shall recommend memories to the registered user by taking into account the memories that the user rated and remembered.
    * 5.8. Recommendations by collaborative filtering systems shall be added for registered users especially for the newcomers.
    * 5.9. There shall be a tab which shows most viewed and highest rated memories, locations and authors.
  * **6. Gamification**
    * _**6.1. Experience and Levels**_
      * 6.1.1. The users shall earn experience points based on their contribution to the site.
      * 6.1.2. The users shall have levels based on their experience points.
    * _**6.2. Badges**_
      * 6.2.1. The system shall award badges to users for different types of activities, such as sharing stories, translating, etc.
      * 6.2.2. There shall be four badges for each activity: Bronze, Silver, Gold, and the final badge named after a historical figure, such as Heredotus.
    * _**6.3. Leaderboards**_
      * 6.3.1. The system shall keep track of the following statistics and display daily, weekly, monthly and all-time leaderboards. The statistics are: highest rated stories, most active cities, most active places within a city, most contributing users, most story-sharing users, most translating users, highest ranked users.
      * 6.3.2. Leaderboards shall be updated daily.
    * _**6.4. Mayorship**_
      * 6.4.1 The system shall award the person who has contributed the highest amount of points by writing about a certain city in the last 30 days with mayorship.
      * 6.4.2. The system shall update the mayor of cities every week.
  * **7. Moderation**
    * 7.1. The stories that have been reported/disliked a lot (above a certain threshold) will be hidden unless an user explicitly wants to see it.


## Nonfunctional Requirements ##

  * **1. Security**
    * 1.1. The system shall protect all information about the registered user such as username, password, email, story; and should not share the private information with others.
    * 1.2. The user shall be able to access contents after a signing process.
    * 1.3. The system shall create a time session after logging process.
    * 1.4. The system shall store password using hash functions such as MD5, SHA1 or SHA256 in order to prevent possible security flaws.
    * 1.5. The system shall encrypt page request using SSL (HTTP Secure) that are returned by the browser in order to protect against eavesdropping and man-in-the-middle attacks.
  * **2. Efficiency**
    * 2.1. The user shall be responsed in maximum 3 seconds.
    * 2.2. The system should be accessible by 3000 users at the same time.
  * **3. Availability**
    * 3.1. The Web application shall be supported by Safari, Mozilla Firefox, Google Chrome and Internet Explorer web browsers.
    * 3.2. The Web application shall be supported also by some popular frameworks such as Bootstrap and UIKit in order to provide responsiveness to users using mobile phones and tablets.
    * 3.3. An Android application shall also be available for mobile devices.



### References ###
  * http://searchsoftwarequality.techtarget.com/definition/HTTPS
  * http://mashable.com/2011/05/31/https-web-security/
  * http://en.wikipedia.org/wiki/Bootstrapping