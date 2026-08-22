
[Java Development Kit 21]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[Gradle]: https://www.gradle.org/

[discord-shield]: https://dcbadge.limes.pink/api/server/https://discord.gg/BbH6kv6
[discord-url]: https://discord.gg/BbH6kv6

[image-url]: https://i.imgur.com/YP28cx6.png

# Bittermelon #

![image-url]

## Contributing ##

### Prerequisites ###
* [Java Development Kit 25]
* [Gradle]



### 1) Clone The Repository ###
Follow the steps here: https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository

### 2) Setup in [IntelliJ IDEA] ###
1. Open the template's root folder as a new project in IDEA. This is the folder that contains this README.md file and the gradlew executable.
2. If your default JVM/JDK is not Java 21 you will encounter an error when opening the project. This error is fixed by going to `File > Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JVM` and changing the value to a valid Java 21 JVM. You will also need to set the Project SDK to Java 21. This can be done by going to `File > Project Structure > Project SDK`. Once both have been set open the Gradle tab in IDEA and click the refresh button to reload the project.
3. Open your Run/Debug Configurations. Under the `Application` category there should now be options to run Fabric and NeoForge projects. Select one of the client options and try to run it.
4. Assuming you were able to run the game in step 3 your workspace should now be set up.

__**Eclipse/VSCode not supported**__

### 3) Creating a PR ###
Follow the steps here: https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request



