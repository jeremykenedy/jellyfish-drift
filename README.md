<p align="center">
    <picture>
        <source media="(prefers-color-scheme: dark)" srcset="art/banner-dark.svg">
        <source media="(prefers-color-scheme: light)" srcset="art/banner-light.svg">
        <img src="art/banner-light.svg" alt="Jellyfish Drift, animated TV screensaver with no ads, analytics, or tracking" width="800">
    </picture>
</p>

<p align="center">A continuously animated jellyfish screensaver for Fire TV, Android TV, and Google TV.</p>

<p align="center">
    <a href="https://github.com/jeremykenedy/jellyfish-drift/releases"><img src="https://img.shields.io/github/v/release/jeremykenedy/jellyfish-drift?label=latest%20release" alt="Latest release"></a>
    <a href="https://github.com/jeremykenedy/jellyfish-drift/releases"><img src="https://img.shields.io/github/downloads/jeremykenedy/jellyfish-drift/total" alt="GitHub release downloads"></a>
    <a href="https://github.com/jeremykenedy/jellyfish-drift/actions/workflows/ci.yml"><img src="https://github.com/jeremykenedy/jellyfish-drift/actions/workflows/ci.yml/badge.svg" alt="CI"></a>
    <a href="https://github.com/jeremykenedy/jellyfish-drift/actions/workflows/style.yml"><img src="https://github.com/jeremykenedy/jellyfish-drift/actions/workflows/style.yml/badge.svg" alt="Code style"></a>
    <a href="https://github.com/jeremykenedy/jellyfish-drift/actions/workflows/docs.yml"><img src="https://github.com/jeremykenedy/jellyfish-drift/actions/workflows/docs.yml/badge.svg" alt="Documentation"></a>
    <a href="https://github.com/jeremykenedy/jellyfish-drift/actions/workflows/security.yml"><img src="https://github.com/jeremykenedy/jellyfish-drift/actions/workflows/security.yml/badge.svg" alt="Security checks"></a>
    <a href="LICENSE"><img src="https://img.shields.io/badge/License-Apache--2.0-blue.svg" alt="Apache-2.0 license"></a>
    <a href="https://github.com/jeremykenedy"><img src="https://img.shields.io/github/followers/jeremykenedy?label=Follow&style=social" alt="Follow Jeremy Kenedy on GitHub"></a>
    <a href="https://github.com/jeremykenedy/jellyfish-drift" title="Open the repository and click Star"><img src="https://img.shields.io/badge/Star-this%20repo-yellow?logo=github&style=social" alt="Star this repo"></a>
    <a href="https://github.com/jeremykenedy/jellyfish-drift/stargazers"><img src="https://img.shields.io/github/stars/jeremykenedy/jellyfish-drift?style=social" alt="Star Jellyfish Drift on GitHub"></a>
    <a href="https://github.com/sponsors/jeremykenedy"><img src="https://img.shields.io/badge/Sponsor-jeremykenedy-EA4AAA?logo=githubsponsors&logoColor=white" alt="Sponsor Jeremy Kenedy"></a>
</p>

<p align="center">Show some love by starring this repository on GitHub.</p>

## Table of contents

- [Privacy](#privacy)
- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Configuration](#configuration)
- [Screenshots](#screenshots)
- [Building and testing](#building-and-testing)
- [Documentation](#documentation)
- [License](#license)

## Privacy

The application requests no network permission and includes no advertising, analytics, telemetry, crash reporting, or tracking code. It does not make network requests.

## Features

- Animated jellyfish with translucent bells, pulsing motion, trailing tentacles, and drifting particles.
- Deep-water and bright open-water backgrounds.
- Jellyfish species, density, movement speed, and top-down light shimmer controls.
- Per-setting random selection and an option to randomize all settings each time the screensaver starts.
- D-pad-friendly settings that can be discovered by screensaver host applications.
- Android DreamService integration for compatible TV devices.

## Requirements

- Android 6.0 (API 23) or later.
- A device that exposes Android's DreamService screensaver settings.
- Android SDK platform 36 and build-tools 36.0.0 to build from source.

Fire TV, Android TV, and Google TV behavior is listed in [device verification](docs/VERIFICATION.md) only after it has been tested on each target.

## Installation

Download the signed APK and matching SHA-256 file from [GitHub Releases](https://github.com/jeremykenedy/jellyfish-drift/releases). Verify the checksum, install the APK, then select Jellyfish Drift in the device's Display or Ambient mode screensaver settings. The standalone installer and uninstaller are documented in [installation](docs/INSTALLATION.md).

## Configuration

Open Jellyfish Drift from the TV launcher to adjust its settings. The app stores preferences locally. See [configuration](docs/CONFIGURATION.md) for every option and its available values.

## Screenshots

<p align="center">
    <img src="docs/screenshots/jellyfish-preview.png" alt="Animated moon jellyfish drifting across deep water on an Android TV screen" width="49%">
    <img src="docs/screenshots/jellyfish-light-preview.png" alt="Animated moon jellyfish over light water with the sunlight option enabled on a Fire TV" width="49%">
</p>

The dark preview was captured from the running app on an Android TV emulator. The light preview and [settings screen](docs/screenshots/settings-fire-tv.png) were captured on a Fire TV. The Fire TV DreamService capture is included in [device verification](docs/VERIFICATION.md).

## Building and testing

```bash
bash build.sh
bash test.sh
bash scripts/test-python-coverage.sh
bash scripts/test-coverage.sh
```

The build uses the Android SDK and JDK without third-party runtime dependencies. See [building](docs/BUILDING.md), [testing](docs/TESTING.md), and [architecture](docs/ARCHITECTURE.md).

## Documentation

- [Installation, update, and removal](docs/INSTALLATION.md)
- [Configuration](docs/CONFIGURATION.md)
- [Building from source](docs/BUILDING.md)
- [Architecture](docs/ARCHITECTURE.md)
- [Testing and coverage](docs/TESTING.md)
- [Device verification](docs/VERIFICATION.md)
- [CI](docs/CI.md)
- [Troubleshooting](docs/TROUBLESHOOTING.md)
- [Release process](docs/RELEASING.md)

Show some love by starring this repository on GitHub: [Star Jellyfish Drift](https://github.com/jeremykenedy/jellyfish-drift/stargazers).

## License

Jellyfish Drift is licensed under the [Apache License, Version 2.0](LICENSE). See [NOTICE](NOTICE) for project notices.
