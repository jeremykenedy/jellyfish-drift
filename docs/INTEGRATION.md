# Host integration

Version 2.0 uses filmed scenes. Hosts must verify the published release, checksum, bytes, and signing identity before replacing their pinned metadata.

| Field | Value |
| --- | --- |
| Saver ID | `jellyfish-drift` |
| Repository | `jeremykenedy/jellyfish-drift` |
| Package | `com.jeremykenedy.jellyfishdrift` |
| Dream | `com.jeremykenedy.jellyfishdrift/.JellyfishDreamService` |
| Native settings | `com.jeremykenedy.jellyfishdrift/.SettingsActivity` |
| Preview | `com.jeremykenedy.jellyfishdrift/.PreviewActivity` |
| Release files | `jellyfish-drift.apk`, `jellyfish-drift.apk.sha256` |
| Settings schema | `content://com.jeremykenedy.jellyfishdrift.settings/schema` |
| Saved values | `content://com.jeremykenedy.jellyfishdrift.settings/settings` |

The effective schema exposes scene, playback speed, and random scene/speed selection. The settings query retains six raw legacy values. Density, species, and light rays are preserved for migration but do not affect filmed playback. Read [configuration](CONFIGURATION.md) before updating a host editor; do not retain controls that imply these inactive values change the footage.

Use actual screenshots from [capture provenance](../art/screenshots/sources.json). Blue aquarium images retain their CC BY-SA 4.0 attribution and license. Deep ocean images use the credited NOAA source terms. Keep all media notices in host distributions.

Toolkit installs the published APK and selects its Dream separately. Its existing `jellyfish-drift` registration can follow a verified new release. Fire TV UI integration must update its pinned release metadata, digest, bytes, and native preview together after publication. A local candidate, repository commit, or source capture is not a published asset contract.

Host settings writes should follow the native foreground Settings route when the target's memory pressure makes a background provider unavailable. Report provider errors explicitly. Do not silently retry an unavailable provider into a successful configuration claim or change process priority to hide it.
