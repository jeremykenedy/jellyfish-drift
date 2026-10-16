#!/usr/bin/env python3
"""Build the maintained themed banners and their content-addressed copies."""
from pathlib import Path
import hashlib
import re

ROOT = Path(__file__).resolve().parent.parent
ART = ROOT / 'art'


def banner(dark, mobile, still):
    width, height = (392, 408) if mobile else (800, 200)
    background = 'url(#night)' if dark else '#fff'
    foreground, secondary = ('#fff', '#a8a29e') if dark else ('#0f172a', '#64748b')
    style = '' if still else '''<style>
@keyframes drift{0%,100%{transform:translate(0,0) rotate(-4deg)}50%{transform:translate(7px,-12px) rotate(5deg)}}
@keyframes breathe{0%,100%{transform:scale(1,1)}34%{transform:scale(.86,1.12)}65%{transform:scale(.99,1.01)}}
@keyframes trailing{0%,100%{transform:skewX(-5deg)}50%{transform:skewX(6deg)}}
.jelly{animation:drift 7.5s ease-in-out infinite;transform-box:fill-box;transform-origin:center}
.bell{animation:breathe 3.7s ease-in-out infinite;transform-box:fill-box;transform-origin:center}
.arms{animation:trailing 5.5s ease-in-out infinite;transform-box:fill-box;transform-origin:top center}
@media(prefers-reduced-motion:reduce){.jelly,.bell,.arms{animation:none}}
</style>'''
    def cls(name):
        return '' if still else f' class="{name}"'
    jelly = f'''<g{cls('jelly')}>
<g{cls('arms')} fill="none" stroke="#60a5fa" stroke-linecap="round">
<path d="M45 65 C22 91 61 105 43 131 M57 67 C83 90 41 113 64 145 M70 65 C49 90 86 106 70 133" stroke-width="4" opacity=".55"/>
<path d="M25 57 C8 89 36 102 18 128 M37 64 C20 94 50 116 34 145 M83 61 C106 92 76 114 97 140 M94 55 C112 80 91 105 112 126" opacity=".65"/>
</g>
<g{cls('bell')}>
<path d="M12 54 C15 3 98 2 108 54 C88 74 32 74 12 54Z" fill="url(#gel)" stroke="#60a5fa" stroke-width="1.5"/>
<path d="M12 54 C42 45 78 45 108 54 M60 15 C35 28 32 42 30 62 M60 15 C48 32 47 49 46 67 M60 15 C74 33 74 50 76 66 M60 15 C92 30 91 44 93 62" fill="none" stroke="#93c5fd" opacity=".55"/>
<ellipse cx="52" cy="40" rx="6" ry="8" fill="none" stroke="#c4b5fd"/><ellipse cx="67" cy="42" rx="6" ry="8" fill="none" stroke="#c4b5fd"/>
</g></g>'''
    illustration = f'<g transform="translate({129 if mobile else 570} {199 if mobile else 35}) scale({1.12 if mobile else .93})">{jelly}</g>'
    pills = [('3D bells', 82), ('Dark and light', 112), ('Offline', 70)]
    x, y = 20 if mobile else 32, 132
    elements = []
    for label, length in pills:
        elements.append(f'<rect x="{x}" y="{y}" width="{length}" height="22" rx="11" fill="{chr(35)+"172033" if dark else chr(35)+"eff6ff"}" stroke="#60a5fa" stroke-width=".8"/><text x="{x+length/2}" y="{y+14}" fill="{chr(35)+"93c5fd" if dark else chr(35)+"1e40af"}" font-size="{9 if mobile else 10}" text-anchor="middle">{label}</text>')
        x += length + 8
    title_size = 36 if mobile else 40
    return f'''<svg xmlns="http://www.w3.org/2000/svg" width="{width}" height="{height}" viewBox="0 0 {width} {height}" role="img" aria-label="Jellyfish Drift: no tracking or analytics">
{style}<defs><linearGradient id="night"><stop stop-color="#0c0a09"/><stop offset="1" stop-color="#1c1917"/></linearGradient><linearGradient id="gel" x2="0" y2="1"><stop stop-color="#93c5fd" stop-opacity=".45"/><stop offset="1" stop-color="#6366f1" stop-opacity=".12"/></linearGradient></defs>
<rect width="{width}" height="{height}" rx="16" fill="{background}" stroke="{'#292524' if dark else '#e5e7eb'}"/>
<g font-family="system-ui, -apple-system, 'Segoe UI', sans-serif"><text x="{20 if mobile else 32}" y="{76 if mobile else 80}" font-size="{title_size}" font-weight="700" letter-spacing="-.5" fill="{foreground}">Jellyfish Drift</text>
<text x="{20 if mobile else 32}" y="{105 if mobile else 111}" font-size="{12 if mobile else 14}" fill="{secondary}">Living water. No tracking or analytics.</text>
{''.join(elements)}</g>{illustration}</svg>
'''


def main():
    for old in ART.glob('banner-*.*.svg'):
        if re.search(r'\.[0-9a-f]{12}\.svg$', old.name):
            old.unlink()
    mapping = {}
    for mobile in (False, True):
        for still in (False, True):
            for dark in (False, True):
                name = 'banner-' + ('mobile-' if mobile else '') + ('still-' if still else '') + ('dark' if dark else 'light')
                data = banner(dark, mobile, still).encode()
                (ART / (name + '.svg')).write_bytes(data)
                hashed = name + '.' + hashlib.sha256(data).hexdigest()[:12] + '.svg'
                (ART / hashed).write_bytes(data)
                mapping[name] = hashed
    return mapping


if __name__ == '__main__':
    main()
