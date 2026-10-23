# Host integration

Keep the original `jellyfish-drift` registration and Android package. Version 3 restores classic rendering. The filmed app is a separate `jellyfish-drift-v2` registration and package; installation must not migrate or delete either app's data.

| Field | Classic value |
| --- | --- |
| Repository | `jeremykenedy/jellyfish-drift` |
| Saver ID | `jellyfish-drift` |
| Package | `com.jeremykenedy.jellyfishdrift` |
| Dream | `com.jeremykenedy.jellyfishdrift/.JellyfishDreamService` |
| Settings | `com.jeremykenedy.jellyfishdrift/.SettingsActivity` |
| Preview | `com.jeremykenedy.jellyfishdrift/.PreviewActivity` |
| Assets | `jellyfish-drift.apk`, `jellyfish-drift.apk.sha256` |
| Provider | `com.jeremykenedy.jellyfishdrift.settings` |
| Restoration version | 3.0.0, Android code 7 |

All six original schema rows are effective: background, density, motion, species, light_rays, randomize_all. Use current classic captures for its preview, keeping V2 footage under the new entry. Existing v2.0.0 tags remain immutable, but must not be pinned as the classic restoration. Verify actual public release bytes, checksum, package, and original signer before updating host metadata.

The optional `show_clock` boolean defaults to false and has `randomAllowed=false`. It is independent of scene randomization and must not be overwritten by a host randomize-all operation. The settings provider exposes seven rows, retaining the six original keys.
