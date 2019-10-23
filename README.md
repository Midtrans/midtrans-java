Skip to content
Search or jump to…

Pull requests
Issues
Marketplace
Explore
 
@Xaxxis 
1
00Xaxxis/midtrans-java
 Code Issues 1 Pull requests 0 Projects 0 Wiki Security Insights Settings
midtrans-java
/
README.md
 

1
# Midtrans Client - Java
2
[ ![Download](https://api.bintray.com/packages/midtrans/midtrans-java/com.midtrans/images/download.svg) ](https://bintray.com/midtrans/midtrans-java/com.midtrans/_latestVersion) [![Build Status](https://travis-ci.org/Xaxxis/midtrans-java.svg?branch=master)](https://travis-ci.org/Xaxxis/midtrans-java)
3
​
4
Midtrans :heart: Java, This is the Official Java API client/library for Midtrans Payment API. Visit [https://midtrans.com](https://midtrans.com). More information about the product and see documentation at [http://docs.midtrans.com](https://docs.midtrans.com) for more technical details. This library used java version 1.8
5
​
6
## 1. Installation
7
​
8
### 1.a Using Maven or Gradle
9
If you're using Maven as the build tools for your project, please add jcenter repository to your build definition, then add the following dependency to your project's build definition (pom.xml):
10
Maven:
11
```xml
12
<repositories>
13
    <repository>
14
        <id>jcenter</id>
15
        <name>bintray</name>
16
        <url>http://jcenter.bintray.com</url>
17
    </repository>
18
</repositories>
19
​
20
<dependencies>
21
    <dependency>
22
        <groupId>com.midtrans</groupId>
23
        <artifactId>java-library</artifactId>
24
        <version>1.1.0</version>
25
</dependency>
26
</dependencies>
27
```
28
Gradle:
29
If you're using Gradle as the build tools for your project, please add jcenter repository to your build script then add the following dependency to your project's build definition (build.gradle):
30
```Gradle
31
repositories {
32
    maven {
33
        url  "http://jcenter.bintray.com" 
34
    }
35
}
36
​
37
dependencies {
38
    compile 'com.midtrans:java-library:1.1.0'
39
}
40
```
41
​
42
### 1.b Using JAR File
43
​
@Xaxxis
Commit changes
Commit summary
Update README.md
Optional extended description
Add an optional extended description…

Choose which email address to associate with this commit

 Commit directly to the master branch.
 Create a new branch for this commit and start a pull request. Learn more about pull requests.
 
© 2019 GitHub, Inc.
Terms
Privacy
Security
Status
Help
Contact GitHub
Pricing
API
Training
Blog
About
