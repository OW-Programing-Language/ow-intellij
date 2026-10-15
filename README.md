# Ow Language — IntelliJ IDEA plugin

[![Build plugin](https://github.com/OW-Programing-Language/ow-intellij/actions/workflows/build.yml/badge.svg)](https://github.com/OW-Programing-Language/ow-intellij/actions/workflows/build.yml)

Language support for the [Ow programming language](https://github.com/OW-Programing-Language/ow-lang) in IntelliJ IDEA (and other JetBrains IDEs):

- **`.ow` file type** with the Ow icon
- **Syntax highlighting** (keywords, strings, numbers, comments)
- **Run .ow File** action — runs the current file with the installed `ow` command (right‑click or the Run context menu)

![logo](logo.png)

## Requirements

Install the Ow toolchain and put `ow` on your PATH — see
[ow-lang / INSTALL.md](https://github.com/OW-Programing-Language/ow-lang/blob/main/INSTALL.md).
The **Run** action calls `ow run <file>`.

## Build

Requires JDK 17+. The Gradle wrapper fetches everything else (including the
IntelliJ Platform SDK on first run).

```sh
./gradlew buildPlugin      # produces build/distributions/ow-intellij-<version>.zip
./gradlew runIde           # launches a sandbox IDE with the plugin installed
```

Install the built zip via **Settings → Plugins → ⚙ → Install Plugin from Disk…**

## Publishing

CI can publish to the JetBrains Marketplace with `./gradlew publishPlugin`
(set the `PUBLISH_TOKEN` environment variable). Marketplace publishing is a
manual, account-gated step — it is not done automatically.

## License

[MIT](LICENSE).
