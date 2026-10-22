# ISS Tracker

An Android app that tracks the International Space Station in real-time on an OpenStreetMap.

## Features

- **Real-time ISS tracking** on an OpenStreetMap with live coordinates
- **People in Space** — list of all people currently in orbit (from open-notify API), grouped by spacecraft
- **Astronaut detail screen** — tap any astronaut to see their biography from Wikipedia
- **Custom app icon** — ISS station on a dark space background with stars
- **Multi-language support** — English, Russian, German, French, Spanish, Italian
- **Settings** — customize ISS marker color, path color, and clear path history
- **User location** — shows your position on the map alongside the ISS

## Screenshots

![Menu](menu_screenshot.png)
![People in Space](people_in_space.png)
![Astronaut Detail](astronaut_detail.png)
![Map](map_screenshot.png)

## Tech Stack

- **Language**: Kotlin 2.2.10
- **UI**: Jetpack Compose (Material 3)
- **Architecture**: MVVM with ViewModel + StateFlow
- **Map**: OSMDroid
- **Networking**: Retrofit + Gson
- **Images**: Coil
- **Location**: Google Play Services Location
- **Async**: Kotlin Coroutines
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36

## Setup

1. Clone the repository
2. Open the project in Android Studio (latest version recommended)
3. Sync Gradle files
4. Run on an emulator or physical device

> **Note**: The app requires an internet connection to fetch ISS data and location permissions to show your position on the map.

## APIs Used

- **[open-notify.org](http://api.open-notify.org/)** — Real-time ISS position and people in space data
- **[Wikipedia REST API](https://en.wikipedia.org/)** — Astronaut biographies and descriptions

## License

This project is for educational purposes.
