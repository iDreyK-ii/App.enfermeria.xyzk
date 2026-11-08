# Android / Jetpack Compose integration

The Kotlin sources in this folder are a first integration layer, not a standalone APK.

- Package: `com.idreyk.enfermeria360.scales360`
- Requires the host application's Compose + Material 3 dependencies.
- Copy/import the package into the existing `:app` module only after checking package naming and navigation.
- Replace the temporary emoji scene markers with the six commissioned local illustration assets. Do not put score labels or clinical stimuli inside artwork.
- Connect `onOpenScale(id)` to the host app's existing scale routes; do not duplicate existing calculators.
- The six item modes are conservative presentation states. The authoritative permission/review status remains in the editorial JSON.
