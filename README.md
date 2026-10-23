<p align="center">
  <a href="https://github.com/jeremykenedy/jellyfish-drift#gh-light-mode-only"><picture><source media="(max-width: 600px) and (prefers-reduced-motion: reduce)" srcset="art/banner-mobile-still-light.06fdb2435437.svg"><source media="(prefers-reduced-motion: reduce)" srcset="art/banner-still-light.180e4f37264f.svg"><source media="(max-width: 600px)" srcset="art/banner-mobile-light.3c05d0ee479f.svg"><img src="art/banner-light.ed9fad3d48c0.svg#gh-light-mode-only" alt="Jellyfish Drift" width="800"></picture></a>
  <a href="https://github.com/jeremykenedy/jellyfish-drift#gh-dark-mode-only"><picture><source media="(max-width: 600px) and (prefers-reduced-motion: reduce)" srcset="art/banner-mobile-still-dark.657615bff5d9.svg"><source media="(prefers-reduced-motion: reduce)" srcset="art/banner-still-dark.f91bb9482be9.svg"><source media="(max-width: 600px)" srcset="art/banner-mobile-dark.3d5bdbb5f396.svg"><img src="art/banner-dark.207dad6e5b97.svg#gh-dark-mode-only" alt="Jellyfish Drift" width="800"></picture></a>
</p>

<p align="center">Real filmed jellyfish scenes for compatible TV screensavers, played entirely offline.</p>

<p align="center">
  <a href="https://github.com/jeremykenedy/jellyfish-drift/releases"><img src="https://img.shields.io/github/downloads/jeremykenedy/jellyfish-drift/total" alt="GitHub release downloads"></a>
  <a href="https://github.com/jeremykenedy/jellyfish-drift/releases"><img src="https://img.shields.io/github/v/release/jeremykenedy/jellyfish-drift?label=latest%20release" alt="Latest release"></a>
  <a href="https://github.com/jeremykenedy/jellyfish-drift/actions/workflows/ci.yml"><img src="https://github.com/jeremykenedy/jellyfish-drift/actions/workflows/ci.yml/badge.svg" alt="Build and tests"></a>
  <a href="https://github.com/jeremykenedy/jellyfish-drift/actions/workflows/style.yml"><img src="https://github.com/jeremykenedy/jellyfish-drift/actions/workflows/style.yml/badge.svg" alt="Code style"></a>
  <a href="https://github.com/jeremykenedy/jellyfish-drift/actions/workflows/docs.yml"><img src="https://github.com/jeremykenedy/jellyfish-drift/actions/workflows/docs.yml/badge.svg" alt="Documentation"></a>
  <a href="https://github.com/jeremykenedy/jellyfish-drift/actions/workflows/security.yml"><img src="https://github.com/jeremykenedy/jellyfish-drift/actions/workflows/security.yml/badge.svg" alt="Security checks"></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/License-Apache--2.0-blue.svg" alt="Application code: Apache-2.0"></a>
</p>
<p align="center">
  <a href="https://github.com/jeremykenedy"><img src="https://img.shields.io/github/followers/jeremykenedy?label=Follow&style=social" alt="Follow Jeremy Kenedy"></a>
  <a href="https://github.com/jeremykenedy/jellyfish-drift"><img src="https://img.shields.io/github/stars/jeremykenedy/jellyfish-drift?label=Star&style=social" alt="Star Jellyfish Drift"></a>
  <a href="https://github.com/sponsors/jeremykenedy"><img src="https://img.shields.io/badge/Sponsor-jeremykenedy-EA4AAA?logo=githubsponsors&logoColor=white" alt="Sponsor Jeremy Kenedy"></a>
</p>

**2.0 filmed candidate:** final physical production Dream and presentation checks are pending. The latest published release may use the previous renderer. See the [verification record](docs/VERIFICATION.md).

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

Playback uses bundled files, with no network permission, advertisements, analytics, or tracking. The optional command-line installer contacts GitHub to download published release files and verify their checksum.

## Features

