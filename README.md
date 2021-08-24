<?xml version="1.0" encoding="UTF-8"?>
<module type="JAVA_MODULE" version="4" />

# Course "Java Developer", OTUS

# Content:
* [Student](#Student)
* [Module hw01-gradle](#Module-hw01-gradle)
* [Module hw02-DIY-ArrayList](#Module_hw02-DIY-ArrayList)
* [Module hw03-Reflection](#Module_hw03-Reflection)
* [Module hw04-GCComparisons](#Module_hw04-GCComparisons)
* [Module hw05-AOP](#Module_hw05-AOP)
* [Module hw06-ATM](#Module_hw06-ATM)
* [Module hw07-MessageHandler](#Module_hw07-MessageHandler)
* [Module hw08-JsonObjectWriter](#Module_hw08-JsonObjectWriter)
* [Module hw09-Jdbc](#Module_hw09-Jdbc)
* [Module hw09-Hibernate](#Module_hw10-Hibernate)
* [Module hw11-MyCache](#Module_hw11-MyCache)
* [Module hw12-WebServer](#Module_hw12-WebServer)
* [Module hw13-DI](#Module_hw13-DI)
* [Module hw14-SpringMVC](#Module_hw14-SpringMVC)
* [Module hw15-Executors](#Module_hw15-Executors)
* [Module hw16-MessageSystem](#Module_hw16-MessageSystem)
* [Module hw17-MicroServices](#Module_hw17-MicroServices)

# Student
Student: Vadim Lyulin
Course: Java Developer
Group: 2020-06

## Module hw01-gradle<a name="Module-hw01-gradle"></a>
Homework: Gradle project with modular structure

Objective: to learn how to create a Gradle project (Maven), prepare for homework.
1) Create an account at github.com (if not already)
2) Create a homework repository
3) Checkout the repository on your computer
4) Create a local branch hw01-gradle
5) Create gradle project
6) Add the latest version of the dependency to the project
<groupId> com.google.guava </groupId>
<artifactId> guava </artifactId>
7) Create Module hw01-gradle
8) In the module, make the HelloOtus class
9) In this class, make a call to some method from guava
10) Create "fat-jar"
11) Make sure the fat-jar starts.
12) Make a pull-request to gitHub
13) Send the link to PR for verification (personal account, chat with a teacher).

If you wish, you can create a maven project and then work with maven on the course.
For Maven, the instructions are similar (just replace Gradle with Maven in the text).

## Module hw02-DIY-ArrayList<a name="Module_hw02-DIY-ArrayList"></a>
Homework: DIY ArrayList

Objective: to study how the standard collection ArrayList works. Practice building your collection.
Write your own implementation of ArrayList based on the array.
class DIYarrayList<T> implements List<T>{...}

Check that methods from java.util.Collections work on it:
Collections.addAll(Collection<? super T> c, T... elements)
Collections.static <T> void copy(List<? super T> dest, List<? extends T> src)
Collections.static <T> void sort(List<T> list, Comparator<? super T> c)

## Module hw03-Reflection<a name="Module_hw03-Reflection"></a>
Homework: Your own test framework
Objective: to learn how to work with reflection and annotations, to understand how the junit framework works.
Write your own test framework.

Maintain your annotations @Test, @Before, @After.

Run by calling a static method with the name of the class with tests.

What you need to do:
1) create three annotations - @Test, @Before, @After.
2) Create a test class containing annotated methods.
3) Create a "test launcher". At the entrance, it should receive the name of the class with the tests, in which the methods marked with annotations and paragraph 1 should be found and run.
4) The startup algorithm should be as follows:
Before method (s)
the current Test method
After method (s)
for each such "triplet" it is necessary to create OWN object of the test class.
5) An exception in one test should not interrupt the entire testing process.
6) Based on the exceptions that occurred during testing, display the statistics of test execution (how many were successful, how many fell, how many were in total).

## Module hw04-GCComparisons<a name="Module_hw04-GCComparisons"></a>
Homework: Comparison of different garbage collectors
Objective: to understand the impact of garbage collectors using a simple application as an example.
Write an application that monitors garbage collections and logs the number of assemblies of each type
(young, old) and the time spent on assemblies per minute.

Achieve OutOfMemory in this app through slow memory drip
(for example add items to List and only remove half).


Configure the application (you can add Thread.sleep (...)) so that it crashes
with OOM about 5 minutes after starting work.

Collect statistics (number of builds, build time) for different GCs.

!!! Draw conclusions !!!
THIS IS THE MOST IMPORTANT PART OF THE WORK:
Which gc is better and why?

