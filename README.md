
# 📍 Where Did I Keep It?

### Remember where. Find when. ✨

**Where Did I Keep It?** is a simple, privacy-focused Android application that helps you remember where you kept your everyday belongings.

Save an item with a **photo, name, location, and notes**, then quickly find it later using search.

> 📸 Save your things
> 📍 Remember where
> 🔎 Find it later

---

## ✨ Features

* 👤 **Multiple User Accounts** — Create and manage separate local user accounts.
* 🔐 **Duplicate Account Protection** — Prevents duplicate account names.
* 📦 **Save Items** — Store belongings with a name, photo, location, and notes.
* 📸 **Camera Support** — Take a photo directly from the app.
* 🖼️ **Photo Picker** — Select existing photos from your device.
* 📍 **Custom Locations** — Create flexible hierarchical locations such as `Home → Kitchen → Cabinet → 3rd Shelf`.
* 🔎 **Advanced Search** — Search items using names, locations, and notes.
* 💡 **Search Suggestions** — Get useful suggestions while searching.
* 🕘 **Recent Searches** — Quickly access previous searches.
* ⭐ **Favorites / Pinning** — Keep important items easily accessible.
* ↕️ **Sorting & Filtering** — Organize and narrow down search results.
* ✏️ **Edit Items** — Update item details, locations, notes, and photos.
* 🗑️ **Delete Items** — Remove items with confirmation.
* 💾 **Offline Storage** — Data is stored locally on the device.
* 🔒 **Privacy Focused** — No cloud backend or online account is required.
* 🎨 **Modern Material 3 UI** — Clean and friendly pastel interface.
* 📱 **Physical Device Tested** — Tested on a real Android device.

---

## 📱 Screenshots

### 🌟 Welcome Screen

<p align="center">
 width="716" height="1599" alt="WhatsApp Image 2026-09-12 at 13 17 11" src="https://github.com/user-attachments/assets/12ada836-d847-4b4d-a9d7-048c68eb9664" />
/>
</p>

### 👤 Account Selection

<p align="center">
  width="716" height="1599" alt="WhatsApp Image 2026-09-12 at 13 17 45" src="https://github.com/user-attachments/assets/4a30e986-e632-4f66-a322-3f16cab1b31d" />
/>
</p>


### ➕ Add Item

<p align="center">
 <img width="716" height="1599" alt="WhatsApp Image 2026-09-12 at 13 18 22" src="https://github.com/user-attachments/assets/d46cb95d-70af-4cd6-8128-7fa20c59ba23" />
/>
</p>


### 🔎 Search

<p align="center">
  <img width="716" height="1599" alt="WhatsApp Image 2026-09-12 at 13 19 39" src="https://github.com/user-attachments/assets/fc48dcbe-fb70-43e7-969c-2dd451ea87dd" />

</p>


### ⚙️ Settings

<p align="center">
  <img width="716" height="1599" alt="WhatsApp Image 2026-09-12 at 13 20 03" src="https://github.com/user-attachments/assets/5a4f886f-b2df-4375-90df-3849889e03e2" />

</p>

---

## 🎯 Problem Statement

People often forget where they kept their belongings.

For example:

> *"Where did I keep my passport?"*
> *"Which cupboard has my charger?"*
> *"Where did I put that document?"*

Instead of relying on memory, this application lets users create a digital record of their belongings.

For example:

**Item**

`Passport`

**Location**

`Home → Bedroom → Cupboard → 2nd Shelf`

**Notes**

`Inside the blue document folder.`

Later, the user can simply search for **Passport** and immediately find its saved location.

---

## 🚀 How It Works

The basic workflow is:

**1. Create or select an account**
**2. Add an item**
**3. Add a photo**
**4. Enter the item name**
**5. Select or create a custom location**
**6. Add optional notes**
**7. Save the item**
**8. Search for it whenever needed**

---

## 🔎 Search

The application allows users to search their saved belongings quickly.

Search can be performed using:

* Item name
* Location
* Notes

### Example

Searching for:

`Kitchen`

can find:

`Home → Kitchen → Cabinet → 3rd Shelf`

Searching for:

`charger`

can find an item based on its name or notes.

The application also includes:

* Search suggestions
* Recent searches
* Result highlighting
* Sorting
* Location filters
* Photo filters
* Favorites / pinning

---

## 👤 Multiple Accounts

The application supports multiple local users.

Each user can maintain their own collection of saved belongings.

For example:

```text
User A
 ├── Passport
 ├── Camera
 └── Charger

User B
 ├── Books
 ├── Documents
 └── Accessories
```

All account and item information is stored locally on the device.

---

## 🔐 Duplicate Account Protection

The application prevents users from creating duplicate account names.

Account names are checked after trimming unnecessary spaces and comparing names without considering letter case.

For example:

* `user`
* `User`
* `USER`
* `user`

are treated as the same account name.

When a duplicate is detected, the application displays:

**Account already exists ⚠️**

> An account with this name already exists. Please use another name.

The existing account and its data are not overwritten or deleted.

---

## 📸 Photo Management

Users can associate real photos with their saved belongings.

The application supports:

* 📷 Taking photos using the camera
* 🖼️ Selecting photos from the device
* 👀 Previewing photos
* 🔄 Replacing photos
* ❌ Removing photos

