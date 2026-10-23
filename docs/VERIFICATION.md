# Device verification

The 2.0 filmed candidate is isolated and has not been released or accepted as a production replacement. A build or preview does not prove vendor idle activation, sustained performance, or visual quality.

## Current candidate

- Package: `com.jeremykenedy.jellyfishdrift`.
- Version: `2.0.0`, code `4`.
- APK SHA-256: `109099f558be22a71970b56a962907053dd3b19cd6bc95b32a03101da62f0c8e`.
- Production signing certificate SHA-256: `eba70847a8e5fc55507fdf86d18bfe36ed9ae753e1e0e2ae4807676722ef949e`.
- Offline 1080p H264 recordings with a 720p decoder fallback. Output resolution does not imply native 4K source detail.

## Local checks

The signed build, privacy scan, installer tests, option regression tests, formatter, and Java coverage checks passed. The Java report covers 25 of 25 lines and 34 of 34 branches in pure option resolution and legacy value validation. This does not measure Android lifecycle or decoder correctness.

The previous Java formatter paths matched no application files, and coverage thresholds counted covered lines instead of requiring a coverage ratio. The candidate uses the repository as its Gradle project root, matches actual source files, and requires full line and branch ratios for those two pure classes.

## Earlier code 3: Android TV emulator, API 34

The earlier `0843b6b2` code 3 candidate was installed as a same-signer update. All six previously saved Jellyfish values survived the update. The provider returned three effective schema rows and all six raw saved values.

Native Preview recorded Blue aquarium for 35.03 seconds and Deep ocean for 40.01 seconds, spanning two loops of each scene. Both captures are 1920x1080. The recorded capture stream is approximately 30 fps; this is not a physical-device presentation measurement.

The settings screen showed only scene, playback speed, and randomization. Preview opened from the initially focused button and returned with Back. Slow and fast scene previews were captured. D-pad navigation reached Credits, the full offline notice and license text scrolled, and Back closed the dialog. Home released the player.

A follow-up verified normal task resume: Home released the player, reopening the task resumed Preview with a live 1920x1080 decoder, and Back returned to Settings. A separate remote-input check inspected fresh native focus XML at every step, selected Blue aquarium and 1.25x from their actual dropdowns, verified persisted values through the provider, and opened the corresponding Preview from the confirmed focused button. The dialog's explicit Close button and decoder fallback failure paths remain untested; Back closure passed.

The helper restored exact Jellyfish, Aquarium, Aquarium Live, UI backup, protection-service, and system settings. It left Home in the foreground without changing Dream selection. No app data was cleared or uninstalled.

## Moving quality and physical gates

The blue recording has a visible editorial loop dissolve and repeats every 16 seconds. The deep recording repeats approximately every 19 seconds and includes a source camera-view dissolve. Its top-field-first source was deinterlaced: detection changed from 599 of 600 TFF frames to 571 of 571 progressive output frames. Fine-line source artifacts remain. Corrected moving footage needs final visual review.

A separate package using the original blue video established real Fire TV Dream binding and approximately 30 settled presentations per second. That prototype is not this production candidate and cannot establish the current dark recording, playback speeds, settings, or lifecycle behavior.

The current production candidate still needs actual bound-Dream validation and physical presentation measurements at all supported speeds, plus final moving quality acceptance. Sustained thermal behavior and physical Google TV support remain unverified. Historical Canvas or shader screenshots in the repository are not evidence of the filmed candidate.

## Provider persistence defect and code 4

The code 3 physical run established actual non-test Dream playback for Blue aquarium at both speeds. Its minimum settled presentation windows were 26.904 presents/s at 0.9x and 36.238 at 1.25x. These are partial code 3 results, not a four-case or code 4 pass.

The first Deep ocean setup then failed: a successful shell update command was followed by repeated reads of `background=light` instead of the requested `dark`. The run stopped, preserved the failed readback, and restored the fresh current 46-value UI export, app preferences, eleven OS values, external backup/import bytes, guard/VPN, prior actual Dream, and sleep state exactly. No historical layout or CSV was restored.

Code 4 changes provider writes from asynchronous `apply()` to synchronous `commit()`, returning an updated row only when storage succeeds. Android documents that `apply()` acknowledges an in-memory update before its disk write finishes. Process-stop ordering or a swallowed command error may also contribute to the observed failure; the root cause is not claimed proven until native comparison.

The new native regression checks acknowledged writes after real process death, plus first writes after confirmed process exit, without retrying failed cases into a pass. It records command stdout/stderr, preserves all six original values, and restores them even when a case fails. Old/new native comparison and all four physical cases remain pending for code 4. Videos, preview image, renderer source, and other resources are byte-identical to code 3; only the provider, version code, and media metadata changed.
