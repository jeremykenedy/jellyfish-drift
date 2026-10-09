# Architecture

Jellyfish Drift is a dependency-free Android application with one DreamService and a native settings activity. The scene combines original high-detail transparent jellyfish artwork with a hardware-accelerated Android Canvas. Species drift through the water with pulsing bells, gently deformed tentacles, particles and optional moving top-down light.

The DreamService creates the scene when attached, starts its frame loop only while dreaming, and stops callbacks when the dream stops or detaches. The preview activity uses the same renderer. Preferences are local Android shared preferences. The exported settings provider offers a small, validated contract to host applications so a TV UI can discover and edit supported choices.

The application declares no network permission or third-party runtime libraries. Build outputs and release signing credentials stay outside version control. Pure option resolution and value validation are isolated so host tests can measure their line and branch coverage. Android lifecycle, graphics and TV behavior require emulator or device verification.
