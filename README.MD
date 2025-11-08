<div align="center">

# 🚀 ReleaseChecker

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://openjdk.java.net/)
[![Maven Central](https://img.shields.io/badge/Maven-Central-red.svg)](https://repo.neziw.ovh/releases)

</div>

> Simple, lightweight, and modern Java library for checking GitHub releases via GitHub API

---

## 📋 Table of Contents

- [Features](#-features)
- [Requirements](#-requirements)
- [Installation](#-installation)
- [Quick Start](#-quick-start)
- [Configuration](#-configuration)
- [Usage Examples](#-usage-examples)
- [API Reference](#-api-reference)
- [Best Practices](#-best-practices)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)
- [License](#-license)

---

## ✨ Features

- 🔍 **Latest Release Check** - Get the latest release information from any GitHub repository
- 📊 **Version Comparison** - Check if a newer version is available and how many versions behind you are
- 📦 **Full Release History** - Retrieve all available releases with metadata
- 🔐 **GitHub Token Support** - Optional authentication for private repositories and higher rate limits
- 🚀 **Lightweight** - Minimal dependencies, only requires Gson
- 🎯 **Simple API** - Easy-to-use builder pattern for quick setup
- 🛡️ **Error Handling** - Comprehensive exception handling with detailed error messages
- 📝 **Type-Safe** - Uses Java records for type-safe data structures

---

## 📦 Requirements

- **Java**: 17 or higher

- **Maven** or **Gradle**: For dependency management

- **GitHub Repository**: Public repository or valid GitHub token for private repositories

---

## 🔧 Installation

### Maven

Add the repository and dependency to your `pom.xml`:

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

### Gradle

Add the repository and dependency to your `build.gradle.kts`:

```kotlin
repositories {
    maven {
        name = "neziw-repo"
        url = uri("https://repo.neziw.ovh/releases")
    }
}

dependencies {
    implementation("ovh.neziw:ReleaseChecker:1.0.2")
}
```

Or in Groovy (`build.gradle`):

```groovy
repositories {
    maven {
        name "neziw-repo"
        url "https://repo.neziw.ovh/releases"
    }
}

dependencies {
    implementation "ovh.neziw:ReleaseChecker:1.0.2"
}
```

---

## 🚀 Quick Start

### Basic Example

```java
import ovh.neziw.checker.ReleaseCheck;
import ovh.neziw.checker.ReleaseCheckBuilder;

// Create a ReleaseCheck instance for a repository
ReleaseCheck releaseCheck = ReleaseCheckBuilder.builder()
    .setRepositoryOwner("nats-io")
    .setRepositoryName("nats.java")
    .build();

// Get the latest release
ReleaseData latestRelease = releaseCheck.getLatestRelease();
System.out.println("Latest version: " + latestRelease.tagName());

// Check if a newer version is available
boolean isNewerAvailable = releaseCheck.isNewerVersionAvailable("2.20.1");
System.out.println("Is newer version available: " + isNewerAvailable);

// Get how many versions behind you are
int behindCount = releaseCheck.getBehindCount("2.20.1");
if (behindCount == -1) {
    System.out.println("Tag not found");
} else if (behindCount == 0) {
    System.out.println("You are using the latest version");
} else {
    System.out.println("You are " + behindCount + " versions behind");
}
```

**Output:**
```
Latest version: 2.20.2
Is newer version available: true
You are 1 versions behind
```

---

## ⚙️ Configuration

### Builder Pattern

The library uses a builder pattern for configuration:

```java
ReleaseCheck releaseCheck = ReleaseCheckBuilder.builder()
    .setRepositoryOwner("owner")      // Required: GitHub repository owner
    .setRepositoryName("repository")  // Required: GitHub repository name
    .setGithubToken("token")          // Optional: GitHub token for private repos
    .build();
```

### Configuration Options

| Method | Required | Description |
|--------|----------|-------------|
| `setRepositoryOwner(String)` | ✅ Yes | GitHub repository owner (username or organization) |
| `setRepositoryName(String)` | ✅ Yes | GitHub repository name |
| `setGithubToken(String)` | ❌ No | GitHub personal access token (for private repos or higher rate limits) |

---

## 📖 Usage Examples

### Example 1: Check Latest Release

```java
ReleaseCheck releaseCheck = ReleaseCheckBuilder.builder()
    .setRepositoryOwner("spring-projects")
    .setRepositoryName("spring-boot")
    .build();

try {
    ReleaseData latest = releaseCheck.getLatestRelease();
    System.out.println("Latest Release: " + latest.name());
    System.out.println("Tag: " + latest.tagName());
    System.out.println("Published: " + latest.publishedAt());
    System.out.println("URL: " + latest.htmlUrl());
} catch (IOException e) {
    System.err.println("Error fetching release: " + e.getMessage());
}
```

### Example 2: Check if Update is Available

```java
ReleaseCheck releaseCheck = ReleaseCheckBuilder.builder()
    .setRepositoryOwner("my-org")
    .setRepositoryName("my-app")
    .build();

String currentVersion = "1.2.3";

try {
    if (releaseCheck.isNewerVersionAvailable(currentVersion)) {
        ReleaseData latest = releaseCheck.getLatestRelease();
        System.out.println("Update available! Latest version: " + latest.tagName());
        System.out.println("Download: " + latest.htmlUrl());
    } else {
        System.out.println("You are using the latest version");
    }
} catch (IOException e) {
    System.err.println("Error checking version: " + e.getMessage());
}
```

### Example 3: Get All Releases

```java
ReleaseCheck releaseCheck = ReleaseCheckBuilder.builder()
    .setRepositoryOwner("apache")
    .setRepositoryName("commons-lang")
    .build();

try {
    List<ReleaseData> releases = releaseCheck.getReleaseDataList();
    System.out.println("Total releases: " + releases.size());
    
    for (ReleaseData release : releases) {
        System.out.println(release.tagName() + " - " + release.publishedAt());
    }
} catch (IOException e) {
    System.err.println("Error fetching releases: " + e.getMessage());
}
```

### Example 4: Check Versions Behind

```java
ReleaseCheck releaseCheck = ReleaseCheckBuilder.builder()
    .setRepositoryOwner("gradle")
    .setRepositoryName("gradle")
    .build();

String myVersion = "8.0.0";

try {
    int behindCount = releaseCheck.getBehindCount(myVersion);
    
    if (behindCount == -1) {
        System.out.println("Version " + myVersion + " not found");
    } else if (behindCount == 0) {
        System.out.println("You are using the latest version!");
    } else {
        System.out.println("You are " + behindCount + " versions behind the latest");
        
        List<ReleaseData> releases = releaseCheck.getReleaseDataList();
        System.out.println("Available updates:");
        for (int i = 0; i < behindCount && i < releases.size(); i++) {
            System.out.println("  - " + releases.get(i).tagName());
        }
    }
} catch (IOException e) {
    System.err.println("Error checking versions: " + e.getMessage());
}
```

### Example 5: Using GitHub Token for Private Repositories

```java
String githubToken = System.getenv("GITHUB_TOKEN");

ReleaseCheck releaseCheck = ReleaseCheckBuilder.builder()
    .setRepositoryOwner("my-org")
    .setRepositoryName("private-repo")
    .setGithubToken(githubToken)
    .build();

try {
    ReleaseData latest = releaseCheck.getLatestRelease();
    System.out.println("Latest release: " + latest.tagName());
} catch (IOException e) {
    System.err.println("Error: " + e.getMessage());
}
```

### Example 6: Get Repository Information

```java
ReleaseCheck releaseCheck = ReleaseCheckBuilder.builder()
    .setRepositoryOwner("google")
    .setRepositoryName("guava")
    .build();

try {
    RepositoryData repo = releaseCheck.getRepositoryData();
    System.out.println("Repository: " + repo.fullName());
    System.out.println("Description: " + repo.description());
    System.out.println("URL: " + repo.htmlUrl());
    System.out.println("Private: " + repo.isPrivate());
} catch (IOException e) {
    System.err.println("Error fetching repository data: " + e.getMessage());
}
```

---

## 🔍 API Reference

### ReleaseCheckBuilder

#### Methods

- `static ReleaseCheckBuilder builder()` - Creates a new builder instance
- `ReleaseCheckBuilder setRepositoryOwner(String owner)` - Sets the repository owner
- `ReleaseCheckBuilder setRepositoryName(String name)` - Sets the repository name
- `ReleaseCheckBuilder setGithubToken(String token)` - Sets the GitHub token (optional)
- `ReleaseCheck build()` - Builds the `ReleaseCheck` instance

### ReleaseCheck

#### Methods

- `ReleaseData getLatestRelease() throws IOException` - Gets the latest release
- `List<ReleaseData> getReleaseDataList() throws IOException` - Gets all releases (sorted by published date, newest first)
- `boolean isNewerVersionAvailable(String version) throws IOException` - Checks if a newer version is available
- `int getBehindCount(String tagName) throws IOException` - Returns how many versions behind the given tag is (returns -1 if tag not found, 0 if latest)
- `RepositoryData getRepositoryData() throws IOException` - Gets repository information

### ReleaseData

Record containing release information:

- `int id()` - Release ID
- `String htmlUrl()` - HTML URL of the release
- `String tagName()` - Tag name (version)
- `String targetCommitish()` - Target commit/branch
- `String name()` - Release name
- `boolean draft()` - Whether the release is a draft
- `boolean prerelease()` - Whether the release is a pre-release
- `ZonedDateTime createdAt()` - Creation timestamp
- `ZonedDateTime publishedAt()` - Publication timestamp

### RepositoryData

Record containing repository information:

- `int id()` - Repository ID
- `String name()` - Repository name
- `String fullName()` - Full repository name (owner/repo)
- `boolean isPrivate()` - Whether the repository is private
- `String htmlUrl()` - HTML URL of the repository
- `String description()` - Repository description
- `boolean fork()` - Whether the repository is a fork

---

## 💡 Best Practices

### 1. **Error Handling**

Always handle `IOException` when calling API methods:

```java
try {
    ReleaseData latest = releaseCheck.getLatestRelease();
    // Use the release data
} catch (IOException e) {
    // Handle network errors, API errors, etc.
    logger.error("Failed to fetch release", e);
}
```

### 2. **GitHub Token Management**

Never hardcode tokens in your source code. Use environment variables or configuration files:

```java
// Use environment variable
String token = System.getenv("GITHUB_TOKEN");

// Or use a configuration file (not committed to VCS)
String token = config.getGitHubToken();

ReleaseCheck releaseCheck = ReleaseCheckBuilder.builder()
    .setRepositoryOwner("owner")
    .setRepositoryName("repo")
    .setGithubToken(token)
    .build();
```

### 3. **Caching Release Data**

The library caches release data internally. If you need fresh data, create a new `ReleaseCheck` instance:

```java
// Data is cached per instance
ReleaseCheck releaseCheck = ReleaseCheckBuilder.builder()
    .setRepositoryOwner("owner")
    .setRepositoryName("repo")
    .build();

// First call fetches from API
ReleaseData latest1 = releaseCheck.getLatestRelease();

// Second call uses cached data
ReleaseData latest2 = releaseCheck.getLatestRelease();
```

### 4. **Version Comparison**

The `isNewerVersionAvailable` method performs semantic version comparison. Make sure your version strings are in the correct format:

```java
// Good: Semantic versioning
releaseCheck.isNewerVersionAvailable("1.2.3");
releaseCheck.isNewerVersionAvailable("2.0.0");

// The method extracts numbers and compares them
// "v1.2.3" -> "1.2.3" (v prefix is stripped)
```

### 5. **Rate Limiting**

GitHub API has rate limits. For unauthenticated requests, the limit is 60 requests per hour. Using a GitHub token increases this to 5000 requests per hour:

```java
// Without token: 60 requests/hour
ReleaseCheck releaseCheck = ReleaseCheckBuilder.builder()
    .setRepositoryOwner("owner")
    .setRepositoryName("repo")
    .build();

// With token: 5000 requests/hour
ReleaseCheck releaseCheck = ReleaseCheckBuilder.builder()
    .setRepositoryOwner("owner")
    .setRepositoryName("repo")
    .setGithubToken(token)
    .build();
```

---

## 🐛 Troubleshooting

### 1. Connection Issues

**Problem**: `IOException` when fetching data

**Solutions**:

- Check your internet connection
- Verify the repository owner and name are correct
- Ensure the repository is accessible (public or you have a valid token)
- Check if GitHub API is available: `https://api.github.com`

### 2. Authentication Failures

**Problem**: 401 Unauthorized or 403 Forbidden errors

**Solutions**:

- Verify your GitHub token is valid
- Check if the token has the required permissions (for private repos, use `repo` scope)
- Ensure the repository exists and you have access to it

### 3. Rate Limiting

**Problem**: 403 Forbidden with rate limit message

**Solutions**:

- Use a GitHub token to increase rate limits
- Implement request caching in your application
- Reduce the frequency of API calls

### 4. Tag Not Found

**Problem**: `getBehindCount()` returns -1

**Solutions**:

- Verify the tag name is correct (case-sensitive)
- Check if the tag exists in the repository
- Ensure the tag is associated with a release (not just a Git tag)

### 5. Version Comparison Issues

**Problem**: `isNewerVersionAvailable()` returns incorrect results

**Solutions**:

- Ensure version strings follow semantic versioning (e.g., "1.2.3")
- The method extracts numbers and compares them, so "v1.2.3" works but unusual formats may not
- Check the tag name format in the repository

---

## 🔧 Advanced Usage

### Custom Error Handling

```java
public class ReleaseCheckerService {
    private final ReleaseCheck releaseCheck;
    
    public ReleaseCheckerService(String owner, String repo) {
        this.releaseCheck = ReleaseCheckBuilder.builder()
            .setRepositoryOwner(owner)
            .setRepositoryName(repo)
            .setGithubToken(System.getenv("GITHUB_TOKEN"))
            .build();
    }
    
    public Optional<ReleaseData> getLatestReleaseSafe() {
        try {
            return Optional.of(releaseCheck.getLatestRelease());
        } catch (IOException e) {
            logger.error("Failed to fetch latest release", e);
            return Optional.empty();
        }
    }
    
    public boolean checkForUpdates(String currentVersion) {
        try {
            return releaseCheck.isNewerVersionAvailable(currentVersion);
        } catch (IOException e) {
            logger.error("Failed to check for updates", e);
            return false;
        }
    }
}
```

### Integration with Application Startup

```java
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        // Check for updates on startup
        checkForUpdates();
        
        SpringApplication.run(MyApplication.class, args);
    }
    
    private static void checkForUpdates() {
        ReleaseCheck releaseCheck = ReleaseCheckBuilder.builder()
            .setRepositoryOwner("my-org")
            .setRepositoryName("my-app")
            .build();
        
        try {
            String currentVersion = "1.0.0";
            if (releaseCheck.isNewerVersionAvailable(currentVersion)) {
                ReleaseData latest = releaseCheck.getLatestRelease();
                logger.info("New version available: {} (current: {})", 
                    latest.tagName(), currentVersion);
            }
        } catch (IOException e) {
            logger.warn("Could not check for updates", e);
        }
    }
}
```

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository

2. Create your feature branch (`git checkout -b feature/amazing-feature`)

3. Commit your changes (`git commit -m 'Add some amazing feature'`)

4. Push to the branch (`git push origin feature/amazing-feature`)

5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- Built with [Gson](https://github.com/google/gson) for JSON parsing

- Uses [GitHub REST API](https://docs.github.com/en/rest) for fetching release data

- Inspired by the need for simple release checking in Java applications

---

## 📞 Support

If you encounter any issues or have questions, please open an issue on the GitHub repository.

---

**Made with ❤️ by [neziw](https://github.com/neziw)**