plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "dev.oreo"
version = "0.1.0"

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
        untilBuild.set("243.*")
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

    // Set these in CI to publish to the JetBrains Marketplace.
    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN") ?: "")
    }
}
