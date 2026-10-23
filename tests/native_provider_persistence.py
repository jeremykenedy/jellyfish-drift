#!/usr/bin/env python3
"""Check acknowledged settings writes across real Android process death."""
import argparse
import json
import re
import shlex
import subprocess
import time
from pathlib import Path

PACKAGE = 'com.jeremykenedy.jellyfishdrift'
URI = 'content://' + PACKAGE + '.settings/settings'


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--serial', required=True)
    parser.add_argument('--cycles', type=int, default=6)
    parser.add_argument('--output', type=Path, required=True)
    parser.add_argument('--lease-file', type=Path)
    parser.add_argument('--lease-grant')
    parser.add_argument('--lease-owner')
    args = parser.parse_args()
    if not 1 <= args.cycles <= 30:
        parser.error('--cycles must be between 1 and 30')
    if args.lease_file and not (args.lease_grant and args.lease_owner):
        parser.error('--lease-file requires --lease-grant and --lease-owner')
    args.output.mkdir(parents=True, exist_ok=True)
    commands = []

    def adb(*command, allow_failure=False):
        if args.lease_file:
            lease = json.loads(args.lease_file.read_text())
            if (lease.get('owner') != args.lease_owner
                    or lease.get('grant') != args.lease_grant
                    or lease.get('serial', lease.get('device')) != args.serial):
                raise RuntimeError('The exclusive device lease changed')
        result = subprocess.run(['adb', '-s', args.serial, *command],
                                capture_output=True, text=True, timeout=30)
        commands.append({'arguments': list(command), 'returnCode': result.returncode,
                         'stdout': result.stdout, 'stderr': result.stderr})
        (args.output / 'commands.json').write_text(json.dumps(commands, indent=2) + '\n')
        content_error = (command and command[0] == 'shell'
                         and command[1].startswith('content ')
                         and re.search(r'Exception|Error|Failed|not found',
                                       result.stderr + result.stdout, re.IGNORECASE))
        if (result.returncode or content_error) and not allow_failure:
            raise RuntimeError(result.stderr + result.stdout)
        return result.stdout.strip()

    def shell(*command, allow_failure=False):
        return adb('shell', shlex.join(command), allow_failure=allow_failure)

    def read():
        rows = shell('content', 'query', '--uri', URI)
        values = dict(re.findall(r'key=([^,\n]+), value=([^\n]+)', rows))
        values = {key.strip(): value.strip() for key, value in values.items()}
        if len(values) != 6:
            raise RuntimeError('Expected all six existing settings from the provider')
        return values

    def write(key, value):
        return shell('content', 'update', '--uri', URI,
                     '--bind', 'key:s:' + key, '--bind', 'value:s:' + value)

    def stop(confirmed):
        shell('am', 'force-stop', PACKAGE)
        if confirmed:
            for _ in range(30):
                if not shell('pidof', PACKAGE, allow_failure=True):
                    return
                time.sleep(.1)
            raise RuntimeError('The previous settings process did not stop')

    before = read()
    (args.output / 'before.json').write_text(json.dumps(before, indent=2) + '\n')
    observations = []
    failure = None
    expected = dict(before)
    try:
        for cycle in range(args.cycles):
            for scenario in ['write-then-death', 'confirmed-death-then-write']:
                if scenario == 'confirmed-death-then-write':
                    stop(True)
                updates = {
                    'background': 'light' if expected['background'] != 'light' else 'dark',
                    'motion': 'fast' if expected['motion'] != 'fast' else 'slow',
                    'randomize_all': 'true' if expected['randomize_all'] != 'true' else 'false',
                }
                for key, value in updates.items():
                    write(key, value)
                expected.update(updates)
                if scenario == 'write-then-death':
                    stop(True)
                actual = read()
                entry = {'cycle': cycle, 'scenario': scenario,
                         'expected': dict(expected), 'actual': actual,
                         'passed': actual == expected}
                observations.append(entry)
                print(json.dumps(entry), flush=True)
                if actual != expected:
                    raise RuntimeError('Acknowledged provider writes did not survive process death')
    except Exception as error:
        failure = str(error)
        raise
    finally:
        # Cleanup retries restore the baseline; they never turn a failed case into a pass.
        stop(True)
        restored = False
        for _ in range(3):
            for key, value in before.items():
                write(key, value)
            stop(True)
            actual = read()
            if actual == before:
                restored = True
                break
        result = {'package': PACKAGE, 'observations': observations,
                  'failure': failure, 'baselineRestoredExactly': restored,
                  'passed': failure is None and restored,
                  'scope': 'No activity, Dream selection, OS setting, permission, or other app was changed'}
        (args.output / 'result.json').write_text(json.dumps(result, indent=2) + '\n')
        if not restored:
            raise RuntimeError('The original settings were not restored exactly')
        print('Original six settings restored exactly.', flush=True)


if __name__ == '__main__':
    main()
