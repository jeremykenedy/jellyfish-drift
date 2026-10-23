# Verification

The current Jellyfish Drift candidate is 3.0.0, Android code 6. Its signed APK SHA-256 is `eb5f6e9a5011b288917389daa9a5f4b23bbaba851820e858e251af4647a2c99e`, 1,242,197 bytes. Package identities and signing keys remain separate between classic and V2.

## Current checks

Local settings/installer tests, formatter, privacy checks, and 100% line/branch coverage of the actual pure options and validation sources pass. Android rendering and framework lifecycle are verified separately on native platforms.

On October 22, 2026, the exact code 6 signed candidate passed a same-signer API 34 emulator update retaining all 6 previous raw settings and adding `show_clock=false`. Its schema/settings cursors expose 7 values. Actual D-pad interaction enabled and disabled Show clock. Both explicit choices remained unchanged while randomize-all was enabled. Current Preview captures visibly show time only when enabled, with a small real position change across 20 seconds. Settings, scrollable offline Credits, the actual Close button, Preview, and Back passed. All baselines, including external file bytes and absent markers, were persisted before mutation. Cleanup restored the previous raw values and surrounding state exactly; the new default-off clock row is intentionally retained.

| Platform | Current evidence |
| --- | --- |
| Android TV API 34 emulator, 1920 x 1080 | Current update, legacy retention, 7-row schema, D-pad clock on/off, randomization independence, 20-second drift pair, Settings/Credits/Close/Preview/Back passed |
| Actual bound Dream on API 34 emulator | Not established: the production AVD does not allow root-only dream commands and exported Somnambulator did not bind a Dream in prior tests |
| Physical Fire TV | Current code 6 Dream, decoder/rendering and presentation gate pending |
| Physical Google TV | Untested; a volunteer with this device is needed |

Please report model, OS/API, resolution, tested behavior, and results through the repository's issue tracker when helping verify an untested device. Current [screenshots and provenance](SCREENSHOTS.md) identify the exact candidate. No physical performance or owner visual-acceptance claim is made.

## Earlier reference evidence

The classic scene and original species artwork come from actual public v1.0.0. Earlier clock-free code 5 retained all six values on a physical same-signer update and bound a normal Dream. Its pacing attempt failed layer discovery, so no frame-rate result is claimed. Those checks do not establish the current code 6 physical gate. The optional clock leaves the original scene view unchanged when off.

Native 4K detail, sustained thermal behavior, and automatic idle activation across vendors remain unverified.
