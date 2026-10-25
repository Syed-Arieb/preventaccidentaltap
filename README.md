# PreventAccidentalTap

Fabric client mod for Minecraft 26.2.

## What it does

It stops extra left clicks on chests when you hold shears.
This helps in ExtremeCraft, where a second tap on a chest can waste Magic Wand durability.

## Download

Latest release:
https://github.com/Syed-Arieb/preventaccidentaltap/releases/latest

## How it works

- Only works when your main hand holds shears.
- Works on single and double chests.
- First tap goes through.
- Extra taps within the cooldown are blocked.
- Holding left click does not repeat taps.

## In game commands

- /pat-debug toggles debug messages.
- /pat-cooldown <ms> sets the delay between taps.

Settings are saved to config/preventaccidentaltap.properties.

## Build

./gradlew build