Conclusions should be formatted in the file Сonclusions.md in the root of the project folder.
Summarize the measurement results in the table.

Try this experiment on a small heap of about 256MB, and on the maximum possible that you can have.

## Module hw05-AOP<a name="Module_hw05-AOP"></a>
Automatic logging.
Objective: To understand how AOP is implemented, what technical means are for this.
Develop this functionality:
a class method can be marked with a homemade @Log annotation, for example, like this:

class TestLogging {
@Log
public void calculation(int param) {};
}

When this method is called "automatically", the parameter values must be logged to the console.
For example like this:

class Demo {
public void action() {
new TestLogging().calculation(6);
}
}

The console should contain:
executed method: calculation, param: 6

Please note: there should not be an explicit call to logging.

Please note that the annotation can be placed, for example, on the following methods:
public void calculation(int param1)
public void calculation(int param1, int param2)
public void calculation(int param1, int param2, String param3)

P.S.
Choose an implementation with ASM if you really want it and are confident in your abilities.
Assessment criteria: The assessment system is as close as possible to the usual school:
3 or more - the task is accepted (satisfactory).
below - the task is returned for revision.

## Module hw06-ATM<a name="Module_hw06-ATM"></a>
ATM emulator
Objective: To put into practice the principles of SOLID.
Write an ATM (ATM) emulator.

An ATM class object must be able to:
- accept banknotes of different denominations (each denomination must have its own cell)
- issue the requested amount with the minimum number of banknotes or an error if the amount cannot be dispensed
This task is not for algorithms, but for design.
Therefore, there is no need to optimize the issue.
- issue the amount of the balance of funds

In this assignment, think more about the architecture of your application.
Do not be distracted by creating such objects as: user, authorization, keyboard, display, UI (console, Web, Swing), currency, account, card, etc.
All this is not only unnecessary, but also harmful!
Assessment criteria: The assessment system is as close as possible to the usual school:
3 or more - the task is accepted (satisfactory).
below - the task is returned for revision.

## Module hw07-MessageHandler<a name="Module_hw07-MessageHandler"></a>
Homework
Message handler
Objective: To apply design patterns in practice.
Implement todo from homework module.

## Module hw08-JsonObjectWriter<a name="Module_hw08-JsonObjectWriter"></a>
Your json object writer
Objective: To learn how to serialize an object in json, to practice parsing the structure of an object.
Write your json object writer (object to JSON string) similar to gson based on javax.json.

Gson does it like this:
Gson gson = new Gson();
AnyObject obj = new AnyObject(22, "test", 10);
String json = gson.toJson(obj);

Do this:
MyGson myGson = new MyGson();
AnyObject obj = new AnyObject(22, "test", 10);
String myJson = myGson.toJson(obj);

You should get:
AnyObject obj2 = gson.fromJson(myJson, AnyObject.class);
System.out.println(obj.equals(obj2));

Support:
- primitive types
- arrays of primitive types
- collections (interface Collection)
don't forget that obj can be null

## Module hw09-Jdbc<a name="Module_hw09-Jdbc"></a>

Homemade ORM
Objective: Learn to work with jdbc. In practice, master the layered architecture of the application.
The job is to use the H2 database.

Create a User table in the database with the following fields:

• id bigint(20) NOT NULL auto_increment
• name varchar(255)
• age int(3)

Create your @Id annotation

Create a User class (with fields that correspond to the table, annotate the id field).

Implement the JdbcMapper <T> interface that can work with classes that have a @Id annotated field.
JdbcMapper <T> must save the object to the base and read the object from the base.
To do this, you need to implement the remaining interfaces from the mapper package.
With such an overview, you get an add-on over DbExecutor <T>, which, according to a given class, is able to generate sql-queries.
And DbExecutor <T> should execute the generated queries.

The table name must match the class name, and the class fields are columns in the table.

Check it out on the User class.

Take the HomeWork class as a basis.

Create another Account table:
• no bigint(20) NOT NULL auto_increment
• type varchar(255)
• rest number

Create an Account class for this table and test the JdbcMapper on this class.

## Module hw10-Hibernate<a name="Module_hw10-Hibernate"></a>

Using Hibernate
Objective: To practice the basics of Hibernate.
Understand how annotations-hibernate affect the shaping of sql queries.
The job is to use the H2 database.

Take as a basis the previous DZ (Homemade ORM),
use the api suggested in the webinar (api package)
and implement the functionality of saving and reading the User object through Hibernate.
(Reflection is no longer needed)
The Hibernate configuration should be moved to a file.

