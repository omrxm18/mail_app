# mail — Kotlin Multiplatform / Compose Multiplatform port

A rewrite of the Flutter `mail_app` UI into Kotlin Multiplatform, targeting
**Android** and **Desktop** (Windows/Linux/macOS) from one `commonMain` source
set, matching the KMP + Compose Multiplatform architecture used on QtStock.

## What's here

- **`model/`** — `Account`, `MailFolder`/`FolderKind`, `Message` (plain `data class`es,
  same shape as the Dart models; `Message.folder` stays a raw string key for the
  same reason the Dart `typedef FolderKindRef` did).
- **`data/MockMailSource.kt`** — same six mock messages/accounts/folders as the
  Flutter version, timed relative to the same fixed "now".
- **`format/DateFormatting.kt`** — hand-rolled replacement for `intl`'s
  `DateFormat` (kotlinx-datetime doesn't ship one); produces identical output
  ("14:02", "Yesterday", "Mon", "Sun, Jun 21 — 14:02").
- **`theme/AppTheme.kt`** — `AppColors` (identical hex values) and
  `rememberAppText()`, a Composable returning all the named `TextStyle`s. Text
  styles are exposed through a Composable rather than top-level constants
  because loading the bundled Inter/JetBrains Mono fonts requires a resource
  loader that's only available inside composition.
- **`state/MailViewModel.kt`** — replaces the five Riverpod providers with one
  `ViewModel` exposing `StateFlow`s (`folders`, `messages`, `selectedFolder`,
  `visibleMessages`, `selectedMessage`, `mobileScreen`) plus the mutating
  functions (`selectFolder`, `selectMessage`, `markRead`, `markUnread`,
  `goBackMobile`). No DI container is wired up yet since there's nothing to
  inject beyond the mock source — instantiate directly via `viewModel { MailViewModel() }`,
  same as QtStock's manual-DI pattern.
- **`ui/`** — `InboxScreen` (breakpoint switch via `BoxWithConstraints`,
  same 700dp cutoff as the Flutter `kMobileBreakpoint`), `FolderSidebar`,
  `MessageListPane`, `ReadingPane`, `MobileInboxNav`. `BorderModifiers.kt`
  holds small `Modifier.borderBottom/Top/Right/leftAccentBar` helpers standing
  in for Flutter's free `Border(...)` on `BoxDecoration`.
- **Fonts** — the real Inter/JetBrains Mono `.ttf` files from the Flutter
  project's `assets/fonts/` are copied into
  `composeApp/src/commonMain/composeResources/font/` (renamed to
  `snake_case`, which Compose Multiplatform's resource generator requires).

## Not ported

`gmail_oauth_service.dart`, `gmail_imap_service.dart`, `google_oauth_config.dart`,
and `gmail_debug_screen.dart` were left out. In the original code they're
explicitly marked as a temporary manual test harness ("TEMP: routes to the
Gmail IMAP debug screen... remove once real account setup replaces this") —
there's no real OAuth/IMAP flow to preserve yet, just a debug screen and a
FAB wired to it in `main.dart`. If you want this ported too (e.g. as a real
Ktor-based IMAP/OAuth layer), say the word and I'll add it.

## Known gaps / trade-offs

- **App icons** — `AndroidManifest.xml` references `@mipmap/ic_launcher`,
  but launcher icon assets weren't ported (the Flutter ones are PNGs sized
  for Android's mipmap buckets; Compose Multiplatform for iOS/desktop icons
  work differently). Either add your own icon set at
  `composeApp/src/androidMain/res/mipmap-*` or Android Studio's asset
  wizard will happily generate one from a template.
- **Local persistence** — the Flutter project depended on `sqflite` but
  nothing in the ported screens actually used it yet (still MockMailSource
  everywhere). Same here — swap `MockMailSource` for a real repository
  behind `MailViewModel` when sync exists; nothing above it needs to change.
- **Gradle/Compose Multiplatform version drift** — versions are pinned
  (Kotlin 2.0.21, Compose Multiplatform 1.7.0, AGP 8.5.2) to what's known-good
  as of writing. This project can't be built or run in this sandbox (no
  Android SDK / Gradle Multiplatform toolchain, and network access here is
  limited to a handful of package registries), so open it in Android Studio
  or IntelliJ with the Kotlin Multiplatform plugin, let it sync, and bump
  versions if Gradle complains about anything newer being available.

## Running it

```
./gradlew :composeApp:run              # desktop
./gradlew :composeApp:installDebug      # android, device/emulator attached
```
