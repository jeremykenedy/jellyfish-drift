#!/usr/bin/env python3
"""Write themed README banners and their cache-safe copies."""
from pathlib import Path
import hashlib
import re

ROOT = Path(__file__).resolve().parent.parent
ART = ROOT / 'art'
FONT = "system-ui,-apple-system,'Segoe UI',sans-serif"
STYLE = '''<style>
@keyframes drift{0%,100%{transform:translate(0,0)}50%{transform:translate(4px,-7px)}}
@keyframes frill{0%,100%{transform:rotate(-1deg)}50%{transform:rotate(2deg)}}
.float-a,.float-b,.filaments{transform-box:fill-box;transform-origin:center}
.float-a{animation:drift 6.4s ease-in-out infinite}.float-b{animation:drift 8.2s ease-in-out -3.2s infinite}
.filaments{animation:frill 4.8s ease-in-out -1.4s infinite}
@media(prefers-reduced-motion:reduce){.float-a,.float-b,.filaments{animation:none}}
</style>'''


def jelly():
    return '''<path d="M-38 43C-38 15-23 0 0 0S38 15 38 43Q28 37 20 44Q10 37 0 44Q-10 37-20 44Q-29 37-38 43Z" fill="url(#bell)" stroke="#60a5fa" stroke-width="1.3"/>
<path d="M-25 29C-18 10 16 8 25 29M-12 37Q0 25 12 37" fill="none" stroke="#c4b5fd" stroke-opacity=".7" stroke-width="1.4"/>
<g class="filaments" fill="none" stroke-linecap="round"><path d="M-25 43C-35 64-12 79-22 103S-22 123-14 130M-12 45C-20 63 5 80-4 104S-8 120 1 128M1 44C13 66-11 81 6 101S9 116 5 126M15 44C28 63 4 81 20 104S27 120 22 130M28 43C40 65 19 78 32 97S37 115 32 123" stroke="#a78bfa" stroke-opacity=".75" stroke-width="1.6"/></g>'''


def banner(theme, mobile, still):
    dark = theme == 'dark'
    width, height = (392, 408) if mobile else (800, 200)
    foreground = '#fff' if dark else '#0f172a'
    muted = '#a8a29e' if dark else '#64748b'
    background = 'url(#background)' if dark else '#fff'
    title_x, title_y, title_size = (20, 76, 36) if mobile else (40, 80, 40)
    tagline_y, tagline_size = (106, 13) if mobile else (111, 14)
    pill_y = 130
    illustration = 'translate(86 202) scale(1.08)' if mobile else 'translate(570 36)'
    labels = [('Filmed scenes', 92, '#60a5fa', '#eff6ff', '#1e40af'), ('Offline', 64, '#4ade80', '#f0fdf4', '#166534'), ('Playback speed', 104, '#a78bfa', '#f5f3ff', '#6d28d9')]
    pills = []
    x = 20 if mobile else 40
    for label, w, accent, light, text in labels:
        fill, ink = ('#1a1a1a', accent) if dark else (light, text)
        pills.append(f'<rect x="{x}" y="{pill_y}" width="{w}" height="22" rx="11" fill="{fill}" stroke="{accent}" stroke-width=".8"/><text x="{x+w/2}" y="{pill_y+14}" fill="{ink}">{label}</text>')
        x += w + 8
    result = f'''<svg xmlns="http://www.w3.org/2000/svg" width="{width}" height="{height}" viewBox="0 0 {width} {height}" role="img" aria-labelledby="title desc">
<title id="title">Jellyfish Drift</title><desc id="desc">Real filmed jellyfish scenes with offline playback and playback speed controls. The illustration is decorative.</desc>
{'' if still else STYLE}
<defs><linearGradient id="background" x2="1" y2="1"><stop stop-color="#0c0a09"/><stop offset="1" stop-color="#1c1917"/></linearGradient><radialGradient id="bell" cx="38%" cy="25%"><stop stop-color="#dbeafe" stop-opacity=".9"/><stop offset="1" stop-color="#6366f1" stop-opacity=".25"/></radialGradient></defs>
<rect width="{width}" height="{height}" rx="16" fill="{background}" stroke="{'#292524' if dark else '#e5e7eb'}"/>
<g font-family="{FONT}"><text x="{title_x}" y="{title_y}" fill="{foreground}" font-size="{title_size}" font-weight="700" letter-spacing="-.5">Jellyfish Drift</text><text x="{title_x}" y="{tagline_y}" fill="{muted}" font-size="{tagline_size}">Real jellyfish. Quiet water.</text>
<g font-size="{'9' if mobile else '10'}" font-weight="500" text-anchor="middle">{''.join(pills)}</g></g>
<g transform="{illustration}"><g transform="translate(62 12)"><g class="float-a">{jelly()}</g></g><g transform="translate(152 40) scale(.65)"><g class="float-b">{jelly()}</g></g></g>
</svg>'''
    if still:
        result = re.sub(r' class="(?:float-a|float-b|filaments)"', '', result)
    return result + '\n'


for old in ART.glob('banner-*.*.svg'):
    if re.search(r'\.[0-9a-f]{12}\.svg$', old.name):
        old.unlink()
for theme in ['light', 'dark']:
    for mobile in [False, True]:
        for still in [False, True]:
            name = 'banner-' + ('mobile-' if mobile else '') + ('still-' if still else '') + theme
            data = banner(theme, mobile, still).encode()
            (ART / (name + '.svg')).write_bytes(data)
            digest = hashlib.sha256(data).hexdigest()[:12]
            (ART / (name + '.' + digest + '.svg')).write_bytes(data)
readme = ROOT / 'README.md'
readme.write_text(re.sub(r'art/(banner-[a-z-]+)(?:\.[0-9a-f]{12})?\.svg', lambda m: 'art/' + m[1] + '.' + hashlib.sha256((ART / (m[1] + '.svg')).read_bytes()).hexdigest()[:12] + '.svg', readme.read_text()))