Add fields to User:
address (OneToOne)
class AddressDataSet {
private String street;
}
and phone (OneToMany)
class PhoneDataSet {
private String number;
}

Mark up the classes so that when you save / read the User object, nested objects are cascaded / read.

IMPORTANT.
1) Hibernate only needs to create three tables: for phones, addresses and users.
2) When saving a new object, there should be no updates.
Look in the logs and check that these two requirements are met.
Assessment criteria: The assessment system is as close as possible to the usual school:
3 or more - the task is accepted (satisfactory).
below - the task is returned for revision.

## Module hw11-MyCache<a name="hw11-MyCache"></a>
Custom cache engine
Objective: Learn to use WeakHashMap,
understand the basic principle of organizing caching.
Finish the MyCache implementation from the webinar.
Use WeakHashMap to store values.

Add caching to the DBService from the Hibernate ORM or DIY ORM job.
For the sake of simplicity, copy the required classes into this DZ.

Make sure that your cache is actually faster than the DBMS and flushes when it runs out of memory.

## Module hw12-WebServer<a name="hw12-WebServer"></a>
Web server
Objective: To learn how to create server and user http interfaces.
Learn to embed a web server into a ready-made application.
Embed a web server into the application from the Hibernate ORM remote control (or embed the Hibernate remote control into the example from the webinar :)).
Make a start page where the admin must authenticate.
Create an admin page for working with users.
The following functions should be available on this page:
- create user
- get a list of users

## Module hw13-DI<a name="hw13-DI"></a>

Homework (Own IoC container)
Objective: In the process of creating your context, understand how the main part of the Spring framework works.
Mandatory part:

- Download a blank multiplication table simulator application from the repository with examples
- In the AppComponentsContainerImpl class, implement the processing received in the configuration constructor, based on the annotation markup from the appcontainer package.
It is also necessary to implement the getAppComponent methods.
- As a result, you should get a working application. You can only change
AppComponentsContainerImpl class

Additional task (you can skip it):
- Divide AppConfig into several classes and distribute component creation across them. Add a constructor to AppComponentsContainerImpl that handles multiple configuration classes

Additional task (you can skip it):
- In AppComponentsContainerImpl add a constructor that takes the package name as input and processes all the configuration classes available there (see dependencies in pom.xml)

##Module hw14-SpringMVC<a name="hw15-SpringMVC"></a>

Homework
Spring MVC Web Application
Objective: To learn how to create war packages and run them in TomCat.
Learn to use Thymeleaf.
- Collect war for the application from DZ about Web Server
- Create main application classes like Spring beans (Cache, Dao, DBService)
- Configure dependencies using Java / Annotation based configuration
- Use @Controller and / or @RestController to process requests
- Use Thymeleaf as a template engine
- Run a web application on an external web server

You don't need to do authorization and authentication.

##Module hw15-Executors<a name="hw15-Executors"></a>
Sequence of numbers
Objective: To master the basic synchronization mechanisms.
Two streams print numbers from 1 to 10, then from 10 to 1.
It is necessary to make the numbers alternate, i.e. I got the following conclusion:
Stream 1: 1 2 3 4 5 6 7 8 9 10 9 8 7 6 5 4 3 2 1 2 3 4 ....
Stream 2: 1 2 3 4 5 6 7 8 9 10 9 8 7 6 5 4 3 2 1 2 3 ....

Should always start Stream 1.

##Module hw16-MessageSystem<a name="hw16-MessageSystem"></a>

Homework
MessageSystem
Purpose: In practice, master the architectural approach "Message system".
Add a messaging system to DZ about a web server with an IoC container
(it's easier to use Spring Boot here).
Forward messages from websocket to DBService and back.

For how to work with websockets, see the example from the Asynchronous Web applications webinar.

Add MessageSystem as a Module, following the example from the webinar.

##Module hw17-MicroServices<a name="hw17-MicroServices"></a>

Homework
MessageServer
Objective: To learn how to develop network applications.
The server from the previous DZ about MessageSystem is divided into three applications:
▪ MessageServer
▪ Frontend
▪ DBServer

- Make MessageServer socket server, Frontend and DBServer clients
- Forward messages from Frontend to DBService via MessageServer
- Run application with two Frontend and two DBService (but on the same database) on different ports
- Start Frontend and DBService "by hand"

- Optionally, you can launch Frontend and DBServer from MessageServer
- Such a launch must be "alienable", i.e. the "build" should run on another computer without much hassle.

### Teachers
Sergey Petrelevich<br>
Strekalov Pavel <br>
Alexander Orudzhev <br>
Vyacheslav Lapin <br>
Vitaly Kutsenko <br>

