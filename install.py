#!/usr/bin/env python3
"""Install, update, or remove Jellyfish Drift from an Android TV over ADB."""

import argparse
import subprocess
import sys
from pathlib import Path


ROOT = Path(__file__).resolve().parent
APK = ROOT / "build" / "jellyfish-drift.apk"
PACKAGE = "com.jeremykenedy.jellyfishdrift"


def run(arguments):
    return subprocess.run(arguments, check=False, text=True, capture_output=True)


def devices():
    result = run(["adb", "devices"])
    if result.returncode:
        raise RuntimeError(result.stderr.strip() or "ADB could not list devices")
    return [line.split()[0] for line in result.stdout.splitlines()[1:]
            if len(line.split()) > 1 and line.split()[1] == "device"]


def choose_device(serial, available):
    if serial:
        if serial not in available:
            raise RuntimeError("The requested ADB device is not connected or authorized")
        return serial
    if not available:
        raise RuntimeError("No authorized Android TV is connected. Connect it with ADB first")
    if len(available) == 1:
        return available[0]
    print("Connected Android devices:")
    for index, item in enumerate(available, start=1):
        print("  {}. {}".format(index, item))
    choice = input("Choose a device number: ").strip()
    if not choice.isdigit() or not 1 <= int(choice) <= len(available):
        raise RuntimeError("Choose one of the listed device numbers")
    return available[int(choice) - 1]


def confirm(prompt, assume_yes):
    if assume_yes:
        return True
    return input("{} [y/N] ".format(prompt)).strip().lower() in ("y", "yes")


def main():
    parser = argparse.ArgumentParser(description="Install or remove Jellyfish Drift on an Android TV")
    parser.add_argument("--serial", help="ADB serial or TV_IP:5555")
    parser.add_argument("--uninstall", action="store_true", help="Remove Jellyfish Drift")
    parser.add_argument("--yes", action="store_true", help="Confirm the install or update")
    parser.add_argument("--force", action="store_true", help="Confirm destructive non-interactive removal")
    args = parser.parse_args()
    if args.uninstall and args.yes and not args.force:
        parser.error("--yes does not confirm removal; use --force with --uninstall")
    if args.uninstall and args.yes and args.force and not sys.stdin.isatty():
        approved = True
    elif args.uninstall:
        approved = confirm("Remove Jellyfish Drift and its local settings?", args.force)
    else:
        if not APK.is_file():
            raise RuntimeError("APK not found. Run bash build.sh first")
        approved = confirm("Install or update Jellyfish Drift?", args.yes)
    if not approved:
        print("Cancelled. No device changes were made.")
        return 0
    serial = choose_device(args.serial, devices())
    print("\n  Jellyfish Drift\n  ----------------\n  Device: {}\n".format(serial))
    command = ["adb", "-s", serial]
    if args.uninstall:
        operation = command + ["uninstall", PACKAGE]
    else:
        operation = command + ["install", "-r", str(APK)]
    print("  Working...", end="", flush=True)
    result = run(operation)
    if result.returncode:
        print(" failed")
        raise RuntimeError(result.stderr.strip() or result.stdout.strip() or "ADB command failed")
    print(" complete\n\n  {}\n".format(result.stdout.strip()))
    print("  The app does not change the TV's screensaver selection or system timers.")
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except (OSError, RuntimeError) as error:
        print("\n  Failed: {}\n".format(error), file=sys.stderr)
        raise SystemExit(1)
