# ReleaseChecker

Easy and lightweight GitHub release parser written in Java using Gson and HTTPS Requests.

### Example Usage:
```java
// https://github.com/nats-io/nats.java
final ReleaseCheck releaseCheck = ReleaseCheckBuilder.builder()
    .setRepositoryOwner("nats-io")
    .setRepositoryName("nats.java")
    .build();

System.out.println("Latest version: " + releaseCheck.getLatestRelease().tagName());
System.out.println("Is a newer version available: " + releaseCheck.isNewerVersionAvailable("2.20.1"));

//You can also get all available releases using 'ReleaseCheck#getReleaseDataList()'

final int behindCount = releaseCheck.getBehindCount("2.20.1");

if (behindCount == -1) {
    System.out.println("There is no such tag.");
} else if (behindCount == 0) {
    System.out.println("You are using the latest version.");
} else {
    System.out.println("Your version is " + behindCount + " versions behind the latest.");
}
```

**Output** (since 19/10/2024):
```
Latest version: 2.20.2
Is a newer version available: true
Your version is 1 versions behind the latest.
```

### Adding dependency

**Maven:**
```xml
<repositories>
    <repository>
        <id>neziw-repo</id>
        <url>https://repo.neziw.ovh/releases</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>ovh.neziw</groupId>
        <artifactId>ReleaseChecker</artifactId>
        <version>1.0.2</version>
    </dependency>
</dependencies>
```

**Gradle:**
```groovy
repositories {
    maven {
        name "neziw-repo"
        url "https://repo.neziw.ovh/releases"
    }
}

implementation "ovh.neziw:ReleaseChecker:1.0.2"
```

---
Special thanks to [JetBrains](https://www.jetbrains.com/products/) company for providing development tools used to develop this project. 

[<img src="https://user-images.githubusercontent.com/65517973/210912946-447a6b9a-2685-4796-9482-a44bffc727ce.png" alt="JetBrains" width="150">](https://www.jetbrains.com)
