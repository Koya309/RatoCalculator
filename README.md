# RatoCalculator

A modern, feature-rich Android calculator and unit converter application built with *Jetpack Compose* and *Modern Android Development (MAD)* best practices.

Designed with a clean *MVVM + Clean Architecture*, this project demonstrates high-quality software craftsmanship, reactive state handling, offline persistence, and seamless UI/UX design.

---

## Features

Calculator Engine: Supports basic and scientific mathematical expressions with real-time evaluation and auto-calculation powered by `exp4j`.

Unit Converter: Comprehensive unit conversion including length, volume, mass, temperature, and SI prefixes with two-way sync.

Calculation History: Persists mathematical operations and historical data locally using Room Database.

Localization (Multi-language): Full support for Dynamic Language switching (English & Thai) via modern Android App Locales.

Theme Customization: Complete Material Design 3 (M3) implementation supporting Light, Dark, and Dynamic Color themes.

Customizable Preferences: Fine-grained user settings saved via Jetpack DataStore (Decimal places, trailing zeros, unit display modes).

---

## Architecture & Tech Stack

This project follows the official **Android Architecture Guidelines** for scalable, testable, and maintainable codebases.

Language: Kotlin

UI Framework: Jetpack Compose with Material Design 3

Architecture: Clean Architecture + MVVM (Model-View-ViewModel)

Mathematical Expression Parser: [exp4j]

Dependency Injection: Hilt

Local Database: Room Persistence Library

Data Persistence: Jetpack DataStore

Asynchronous Programming: Kotlin Coroutines & `StateFlow` / `SharedFlow`

Navigation: Jetpack Compose Navigation Component

---

## Development Roadmap

- [x] Basic & Scientific Calculator Engine
- [x] Unit Converter with SI Prefixes
- [x] Calculation History Log (Room DB)
- [x] Dynamic Multi-language Support (EN/TH)
- [x] Material 3 Dynamic Theming
- [ ] Reset Settings Functionality *(In Progress)*
- [ ] Additional Unit Conversion Categories

---

## Acknowledgements & Open Source Libraries

This project makes use of the following open-source library:
* **[exp4j](https://www.objecthunter.net/exp4j/)** by Frank Lubeck – A handy mathematical expression evaluator for Java/Android.

---

## Author

Developed with dedication as a showcase portfolio project.  
Feel free to check out the codebase, open issues, or contribute!
