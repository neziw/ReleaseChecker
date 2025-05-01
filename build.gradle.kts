import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("checkstyle")
    id("co.uzzu.dotenv.gradle") version "4.0.0"
}

group = "ovh.neziw"
version = "1.0.2"

tasks.withType<JavaCompile> {
    options.compilerArgs = listOf("-Xlint:deprecation")
    options.encoding = "UTF-8"
}

tasks.withType<ShadowJar> {
    archiveFileName.set("${project.name} ${project.version}.jar")
    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**",
        "org/checkerframework/**",
        "META-INF/**",
        "javax/**"
    )
    mergeServiceFiles()
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

checkstyle {
    toolVersion = "10.23.0"
    maxWarnings = 0
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("com.google.code.gson:gson:2.13.1")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

publishing {
    publications {
        val publication = create<MavenPublication>("maven") {
            groupId = "${project.group}"
            artifactId = project.name
            version = "${project.version}"
        }
        project.shadow.component(publication)
    }
    repositories {
        maven {
            name = "neziw-repo"
            url = uri("https://repo.neziw.ovh/releases/")

            credentials {
                val usernameKey = "MVN_USER"
                val passwordKey = "MVN_PASS"
                username = if (env.isPresent(usernameKey)) env.fetch(usernameKey) else System.getenv(usernameKey)
                password = if (env.isPresent(passwordKey)) env.fetch(passwordKey) else System.getenv(passwordKey)
            }
        }
    }
}