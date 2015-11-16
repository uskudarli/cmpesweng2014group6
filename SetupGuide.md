### Setup Guide ###

  * 1. **Install [Apache Tomcat 8.0](http://tomcat.apache.org/download-80.cgi)**
  * 2. **Install [JDK 7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html) and [JRE 1.7](http://www.oracle.com/technetwork/java/javase/downloads/java-se-jre-7-download-432155.html)**
  * 3. **Install [Eclipse JEE (for Java Developers)](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/keplerr)**
  * 4. **Install Git**
    * Open Eclipse, then go to Help-Install New Software
    * Install _Eclipse Git Team Provider_
    * Clone a Git Repository (from Google Code) using the information [here](https://code.google.com/p/cmpesweng2014group6/source/checkout)
    * At first, you have to clone the repository **once**, then, for every time you work, you should _pull_ changes. After your changes, you should _commit_ and _push_ using Team option of the project
  * 5. **Setup Tomcat server on Eclipse**
    * Select _Servers_ from View
    * Right click, then select New Server
    * Select _Apache Tomcat v8.0 Server_ as server type
    * 
  * 6. **Create New Dynamic Project**
    * Select File-New-Dynamic Web Project from toolbar
    * Write _451Project_ as Project Name
    * Select _Apache Tomcat v8.0_ as Target Runtime
    * Finish
  * 7. **Create Hello World Servlet**
    * Right click on the project, then select New-Servlet
    * Write _HelloWorld_ as class name
    * Change _doGet_ function with this:
```
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        pw.println("");
        pw.println("");
        pw.println("");
        pw.println("<h1>Hello World</h1>");
        pw.println("");
    }
```
    * Run
  * 8. **Javadoc**
    * Open Eclipse, then select Project-Javadoc
    * Click browse, then select _'documentary'_ folder that is created before.