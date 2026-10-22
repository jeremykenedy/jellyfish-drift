# Host integration

Jellyfish Drift exposes native Android activities, an Android DreamService and a provider for its six visual preferences. It has no network permission or runtime download step.

| Contract | Value |
| --- | --- |
| Package | `com.jeremykenedy.jellyfishdrift` |
| Settings | `com.jeremykenedy.jellyfishdrift/.SettingsActivity` |
| Preview | `com.jeremykenedy.jellyfishdrift/.PreviewActivity` |
| Dream | `com.jeremykenedy.jellyfishdrift/.JellyfishDreamService` |
| Schema | `content://com.jeremykenedy.jellyfishdrift.settings/schema` |
| Preferences | `content://com.jeremykenedy.jellyfishdrift.settings/settings` |
| Candidate version | `1.1.0`, version code `2` |
| Production certificate SHA-256 | `eba70847a8e5fc55507fdf86d18bfe36ed9ae753e1e0e2ae4807676722ef949e` |
| Candidate APK SHA-256 | `e804871a9393ec8e564c1e8c2458a904841020debdd0e084e0b8d90134ee2123` |
| Candidate APK bytes | 1,271,121 |
| Release APK name | `jellyfish-drift.apk` |
| Release checksum name | `jellyfish-drift.apk.sha256` |

The 1.1 renderer preserves the 1.0 package, signing key, six preference keys and accepted values. An ordinary signed update preserves preferences. Host catalogs must pin an actual published release and verify its downloaded APK against the published checksum; a source branch or candidate is not a release.

The intended 1.1 assets are [the signed APK](https://github.com/jeremykenedy/jellyfish-drift/releases/download/v1.1.0/jellyfish-drift.apk) and [its checksum](https://github.com/jeremykenedy/jellyfish-drift/releases/download/v1.1.0/jellyfish-drift.apk.sha256). They are unavailable until the physical gate, primary-branch checks and publication finish. The c065 APK passed native and physical functional routes but failed the physical 25-nettle performance gate at 6.53 presents/second; it must not be pinned as a published release. The new e804 indexed-buffer candidate is unverified on devices and must not be treated as installable release metadata yet. Older deleted candidate binaries are not claimed byte-identical.

## Preferences

| Key | Accepted values |
| --- | --- |
| `background` | `dark`, `light`, `random` |
| `density` | `few`, `handful`, `many`, `ton`, `schools`, `random` |
| `motion` | `slow`, `normal`, `fast`, `random` |
| `species` | `moon`, `sea_nettle`, `purple_stripe`, `random` |
| `light_rays` | `off`, `on`, `random` |
| `randomize_all` | `false`, `true` |

Query the schema for labels and defaults. Update preferences with `ContentResolver.update()` using string `key` and `value` fields. Invalid values fail closed. This provider exposes only local visual settings. See [configuration](CONFIGURATION.md) and [verification](VERIFICATION.md) for behavior and current target-platform evidence.
