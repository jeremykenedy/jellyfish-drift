import io
import runpy
import sys
import unittest
from contextlib import redirect_stderr, redirect_stdout
from pathlib import Path
from unittest.mock import patch
import subprocess

import install


class InstallerTest(unittest.TestCase):
    def test_run_uses_captured_nonraising_subprocess(self):
        result = object()
        with patch.object(subprocess, "run", return_value=result) as mocked_run:
            self.assertIs(result, install.run(["adb", "devices"]))
        mocked_run.assert_called_once_with(["adb", "devices"], check=False, text=True, capture_output=True)

    def test_devices_returns_only_authorized_serials(self):
        result = type("Result", (), {"returncode": 0, "stdout": "List\nTV1 device\nTV2 unauthorized\noffline offline\n", "stderr": ""})()
        with patch.object(install, "run", return_value=result):
            self.assertEqual(["TV1"], install.devices())

    def test_devices_reports_adb_failure(self):
        result = type("Result", (), {"returncode": 1, "stdout": "", "stderr": "adb unavailable"})()
        with patch.object(install, "run", return_value=result):
            with self.assertRaisesRegex(RuntimeError, "adb unavailable"):
                install.devices()

    def test_choose_device_validates_and_selects(self):
        self.assertEqual("TV1", install.choose_device("TV1", ["TV1"]))
        self.assertEqual("TV1", install.choose_device(None, ["TV1"]))
        with self.assertRaisesRegex(RuntimeError, "not connected"):
            install.choose_device("TV2", ["TV1"])
        with patch("builtins.input", return_value="2"):
            self.assertEqual("TV2", install.choose_device(None, ["TV1", "TV2"]))
        for choice in ("", "0", "3", "x"):
            with patch("builtins.input", return_value=choice):
                with self.assertRaisesRegex(RuntimeError, "listed device numbers"):
                    install.choose_device(None, ["TV1", "TV2"])

    def test_confirm_handles_flag_and_prompt(self):
        self.assertTrue(install.confirm("continue?", True))
        with patch("builtins.input", return_value="yes"):
            self.assertTrue(install.confirm("continue?", False))
        with patch("builtins.input", return_value="n"):
            self.assertFalse(install.confirm("continue?", False))

    def test_install_requires_built_apk_and_confirmation(self):
        with patch.object(sys, "argv", ["install.py", "--yes"]), patch.object(Path, "is_file", return_value=False):
            with self.assertRaisesRegex(RuntimeError, "Run bash build.sh"):
                install.main()
        output = io.StringIO()
        with patch.object(sys, "argv", ["install.py"]), patch.object(Path, "is_file", return_value=True), \
                patch("builtins.input", return_value="no"), redirect_stdout(output):
            self.assertEqual(0, install.main())
        self.assertIn("No device changes", output.getvalue())

    def test_install_runs_on_selected_device(self):
        result = type("Result", (), {"returncode": 0, "stdout": "Success", "stderr": ""})()
        output = io.StringIO()
        with patch.object(sys, "argv", ["install.py", "--yes", "--serial", "TV1"]), \
                patch.object(Path, "is_file", return_value=True), patch.object(install, "devices", return_value=["TV1"]), \
                patch.object(install, "run", return_value=result) as mocked_run, redirect_stdout(output):
            self.assertEqual(0, install.main())
        self.assertEqual(["adb", "-s", "TV1", "install", "-r", str(install.APK)], mocked_run.call_args.args[0])
        self.assertIn("does not change the TV's screensaver", output.getvalue())

    def test_uninstall_requires_explicit_force_for_noninteractive_yes(self):
        with patch.object(sys, "argv", ["install.py", "--uninstall", "--yes"]):
            with self.assertRaises(SystemExit):
                install.main()

    def test_uninstall_confirmation_and_result(self):
        with patch.object(sys, "argv", ["install.py", "--uninstall"]), patch("builtins.input", return_value="no"), \
                redirect_stdout(io.StringIO()):
            self.assertEqual(0, install.main())
        result = type("Result", (), {"returncode": 0, "stdout": "Success", "stderr": ""})()
        with patch.object(sys, "argv", ["install.py", "--uninstall", "--yes", "--force"]), \
                patch.object(sys.stdin, "isatty", return_value=False), patch.object(install, "devices", return_value=["TV1"]), \
                patch.object(install, "run", return_value=result) as mocked_run, redirect_stdout(io.StringIO()):
            self.assertEqual(0, install.main())
        self.assertEqual(["adb", "-s", "TV1", "uninstall", install.PACKAGE], mocked_run.call_args.args[0])

    def test_missing_device_and_adb_operation_failure_are_reported(self):
        with patch.object(sys, "argv", ["install.py", "--yes"]), patch.object(Path, "is_file", return_value=True), \
                patch.object(install, "devices", return_value=[]):
            with self.assertRaisesRegex(RuntimeError, "No authorized"):
                install.main()
        failed = type("Result", (), {"returncode": 1, "stdout": "", "stderr": "install failed"})()
        with patch.object(sys, "argv", ["install.py", "--yes"]), patch.object(Path, "is_file", return_value=True), \
                patch.object(install, "devices", return_value=["TV1"]), patch.object(install, "run", return_value=failed), \
                redirect_stdout(io.StringIO()):
            with self.assertRaisesRegex(RuntimeError, "install failed"):
                install.main()

    def test_force_uninstall_with_interactive_terminal_still_asks(self):
        result = type("Result", (), {"returncode": 0, "stdout": "Success", "stderr": ""})()
        with patch.object(sys, "argv", ["install.py", "--uninstall", "--force"]), \
                patch.object(sys.stdin, "isatty", return_value=True), patch("builtins.input", return_value="yes"), \
                patch.object(install, "devices", return_value=["TV1"]), patch.object(install, "run", return_value=result), \
                redirect_stdout(io.StringIO()):
            self.assertEqual(0, install.main())

    def test_command_entrypoint_success_and_failure(self):
        success = type("Result", (), {"returncode": 0, "stdout": "Success", "stderr": ""})()
        device_list = type("Result", (), {"returncode": 0, "stdout": "List\nTV1 device\n", "stderr": ""})()
        with patch.object(sys, "argv", ["install.py", "--yes", "--serial", "TV1"]), \
                patch.object(Path, "is_file", return_value=True), patch.object(subprocess, "run", side_effect=[device_list, success]), \
                redirect_stdout(io.StringIO()):
            with self.assertRaises(SystemExit) as result:
                runpy.run_path(str(install.ROOT / "install.py"), run_name="__main__")
        self.assertEqual(0, result.exception.code)
        failed_devices = type("Result", (), {"returncode": 1, "stdout": "", "stderr": "adb unavailable"})()
        errors = io.StringIO()
        with patch.object(sys, "argv", ["install.py", "--yes"]), patch.object(Path, "is_file", return_value=True), \
                patch.object(subprocess, "run", return_value=failed_devices), redirect_stderr(errors):
            with self.assertRaises(SystemExit) as result:
                runpy.run_path(str(install.ROOT / "install.py"), run_name="__main__")
        self.assertEqual(1, result.exception.code)
        self.assertIn("adb unavailable", errors.getvalue())


if __name__ == "__main__":
    unittest.main()
