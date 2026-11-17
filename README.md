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

## Installation

You don't need the source — install the pre-built plugin:

### Option A — from a GitHub release (available now)

1. Go to [**Releases**](https://github.com/OW-Programing-Language/ow-intellij/releases)
   and download the latest `ow-intellij-<version>.zip`.
2. In your JetBrains IDE: **Settings/Preferences → Plugins → ⚙ (gear icon) →
   Install Plugin from Disk…**
3. Select the downloaded `.zip` and click **OK**, then **Restart** the IDE.

### Option B — from the JetBrains Marketplace

Once published: **Settings → Plugins → Marketplace**, search **"Ow Language"**,
click **Install**. (Marketplace publishing is a one-time, account-gated step.)

> Works in IntelliJ IDEA (Community or Ultimate) and other JetBrains IDEs
> (PyCharm, WebStorm, etc.) on build 2023.3 and newer.

## Usage

1. Open or create a file with the **`.ow`** extension — it gets the Ow icon and
   full syntax highlighting automatically.
2. To run it, **right-click in the editor → “Run .ow File”** (also in the Run
   context menu). Output appears in a dialog.
   - The Ow runtime is **bundled**, so this works immediately — no Ow or Java to
     install. A long-running `serve:` web app blocks until it exits; run those
     from a terminal instead.

```ow
# hello.ow
main:
  set sprites: { "Blaze": "hostile/blaze.gif", "Magma Cube": "hostile/magmacube.png" }
  print: map_get(sprites, "Magma Cube", "hostile/zombie.png")
```

Right-click → **Run .ow File** prints `hostile/magmacube.png`.

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

## Publishing to the JetBrains Marketplace

CI publishes automatically on every version tag (`v*`) via the
`publish-marketplace` job — **but only once a `PUBLISH_TOKEN` secret is set**
(otherwise the publish step is skipped and tags still produce a GitHub release).

One-time setup:

1. **Create a vendor account** at <https://plugins.jetbrains.com> and sign in.
2. **First upload (manual, required once):** build the zip locally
   (`./gradlew buildPlugin` → `build/distributions/ow-intellij-<version>.zip`)
   and upload it at <https://plugins.jetbrains.com/plugin/add>. JetBrains
   **moderates the first submission** (usually 1–2 business days) before the
   plugin is publicly listed. This also registers the plugin id `dev.oreo.ow`.
3. **Create a permanent token** at
   <https://plugins.jetbrains.com/author/me/tokens>.
4. In the repo: **Settings → Secrets and variables → Actions → New repository
   secret**, name it **`PUBLISH_TOKEN`**, paste the token.

After that, every `vX.Y.Z` tag builds the plugin and publishes the new version to
the Marketplace automatically (`./gradlew publishPlugin`). Users then install it
from **Settings → Plugins → Marketplace** (search “Ow Language”) with automatic
updates.

To publish locally instead: `PUBLISH_TOKEN=<token> ./gradlew publishPlugin`.

## License

[MIT](LICENSE).
