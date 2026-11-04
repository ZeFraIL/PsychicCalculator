# 📌 Class Documentation: SplashActivity

### 1. General Information
- **Class Name:** `SplashActivity`
- **Type:** Activity (Android Screen)
- **Role in Application:** Displays the initial mystical splash screen featuring the application name in exotic typography and an animation of `main_image` scaling up from a single point to full screen width over 4 seconds, followed by automatic navigation.
- **Component Interaction:** Acts as the `LAUNCHER Activity`. Navigates to `LanguageActivity` via `Intent` and calls `finish()`.

---

### 2. Fields / Variables

| Name | Type | Purpose | Used In |
|------|------|---------|---------|
| `mainImage` | `ImageView` | Central branding graphic component | `onCreate()` for scaling animation |

---

### 3. Methods

**Method Name:** `onCreate`
- **Type:** `protected` | **Returns:** `void`
- **Parameters:** `savedInstanceState` (`Bundle`).
- **Logic:** Enables edge-to-edge layout, sets `activity_splash.xml`, resets `mainImage` scale and alpha to 0, and runs a 4000ms animation scaling `mainImage` to 1.0 with a callback to `navigateToLanguage()`.

**Method Name:** `navigateToLanguage`
- **Type:** `private` | **Returns:** `void`
- **Logic:** Verifies activity is valid, starts `LanguageActivity` via `Intent`, and calls `finish()`.

---

### 4. Lifecycle
- **`onCreate()`**: Initializes UI, resets image bounds, and triggers 4-second scaling animation.

---

### 5. Simple Explanation
Think of `SplashActivity` as the **curtain opening before a movie show**. The crystal ball image grows from a tiny dot to full screen over 4 seconds, and then the screen opens the door to language selection!
