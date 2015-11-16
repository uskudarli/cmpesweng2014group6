![http://www.techpurge.com/wp-content/uploads/2013/11/7564325198_6b7e77cfce_b.jpg](http://www.techpurge.com/wp-content/uploads/2013/11/7564325198_6b7e77cfce_b.jpg)

The word Android stems from the Greek andro (human). It literally means "man-like" and was being used to denote "automaton resembling a human being", until the smartphone era.

# Android Operating System #

It is a unix-like operating system that is designed mostly for touchscreen mobile devices. Its source code is released under the Apache License, yet most devices that has Android OS ship with proprietary software as well. According to statistics of the Google Play, there are 1M+ apps published with 50B+ downloads. It's one of the most popular platforms among developers.

# Development for Android #

Software development for Android usually happens in Java. Although there are several tools, the most popular one seems to be Android Software Development Kit. Android SDK is available for all three of Windows, Linux and OS X platforms. Android SDK has a lot to offer: Android-specific refactoring, enhanced xml editors, graphical ui builders, simulation, debugging via usb and more. In order to use debugging with usb, one must enable developer settings menu on his/her device. This screen is hidden by default, so one must see "about phone" tab under settings, and tap "build number" seven times. Android SDK also creates a nice test environment for the developer.

# Hello World #

  * Download the SDK at [developer.android.com](http://developer.android.com/sdk/index.html).
  * Let's integrate this with Eclipse, assuming it's already installed. For this, you need to give the address `https://dl-ssl.google.com/android/eclipse/` to Eclipse.
  * Find the 'Android Virtual Device Manager', and add a new AVD. This will be used when testing.
  * Open the main activity file.
  * Add this line:

`import android.widget.TextView;`
  * Add following lines to onCreate function:

> `TextView hello = new TextView(this);`

> `hello.setText("Hello World");`

> `setContentView(hello);`

  * Save and run. Now you're ready to explore the world of Android.

# Read more #

[Android OS Development](http://en.wikipedia.org/wiki/Android_software_development)

[Your First Android Application](http://developer.android.com/training/basics/firstapp/creating-project.html)