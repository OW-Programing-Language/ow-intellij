plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "dev.oreo"
version = "0.1.2"

repositories {
    mavenCentral()
}

// See https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.3.6")
    type.set("IC") // IntelliJ IDEA Community
    plugins.set(listOf())
}

tasks {
    patchPluginXml {
        sinceBuild.set("233")
        // No upper bound: the plugin only uses stable platform APIs, so it keeps
        // working on newer IDEs (2024.x, 2025.x, 2026.x, …).
        untilBuild.set(provider { null })
    }

    compileKotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    // Binary-compatibility check against a range of real IDE builds.
    runPluginVerifier {
        ideVersions.set(listOf("IC-2023.3.6", "IC-2024.1.7", "IC-2024.3.5", "IC-2025.1"))
    }

    // Set these in CI to publish to the JetBrains Marketplace.
    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN") ?: "")
    }
}
