# Verification

The current Jellyfish Drift candidate is 3.0.0, Android code 7. Its signed APK SHA-256 is `2e3a8e5ad912b40bb614568e83c0f752b26e5be3bf9e8f06bb02e917063e3cb2`, 1,242,197 bytes. Package identities and signing keys remain separate between classic and V2.

## Current checks

Local settings/installer tests, formatter, privacy checks, and 100% line/branch coverage of the actual pure options, validation, and frame-pacing sources pass. Android rendering and framework lifecycle are verified separately on native platforms.

On October 22, 2026, the exact code 6 signed candidate passed a same-signer API 34 emulator update retaining all 6 previous raw settings and adding `show_clock=false`. Its schema/settings cursors expose 7 values. Actual D-pad interaction enabled and disabled Show clock. Both explicit choices remained unchanged while randomize-all was enabled. Current Preview captures visibly show time only when enabled, with a small real position change across 20 seconds. Settings, scrollable offline Credits, the actual Close button, Preview, and Back passed. All baselines, including external file bytes and absent markers, were persisted before mutation. Cleanup restored the previous raw values and surrounding state exactly; the new default-off clock row is intentionally retained.

| Platform | Current evidence |
| --- | --- |
| Android TV API 34 emulator, 1920 x 1080 | Code 6 update, legacy retention, 7-row schema, D-pad clock on/off, randomization independence, 20-second drift pair, Settings/Credits/Close/Preview/Back passed; code 7 scheduling retest pending |
| Actual bound Dream on API 34 emulator | Not established: the production AVD does not allow root-only dream commands and exported Somnambulator did not bind a Dream in prior tests |
| Physical Fire TV API 30, 1920 x 1080 surface | Code 6 normal Dream, controls, clock off/on, Credits, and retention passed, but presentation measured 20.00 and 20.66 FPS and failed the 24 FPS release gate. Code 7 retest pending |
| Physical Google TV | Untested; a volunteer with this device is needed |

Please report model, OS/API, resolution, tested behavior, and results through the repository's issue tracker when helping verify an untested device. Current [screenshots and provenance](SCREENSHOTS.md) identify the exact candidate. No current code 7 physical performance or owner visual-acceptance claim is made.

## Frame scheduling

Code 7 replaces a 33 ms delay after drawing with vsync callbacks and an intentional 30 FPS drawing cap. Motion still uses elapsed uptime; original artwork, drawing geometry, and motion formulas are unchanged. Tests cover 60 Hz, 59.94 Hz, 120 Hz, late callbacks, long gaps without catch-up bursts, and restart. This addresses the measured code 6 pacing failure; a host simulation does not establish physical frame rate.

## Earlier reference evidence

The classic scene and original species artwork come from actual public v1.0.0. Earlier clock-free code 5 retained all six values on a physical same-signer update and bound a normal Dream. Its pacing attempt failed layer discovery, so no frame-rate result is claimed. Code 6 later retained all six legacy values and added the new default-off clock row. Its raw cursor comparison reports a row-count difference because the cursor grew from six to seven; separate key/value comparison proves legacy retention. The raw result is preserved. Those checks do not establish the current code 7 physical gate. The optional clock leaves the original scene view unchanged when off.

Native 4K detail, sustained thermal behavior, and automatic idle activation across vendors remain unverified.