| Feature | What it does | Details |
| --- | --- | --- |
| Real recordings | Blue aquarium Aurelia and Deep ocean Crossota scenes | [Scenes and limitations](docs/CONFIGURATION.md) |
| Playback controls | 0.9x, recorded speed, 1.25x, or random scene and speed | [Settings](docs/CONFIGURATION.md) |
| Offline playback | Silent 1080p H264 with a 720p fallback | [Architecture](docs/ARCHITECTURE.md) |
| Native TV settings | Remote controls, Preview, and offline licenses | [Current gallery](docs/SCREENSHOTS.md) |
| DreamService | The same scene player in Preview and the Android screensaver service | [Verified behavior](docs/VERIFICATION.md) |

The recordings repeat every 16 or approximately 19 seconds, with editorial dissolves. Animal count, species, lighting, and camera movement come from each recording. Legacy density, species, and light-ray values remain stored but do not affect filmed playback.

## Requirements

- Android 6.0 (API 23) or later on a compatible TV device.
- A system or host application that supports Android DreamService screensavers.
- ADB access from a computer when sideloading.

Physical Google TV support, sustained thermal behavior, and native 4K source detail are not established. See [platform evidence](docs/VERIFICATION.md).

## Installation

For the **published release**, connect the TV with ADB and run:

```bash
python3 install.py --serial TV_IP:5555
```

The installer verifies the downloaded APK against its release checksum and updates the existing package. It does not automatically select a screensaver. The unreleased 2.0 candidate requires a [local build](docs/BUILDING.md). See [installation and removal](docs/INSTALLATION.md).

## Configuration

Open Jellyfish Drift from the TV launcher to choose a recording or playback speed, watch Preview, or read Credits and licenses. Changes apply when the next view starts. [Configuration](docs/CONFIGURATION.md) documents all three effective controls and preservation of the six legacy raw preference values.

## Screenshots

<table>
<tr><td><img src="art/screenshots/blue-aquarium-native.png" alt="Actual 2.0 native Preview of filmed Aurelia in blue aquarium water" width="100%"></td><td><img src="art/screenshots/deep-ocean-native.png" alt="Actual 2.0 native Preview of filmed Crossota in deep ocean water" width="100%"></td><td><img src="art/screenshots/settings-native.png" alt="Actual native scene and playback speed settings" width="100%"></td></tr>
<tr><td>Blue aquarium</td><td>Deep ocean</td><td>Native settings</td></tr>
</table>

Current Android TV API 34 emulator captures of the signed 2.0 candidate. These are native Preview images, not physical Fire TV or 4K evidence. The [complete gallery](docs/SCREENSHOTS.md) includes credits, speed choices, and resumed playback.

## Building and testing

```bash
bash build.sh
bash test.sh
bash scripts/test-python-coverage.sh
bash scripts/test-coverage.sh
```

The build requires Android SDK platform 36, build-tools 36.0.0, JDK 17 or later, and the original protected signing key. It does not silently generate a replacement production key. See [building](docs/BUILDING.md) and [testing](docs/TESTING.md).

## Documentation

- [Configuration and migration](docs/CONFIGURATION.md)
- [Screenshots](docs/SCREENSHOTS.md)
- [Installation, update, and removal](docs/INSTALLATION.md)
- [Building from source](docs/BUILDING.md)
- [Architecture](docs/ARCHITECTURE.md)
- [Testing and coverage](docs/TESTING.md)
- [Device verification](docs/VERIFICATION.md)
- [CI](docs/CI.md)
- [Troubleshooting](docs/TROUBLESHOOTING.md)
- [Release process](docs/RELEASING.md)
- [Historical version 1.0.0 notes](docs/releases/v1.0.0.md)

## License

Application code is licensed under the [Apache License, Version 2.0](LICENSE). Filmed media uses separate source terms: Aurelia footage and derived images are CC BY-SA 4.0; the credited NOAA footage is public-domain material. See [media notices](assets/NOTICE.txt), the [CC BY-SA license](assets/CC-BY-SA-4.0.txt), and [project notices](NOTICE). The same notices are available offline in settings.
