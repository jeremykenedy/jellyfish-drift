# Configuration

This describes the isolated 2.0 filmed candidate. It has not passed final production native and physical quality gates.

Open Jellyfish Drift from the TV launcher. Changes take effect the next time a preview or screensaver starts. Credits and full license texts are available offline from settings, outside the screensaver image.

| Setting | Options | Default |
| --- | --- | --- |
| Filmed scene | Deep ocean, Blue aquarium, random | Deep ocean |
| Playback speed | 0.9x, 1x recorded speed, 1.25x, random | 1x |
| Random scene and playback speed each start | Off, on | Off |

Blue aquarium shows real Aurelia labiata footage recorded by Eric Polk at Monterey Bay Aquarium. Deep ocean shows real Crossota footage from NOAA Ocean Exploration. The environments come from the recordings. The light option is a blue aquarium scene, not a simulated daylight mode. Animal numbers, species, tentacles, and camera motion are fixed by each recording.

The Blue aquarium loop is 16 seconds. Deep ocean is approximately 19 seconds, and its source also contains a transition between camera views. Both use an editorial crossfade at the loop boundary. These are edited recordings, not naturally seamless events. The dark source was deinterlaced to correct top-field-first combing; some fine-line source artifacts remain.

Slowing playback also reduces the number of unique source frames shown each second. At 0.9x, the approximately 30 fps sources supply approximately 27 unique frames per second. Presentation cadence and decoding performance still require verification on each target device.

Random choices are resolved once when a new scene view is created. The all-settings toggle randomizes only the scene and playback speed.

## Host application settings contract

The exported content provider remains `com.jeremykenedy.jellyfishdrift.settings`:

- `content://com.jeremykenedy.jellyfishdrift.settings/schema` returns the three effective controls, with the existing cursor columns and MIME type.
- `content://com.jeremykenedy.jellyfishdrift.settings/settings` continues to return all six raw saved values, including legacy values that no longer affect filmed playback.

| Raw key | Accepted values | Effect in filmed mode |
| --- | --- | --- |
| `background` | `dark`, `light`, `random` | Deep ocean, Blue aquarium, or a random recording |
| `motion` | `slow`, `normal`, `fast`, `random` | 0.9x, 1x, 1.25x, or random playback speed |
| `randomize_all` | `true`, `false` | Randomizes scene and playback speed each start |
| `density` | `few`, `handful`, `many`, `ton`, `schools`, `random` | Retained without visual effect |
| `species` | `moon`, `sea_nettle`, `purple_stripe`, `random` | Retained without visual effect |
| `light_rays` | `off`, `on`, `random` | Retained without visual effect |

The three inactive keys are omitted from the schema and native settings UI. Existing saved values are neither deleted nor remapped. Reading settings does not replace latent legacy values. A compatible host should render controls from the schema instead of assuming every raw saved value is an effective control.

Updates use `ContentResolver.update()` on the `settings` URI with `ContentValues` named `key` and `value`. Existing validation for all six keys remains in effect. Unsupported keys and values fail with `IllegalArgumentException`. Boolean values use the strings `true` and `false`.

The provider exposes visual preferences only. Playback uses bundled files and does not request network access.
