# Device verification

This file records evidence rather than inferred support. A build on an emulator does not establish vendor screensaver selection, physical 4K composition, thermal performance, or automatic idle activation.

| Platform | Device and OS | Result |
| --- | --- | --- |
| Android TV emulator | `sdk_google_atv64_arm64` | Settings activity and animated preview renderer launched; 1920x1080 screenshot captured. This emulator does not expose the system DreamService manager. |
| Fire TV | AFTDEC012E, Fire OS 11, API 30 | DreamService was activated and captured after 45 seconds. Settings activity, remote preview, and settings provider schema were verified. The TV reported a 3840x2160 panel with a 1920x1080 logical-size override; native 4K composition was not verified. |
| Google TV | Physical device not available | Not verified. |

The Fire TV capture is [jellyfish-dream-fire-tv.jpg](screenshots/jellyfish-dream-fire-tv.jpg). It was taken from the running `JellyfishDreamService`; the capture helper restored the prior screensaver selection (`com.androsaver/.ScreensaverService`) and enabled state (`1`) afterward. The dark 1920x1080 emulator screenshot is [jellyfish-preview.png](screenshots/jellyfish-preview.png); the light-background preview with sunlight enabled is [jellyfish-light-preview.png](screenshots/jellyfish-light-preview.png). The app's Android DreamService preview thumbnail uses a downsized copy of the Fire TV capture.

Frame pacing, memory/CPU use, and temperature after sustained playback have not been measured. Settings persistence and D-pad preview behavior were checked on the Fire TV. Activation and capture were verified for 45 seconds; extended thermal behavior remains unverified. Do not state native 4K support unless the actual device surface and display composition have been checked.
