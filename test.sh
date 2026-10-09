#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
OUT="$ROOT/build/tests"
mkdir -p "$OUT"
javac -source 8 -target 8 -d "$OUT" \
  "$ROOT/src/com/jeremykenedy/jellyfishdrift/JellyfishOptions.java" \
  "$ROOT/src/com/jeremykenedy/jellyfishdrift/SettingsValues.java" \
  "$ROOT/tests/JellyfishOptionsTest.java"
java -ea -cp "$OUT" com.jeremykenedy.jellyfishdrift.JellyfishOptionsTest
python3 -m unittest -v tests.test_installer
