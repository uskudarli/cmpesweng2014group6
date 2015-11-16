# UML Diagram #

![http://imgur.com/JUX9Ox3.jpg](http://imgur.com/JUX9Ox3.jpg)

# Use Cases #


---

## Use Case 1 ##

**Name** : Register


**Actor** : Guests


**Goal** : Registering to the system


**Preconditions**
  * _Username and e-mail_ informations provided by the user must be unique.
  * _Password_ must contain at least an uppercase,lowercase letter and a number. It must be between 6 to 12 characters.

**Steps**
  * User enters the _Username, Password, E-mail_ informations to the system.
  * An e-mail is sent to user that includes confirmation link.

**Postconditions**
  * System creates an account for the user.
  * User shall login to the system as a registered member.


---

## Use Case 2 ##

**Name** : Sign In


**Actor** : Members


**Goal** : Login to the system


**Preconditions**
  * User must have an account

**Steps**
  * User enters the _username, password_ informations.
  * System checks the validity of the informations.

**Postconditions**
  * User logins to the system.
  * If the informations are not valid user should be warned that he/she might have made a mistake.


---

## Use Case 3 ##

**Name** : Search a story via Search engine


**Actor** : Members


**Goal** : Search for a story in the system


**Preconditions**


**Steps**
  * User decides on the keywords and/or date and/or location information to search for a story.
  * The system brings up a story only if the title or tags contain one of the specified keywords or the given date and/or the location is matched.

**Postconditions**
  * User sees the results of his/her query.
  * If a suitable match for the query is not found then the system recommends stories to the user by means of the keywords, location and date.


---

## Use Case 4 ##

**Name** : Search a story through map interface


**Actor** : Members


**Goal** : Browse the map and find stories associated with locations


**Preconditions**
  * There should be stories associated with locations and these stories should be tagged.


**Steps**
  * User opens the map interface.
  * He/she browses the map to find stories.

**Postconditions**
  * User could browse the stories according to location information that he/she browsed from the map.



---

## Use Case 5 ##

**Name** : Create a Story


**Actor** : Members


**Goal** : Creating a new story and adding it to the system.


**Preconditions**
  * User must login to the system


**Steps**
  * User writes a new story.
  * He/she defines at least two tags and one of them should be location.


**Postconditions**
  * All users can read the story.
  * Members can rate, remember the story. They can also comment on it.
  * Story can be searched via search engine and map interface.


---

## Use Case 6 ##

**Name** : Read a Story


**Actor** : Guests and Members


**Goal** : Reading a story


**Preconditions**
  * At least one story should be presented in the system.

**Steps**
  * Story is shown to the user.
  * User reads the story.

**Postconditions**
  * User might rate, remember the story and comment on it if he/she is a registered user.
  * User might subscribe to story owner to check for other stories of the owner.


---

## Use Case 7 ##

**Name** : Subscribe to Other Users


**Actor** : Member


**Goal** : Subscribe to a user


**Preconditions**
  * N/A


**Steps**
  * User sees a story in the system and reads it. He/she might rate,remember and comment on it.
  * He/she realizes that he/she should subscribe to this user to see the subscribed user's previous and future posts.
  * User subscribes to the other user.

**Postconditions**
  * User can see the stories added or to be added by the subscribed user in the subscribed user's profile page.
  * User can get notifications from this user's actions in the system.


---

## Use Case 8 ##

**Name** : Unsubscribe from Users


**Actor** : Members


**Goal** : To unsubscribe from a user


**Preconditions**
  * N/A


**Steps**
  * User unsubcribes from the subscribed user. He/she might not like the posts of the subscribed user.

**Postconditions**
  * The profile of the subscribed user is no longer shown to the user.
  * User can not receive notifications from this user's actions.


---

## Use Case 9 ##

**Name** : Subscribe to Locations


**Actor** : Members


**Goal** : Subscribe to a location to see the related stories


**Preconditions**
  * N/A

**Steps**
  * User subscribes to a location to see the related stories about this location.


**Postconditions**
  * User can see the stories about the location he/she subscribed in the page of the location.



---

## Use Case 10 ##

**Name** : Unsubscribe from Locations


**Actor** : Members


**Goal** : To unsubscribe from a location that is subscribed by the user


**Preconditions**
  * N/A

**Steps**
  * User unsubscribes from the location.


**Postconditions**
  * The page of the subscribed location is no longer shown to the user.
  * User can not receive notifications that is related to this location.



---

## Use Case 11 ##

**Name** : Rate a Story


**Actor** : Members


**Goal** : To rate a story


**Preconditions**
  * User should have read the story.

**Steps**
  * User rates the story from 1 to 5 stars.


**Postconditions**
  * System makes the required calculations to add this rate to the statistics.



---

## Use Case 12 ##

**Name** : Make Comment on a Story


**Actor** : Members


**Goal** : To make a comment to a story


**Preconditions**
  * User should have read the story.

**Steps**
  * User writes his/her comment about the story.


**Postconditions**
  * New comment is shown along with other comments that is made for that particular story.



---

## Use Case 13 ##

**Name** : See the Recommended Stories


**Actor** : Members


**Goal** : To see the stories that are recommended by the system to the user


**Preconditions**
  * User should read some stories.
  * There should be stories with similar tags with the story that is being read or rated by the user.

**Steps**
  * User reads a story. After looking into tags and keywords of the story system recommends some stories to the user.
  * User selects one of the recommended stories and read it. After reading he/she may rate, remember and make comment on it.


**Postconditions**
  * User can reach the recommended stories in the system.


---
