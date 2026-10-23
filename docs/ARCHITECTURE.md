# Architecture

The isolated 2.0 candidate is an offline Android video screensaver. A shared `JellyfishSceneView` uses a centered, aspect-preserving `SurfaceView` and Android `MediaPlayer`. Preview and DreamService play the same bundled recordings without text overlays or runtime network access.

`JellyfishOptions` resolves only the actual recording and playback speed. The provider preserves all six existing raw preference values and validates their original accepted values. Its schema and native settings expose the three effective filmed controls. Density, species, and light-ray values remain stored without affecting the recordings.

The player prepares asynchronously and checks callback identity before starting. Pausing, surface destruction, and DreamService detachment release the decoder and cancel preparation timeouts. A valid surface can prepare playback again after resume or surface recreation. Each scene has a 1080p file and a 720p fallback. Preparation failure, a decoder error, or a 12-second preparation timeout attempts that scene's fallback once. Final failure closes the Dream or presents a message in Preview; it does not add text to the Dream image.

Scene resolution happens once per view. Resume preserves the chosen scene, speed, and last available playback position. Settings changes apply when a new view is created. `MediaPlayer` loops the bundled edited recording, including its documented editorial dissolve.

Credits, full media license text, and the application code license are bundled in assets and reachable from settings using the remote. Media licenses remain separate from the Apache-licensed application code. Source recordings, edits, asset checksums, and current quality limitations are recorded in `assets/NOTICE.txt` and `assets/media.json`.

Pure option resolution and legacy validation have local host tests. Android decoder, lifecycle, input navigation, loop transitions, and actual Dream presentation require native emulator or physical-device evidence. Tests do not substitute for the final moving quality gate.
