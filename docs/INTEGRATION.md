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
| Release APK name | `jellyfish-drift.apk` |
| Release checksum name | `jellyfish-drift.apk.sha256` |

The 1.1 renderer preserves the 1.0 package, signing key, six preference keys and accepted values. An ordinary signed update preserves preferences. Host catalogs must pin an actual published release and verify its downloaded APK against the published checksum; a source branch or candidate is not a release.

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
