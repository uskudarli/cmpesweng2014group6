# Requirements (Version 4.2) #

## Summary ##
Dutluk is a platform created to let people describe how a particular part of the world used to be at a given time. People will be able to read and share their stories from this time and location with the aid of pictures. Naturally, the same location will have different stories changing over time and different aspects from different views depending on each person. When looking at any story, the map will show its location. Additionally, a timeline will be established, which will allow people to see these changes over time with the help of other people's stories on the same location. These stories can also be semantically searched by location, rating, categories and so on. Furthermore, new stories can be recommended to the user based on the user's search parameters over time and other factors like their age, the place they lived and the stories they already liked.

There will be a rating system for the stories and badges that go along with a user's activity and experience. Gamification will be used to motivate the users toward contribution. In addition to these, the user can give feedback related to a story, such as "like" or "I remember that". This feedback system not only will provide corroboration for the stories, but also will allow people to reminisce with others with a similar or the exact same story and will facilitate them to follow each other.


## Glossary (Dictionary) ##
**Guest User:** Guest User is an unregistered user of the system, who may register if he/she wishes. Guest User is able to read last added stories.

**Registered User (Member):** Registered User, also known as Member, is a user of the system who is registered in the database. He/she may login to the system and post stories.

**Story:** Story is a post by a registered member, about a place and a time. Story is supposed to include the memory of the user who writes the story about the place he/she is writing about.

**Place:** Where the story occurred.

**Timeline:** A way of displaying a list of events (stories)  in chronological order, sometimes described as a project artifact.

**Theme:** The main subject that is being discussed or described in the story.

**User-specific:** Only one user is able to create and edit his or her story. Multiple users are not allowed to create the same story simultaneously.

**Experience Points:** Again a tool of gamification, experience points are awarded to users who contribute to the site in some way. Experience points can never decrease.

**Levels:** Accumulating enough experience points will let users to reach higher levels. Level can never decrease.

**Semantic Search:** Searching by taking into account the context of the search terms and the intent of the user, instead of considering individual words separately.

## Functional Requirements ##

  * **1. Users**
    * _**1.1. Guests**_
      * 1.1.1. Guests shall be able to register by writing an e-mail address and a desired password.
    * _**1.2. Members**_
      * 1.2.1. Members shall login to the system by using their e-mail address and passwords.
      * 1.2.2. Members shall be able to edit their details, including their email addresses.
      * 1.2.3. Members shall be able to edit their passwords on the web system.
      * 1.2.4. Members shall be able to reset their passwords with an e-mail sent from the web system, in case of a forgotten password.
      * 1.2.5. Members shall have a profile page, which contains personal information about themselves.
  * **2. Places**
    * _**2.1. Timeline**_
      * 2.1.1. Every place shall have its own timeline.
    * _**2.2. Story**_
      * 2.2.1. A logged in user shall be able to create a story by giving an absolute or approximate date, place, optional tags, optional single image and the required narrative.
      * 2.2.2. Users shall be able to specify locations by name.
      * 2.2.3. Users shall be able to specify date as either by picking a specific date from a calendar, or by giving a duration through an text input.
      * 2.2.4. Place of the story to be created shall be selected through the map interface or through the search engine.
      * 2.2.5. Users shall be able to comment on stories.
      * 2.2.6. Users shall be able to see every comment that was written under the story.
      * 2.2.7. Every user shall be able to rate stories with one of "Terrible", "Not good", "It's OK", "Good", and "Great!".
      * 2.2.8. Every story shall have “I remember that” button.
  * **3. Search and Browse**
    * 3.1. Stories shall be searched through a map interface.
    * 3.2. Stories shall be searched by name of the place, story or author.
    * 3.3. Places shall be searched by name.
    * 3.4. Users shall be searched by name.
  * **4. Subscription**
    * 4.1. Users shall be able to subscribe to other users.
    * 4.2. Users shall be able to subscribe to places.
    * 4.3. Users shall see latest stories of subscribed users in web home page.
    * 4.4. Users shall see latest stories of subscribed places in web home page.
  * **5. Recommendation**
    * 5.1. The system shall provide recommended stories to logged in users.
    * 5.2. Android application shall recommend top rated 5 stories.
    * 5.3. Android application shall also make another 5 recommendations depending on user.
  * **6. Gamification**
    * _**6.1. Experience and Levels**_
      * 6.1.1. The users shall earn experience points based on their contribution (adding a new story, commenting on a story, rating a story, remembering a story) to the site.
      * 6.1.2. The users shall have levels based on their experience points.
      * 6.1.3. Levels shall be represented by a number calculated by experience points.
  * **7. Moderation**
    * 7.1. Users shall be able to report stories on web system.
    * 7.2. Reports shall be stored with their creation dates.
  * **8. Mobile Application**
    * 8.1. Critical functions available on Web should be available on Android too.
  * **9. Pages**
    * _**9.1. Home Page**_
      * 9.1.1. There shall be a map interface, navigation bar, search engine. Besides these, home page shall include news feed which shows stories from subscribed users and places.
      * 9.1.2. Login/Register widget shall be included.
    * _**9.2. Profile Page**_
      * 9.2.1. The profile picture, experience points and level of user shall be shown.
      * 9.2.2. Number of subscribers shall be shown on profile page.
      * 9.2.3. Stories written by user shall be shown.
    * _**9.3. Timeline Page**_
      * 9.3.1. There shall be stories, dates of stories, and pictures of stories on the page.
      * 9.3.2. Users shall go to the individual stories to read.

## Nonfunctional Requirements ##

  * **1. Security**
    * 1.1. The system shall protect all information about the registered user such as username, password, email, story; and should not share the private information with others.
    * 1.2. The user shall be able to access contents after a signing process.
    * 1.3. The system shall create a time session after logging process.
    * 1.4. The system shall store password using hash functions such as MD5, SHA1 or SHA256 in order to prevent possible security flaws.
    * 1.5. Users shall be sent verification emails upon registration, which contains a link or a code to activate their accounts.
    * 1.6. The system shall encrypt page request using SSL (HTTP Secure) that are returned by the browser in order to protect against eavesdropping and man-in-the-middle attacks.
  * **2. Efficiency**
    * 2.1. The user shall be responsed in maximum 3 seconds.
    * 2.2. The system should be accessible by 300 users at the same time.
  * **3. Availability**
    * 3.1. The Web application shall be supported by Safari, Mozilla Firefox, Google Chrome and Internet Explorer web browsers.
    * 3.2. The Web application shall be supported also by popular frameworks such as Bootstrap and UIKit in order to provide responsiveness to users using mobile phones and tablets.
    * 3.3. An Android application shall also be available for mobile devices.
    * 3.4. The mobile application shall be supported by Android 4.0 and later versions.
    * 3.5. The system shall be implemented using Java, since the mobile application runs on Android, and a Java web tool shall be used in web application.
  * **4. Reliability**
    * 4.1. All the records and database shall be copied monthly for possible failures.
    * 4.2. Maintenance periods shall be planned annually.
  * **5. Modifiability**
    * 5.1. Developers shall be able to modify the system in order to answer the varying needs of the users and customers.
  * **6. Usability**
    * 6.1. Usability shall be tested with several user groups by user tests.

### References ###
  * http://searchsoftwarequality.techtarget.com/definition/HTTPS
  * http://mashable.com/2011/05/31/https-web-security/
  * http://en.wikipedia.org/wiki/Bootstrapping