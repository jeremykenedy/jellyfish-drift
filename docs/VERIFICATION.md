# Verification

The current Jellyfish Drift candidate is 3.0.0, Android code 7. Its signed APK SHA-256 is `2e3a8e5ad912b40bb614568e83c0f752b26e5be3bf9e8f06bb02e917063e3cb2`, 1,242,197 bytes. Package identities and signing keys remain separate between classic and V2.

## Current checks

Local settings/installer tests, formatter, privacy checks, and 100% line/branch coverage of the actual pure options, validation, and frame-pacing sources pass. Android rendering and framework lifecycle are verified separately on native platforms.

On October 22, 2026, the exact code 6 signed candidate passed a same-signer API 34 emulator update retaining all 6 previous raw settings and adding `show_clock=false`. Its schema/settings cursors expose 7 values. Actual D-pad interaction enabled and disabled Show clock. Both explicit choices remained unchanged while randomize-all was enabled. Current Preview captures visibly show time only when enabled, with a small real position change across 20 seconds. Settings, scrollable offline Credits, the actual Close button, Preview, and Back passed. All baselines, including external file bytes and absent markers, were persisted before mutation. Cleanup restored the previous raw values and surrounding state exactly; the new default-off clock row is intentionally retained.

| Platform | Current evidence |
| --- | --- |
| Android TV API 34 emulator, 1920 x 1080 | Code 6 legacy retention, 7-row schema, D-pad clock on/off, randomization independence, 20-second drift pair, Settings/Credits/Close/Preview/Back passed; current code 7 tested directly on the physical device |
| Actual bound Dream on API 34 emulator | Not established: the production AVD does not allow root-only dream commands and exported Somnambulator did not bind a Dream in prior tests |
| Physical Fire TV API 30, 1920 x 1080 surface | Code 7 exact installed hash and seven-value retention passed. Native controls, D-pad clock off/on, Credits/scroll/Close, Preview/Home/resume and two actual normal Dream presets passed. Minimum sampled app-layer presentation rates 29.9987 OFF and 30.4872 ON |
| Physical Google TV | Untested; a volunteer with this device is needed |

Please report model, OS/API, resolution, tested behavior, and results through the repository's issue tracker when helping verify an untested device. Current [screenshots and provenance](SCREENSHOTS.md) identify the exact candidate. The two physical presets passed the 24 FPS presentation gate. Owner visual acceptance is not claimed.

## Frame scheduling

Code 7 replaces a 33 ms delay after drawing with vsync callbacks and an intentional 30 FPS drawing cap. Motion still uses elapsed uptime; original artwork, drawing geometry, and motion formulas are unchanged. Tests cover 60 Hz, 59.94 Hz, 120 Hz, late callbacks, long gaps without catch-up bursts, and restart. Code 6 measured 20.00 and 20.66 FPS and failed its release gate. Code 7 passed three settled actual SurfaceFlinger app-layer timestamp windows per preset without screenrecord: dark/few/moon/slow clock OFF minimum 29.9987 presents/s; bright/schools/nettle/fast/rays clock ON minimum 30.4872 presents/s. The ON app layer includes separate clock redraws; it is not a claim that the scene cap is 30.49. The windows are sampled buffers, not a continuous full-duration trace.

## Earlier reference evidence

The classic scene and original species artwork come from actual public v1.0.0. Earlier clock-free code 5 retained all six values on a physical same-signer update and bound a normal Dream. Its pacing attempt failed layer discovery, so no frame-rate result is claimed. Code 6 later retained all six legacy values and added the new default-off clock row. Its raw cursor comparison reports a row-count difference because the cursor grew from six to seven; separate key/value comparison proves legacy retention. The raw result is preserved. Current code 7 physical checks are recorded above. The optional clock leaves the original scene view unchanged when off.

Native 4K detail, sustained thermal behavior, and automatic idle activation across vendors remain unverified.

## Native route and preservation

The current physical code 7 update retained all six legacy values plus the previously saved clock boolean. Native widgets read back all original controls; Show clock on/off used actual D-pad focus and Select. Credits scrolled and its actual Close button closed the dialog. Preview opened and resumed after Home. A harness assumption that an externally reopened Preview must return directly to Settings failed; its raw failure is retained, and no direct Back-to-Settings claim is made for that task stack. The corrected gate launched Settings with Android's supported clear-top behavior before each normal Dream and verified actual binding and Back detachment.

Every attempt restored its fresh UI46, OS11, readable surrounding providers, external file bytes, appop, guard/VPN, and Asleep state with the launcher underneath. An unavailable other-app provider was recorded as unsupported rather than claimed preserved by value. Public gallery/banner review, verified remote primary, and release download checks remain pending.
