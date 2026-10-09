# Continuous integration

GitHub Actions builds the APK with Android SDK 36, runs host behavior tests, verifies 100% line and branch coverage for the pure settings modules, and checks the repository for syntax and generated artifacts. CI has no runtime credentials and does not publish a signed release. See `.github/workflows/` for the active checks.

The application has no third-party runtime dependencies, ads, analytics, telemetry, or network permission. External quality providers are only shown as badges when their workflows and access are configured and verified.
