# Ow Language — IntelliJ IDEA plugin

[![Build plugin](https://github.com/OW-Programing-Language/ow-intellij/actions/workflows/build.yml/badge.svg)](https://github.com/OW-Programing-Language/ow-intellij/actions/workflows/build.yml)

Language support for the [Ow programming language](https://github.com/OW-Programing-Language/ow-lang) in IntelliJ IDEA (and other JetBrains IDEs):

- **`.ow` file type** with the Ow icon
- **Syntax highlighting** — keywords (incl. the module system), **built-in functions**
  (string/math/JSON/UI/audio and the **dictionary/map** builtins `map_get`,
  `map_has`, `map_set`, `map_delete`, `map_keys`, `map_values`, `map_size`),
  strings, numbers (including floats), operators and comments
- **Run .ow File** action — runs the current file (right‑click or the Run context menu)
- **Bundled Ow runtime** — the Ow compiler/runtime ships inside the plugin, so Run works out of the box with no separate install

![logo](logo.png)

## Requirements

**None for running** — the Ow runtime (`OwIDE.jar`) is bundled with the plugin and
executed with the IDE's own Java (JetBrains Runtime), so installing the plugin is
enough. The **Run** action does `ow run <file>` using the bundled runtime.

If you already have the Ow CLI on your PATH (see
[ow-lang / INSTALL.md](https://github.com/OW-Programing-Language/ow-lang/blob/main/INSTALL.md)),
that install is used instead. The bundled runtime tracks a pinned Ow release
(currently v1.2.3).

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