Photos are stored locally on the device.

---

## ✏️ Edit & Delete

Saved items can be managed after creation.

### Edit

Users can update:

* Item name
* Photo
* Location
* Notes

### Delete

Items can be deleted when they are no longer needed, with a confirmation step to help prevent accidental deletion.

---

## 💾 Offline-First Architecture

The application is designed to work without requiring an internet connection for its core functionality.

User data is stored locally:

```text
                 Android App
                     │
          ┌──────────┴──────────┐
          │                     │
          ▼                     ▼
   Room Database          Local Photo Files
          │
          ▼
     Item Information
```

There is no Firebase or cloud database required for the core application.

---

## 🏗️ Architecture

The project follows a modern Android architecture using a layered approach.

```text
┌────────────────────────────┐
│       Jetpack Compose      │
│             UI             │
└─────────────┬──────────────┘
              │
              ▼
┌────────────────────────────┐
│          ViewModel         │
└─────────────┬──────────────┘
              │
              ▼
┌────────────────────────────┐
│         Repository         │
└─────────────┬──────────────┘
              │
              ▼
┌────────────────────────────┐
│            DAO             │
└─────────────┬──────────────┘
              │
              ▼
┌────────────────────────────┐
│       Room Database        │
└────────────────────────────┘
```

### UI Layer

Responsible for:

* Screen design
* User interactions
* Forms
* Search interface
* Navigation
* Material 3 components

### ViewModel

Responsible for:

* UI state
* Business logic
* Search state
* Managing data flow

### Repository

Provides an abstraction between the UI/ViewModel and database layer.

### Room Database

Provides local persistent storage for item and account information.

---

## 🛠️ Tech Stack

| Technology               | Purpose                         |
| ------------------------ | ------------------------------- |
| **Kotlin**               | Primary programming language    |
| **Jetpack Compose**      | Android UI                      |
| **Material 3**           | Design system and UI components |
| **Room Database**        | Local data persistence          |
| **Kotlin Coroutines**    | Asynchronous operations         |
| **Kotlin Flow**          | Reactive data streams           |
| **ViewModel**            | UI state management             |
| **Repository Pattern**   | Data abstraction                |
| **Android Camera API**   | Taking photos                   |
| **Android Photo Picker** | Selecting photos                |
| **Coil**                 | Image loading                   |
| **Gradle**               | Build system                    |

---

## 📂 Project Structure

```text
Find_Item/
│
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/
│   │       │       └── wheredidikeepit/
│   │       │           └── app/
│   │       │
│   │       ├── res/
│   │       └── AndroidManifest.xml
│   │
│   ├── build.gradle.kts
│   └── proguard-rules.pro
│
├── gradle/
│
├── screenshots/
│   ├── welcome.png
│   ├── account.png
│   ├── home.png
│   ├── add-item.png
│   ├── location.png
│   ├── search.png
│   ├── item-details.png
│   └── settings.png
│
├── .gitignore
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── README.md
└── settings.gradle.kts
```

---

## 📦 Download the App

The APK can be distributed through **GitHub Releases**.

### Latest Version

👉 **[Download APK](../../releases/latest)**

The APK can be installed directly on an Android device for testing.

### Installation

1. Open the Releases page.
2. Download the latest `.apk` file.
3. Transfer it to your Android device if necessary.
4. Open the APK.
5. Allow installation from the requested source if Android asks for permission.
6. Install the application.
7. Open **Where Did I Keep It?**
8. Create an account and start saving your belongings.

> The APK available through GitHub Releases is intended for demonstration and testing.

---

## 💻 Run the Project Locally

### Requirements

* Android Studio
* JDK 17
* Android SDK
* Android device or Android Emulator
* USB debugging enabled when using a physical device

### Clone the Repository

```bash
git clone https://github.com/KoletiSankeerthana/Find_Item.git
```

Then:

```bash
cd Find_Item
```

Open the project in **Android Studio** and allow Gradle to sync.

---

## 🔨 Build the APK

On Windows:

```bash
.\gradlew.bat assembleDebug
```

The generated debug APK will be located at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

---

## ▶️ Run on Android Device

1. Open the project in Android Studio.
2. Connect an Android device.
3. Enable USB debugging.
4. Select the connected device.
5. Click **Run** in Android Studio.

The application will be installed directly on the device.

---

## 🧪 Testing

The application has been tested on a physical Android device.

---

## 📚 Android Concepts Demonstrated

This project demonstrates practical experience with:

* Kotlin
* Jetpack Compose
* Material 3
* MVVM architecture
* Repository pattern
* Room Database
* DAO
* Coroutines
* Flow
* Navigation
* Runtime permissions
* Camera integration
* Android Photo Picker
* Local file storage
* Image loading
* Form validation
* Search functionality
* Multi-account data management
* Offline-first architecture
* Physical-device testing

---

## 🔗 Repository

**GitHub:**
[https://github.com/KoletiSankeerthana/Find_Item](https://github.com/KoletiSankeerthana/Find_Item)

---

## 👨‍💻 Author

### Koleti Sankeerthana

GitHub:
[https://github.com/KoletiSankeerthana](https://github.com/KoletiSankeerthana)

---
