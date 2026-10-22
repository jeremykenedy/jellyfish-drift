# Configuration

Open Jellyfish Drift from the TV launcher and use the remote to change a choice. Settings persist on the device and take effect when the screensaver next starts.

| Setting | Options | Default |
| --- | --- | --- |
| Background | Deep water, bright water, random | Deep water |
| Jellyfish amount | A few, a handful, many, a ton, schools, random | A handful |
| Movement | Slow, natural, fast, random | Natural |
| Jellyfish species | Moon jelly, Pacific sea nettle, purple-striped jelly, random | Moon jelly |
| Sunlight from above | Off, on, random | Off |
| Randomize all settings each start | Off, on | Off |

The density choices draw 3, 7, 11, 17 or 25 actual animals. Movement scales the animation clock to 0.55, 1 or 1.65 times normal, including bell contractions, appendage motion and water effects. Moon jelly has a flatter bell and short fringe around four central oral arms; sea nettle has a higher russet bell and long tentacles; purple-striped jelly has a pale bell with violet bands and long trailing arms. Background changes the water lighting, and sunlight adds animated overhead rays. These are original rendered interpretations, not recorded species footage.

Each choice can be randomized independently. The all-settings control selects new values for each supported setting when a new screensaver session begins. Values are chosen once per session, so the visual style remains coherent until the next activation.

## Host application settings contract

The exported content provider is `com.jeremykenedy.jellyfishdrift.settings`. Its versioned cursor types are:

- `content://com.jeremykenedy.jellyfishdrift.settings/schema`: keys, labels, type, default, pipe-separated choices, and whether random is supported.
- `content://com.jeremykenedy.jellyfishdrift.settings/settings`: current key and value pairs.

Update a supported choice through `ContentResolver.update()` on the `settings` URI with `ContentValues` named `key` and `value`. Unsupported keys and values fail closed with `IllegalArgumentException`. Boolean values are the strings `true` or `false`. Do not assume additional settings beyond those returned by the schema.

The provider only exposes these non-sensitive visual preferences. It does not expose identity or device data and does not make network requests.
