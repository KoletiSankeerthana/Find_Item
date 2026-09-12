Yes bro — the issue is that your current README is being rendered inside a **code block**, so GitHub is showing the `#`, `##`, `---`, etc. as plain text instead of interpreting them as Markdown.

You should paste the following **directly into `README.md`**, making sure you do **not** put the entire README inside `markdown ... `.

I’ve also structured it so you can add your app screenshots professionally.

---

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
  <img src="screenshots/welcome.png" width="250"/>
</p>

### 👤 Account Selection

<p align="center">
  <img src="screenshots/account.png" width="250"/>
</p>

### 🏠 Home Screen

<p align="center">
  <img src="screenshots/home.png" width="250"/>
</p>

### ➕ Add Item

<p align="center">
  <img src="screenshots/add-item.png" width="250"/>
</p>

### 📍 Custom Location

<p align="center">
  <img src="screenshots/location.png" width="250"/>
</p>

### 🔎 Search

<p align="center">
  <img src="screenshots/search.png" width="250"/>
</p>

### 📦 Item Details

<p align="center">
  <img src="screenshots/item-details.png" width="250"/>
</p>

### ⚙️ Settings

<p align="center">
  <img src="screenshots/settings.png" width="250"/>
</p>

> **Note:** Replace the filenames above with the exact names of the screenshots you upload to the `screenshots` folder.

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

↓

**2. Add an item**

↓

**3. Add a photo**

↓

**4. Enter the item name**

↓

**5. Select or create a custom location**

↓

**6. Add optional notes**

↓

**7. Save the item**

↓

**8. Search for it whenever needed**

---

## 📍 Custom Hierarchical Locations

One of the key features of the application is its flexible location system.

Users can create locations with multiple levels based on where they actually store their belongings.

### Example 1

`Home → Kitchen`

### Example 2

`Home → Kitchen → Cabinet`

### Example 3

`Home → Kitchen → Cabinet → 3rd Shelf`

### Example 4

`Office → Conference Room → Cabinet 2 → Top Shelf`

### Example 5

`Car → Boot → Left Side`

There is no need to follow a fixed predefined location structure.

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

## 🔒 Privacy

Privacy is one of the main design goals of the application.

The core application does not require:

* ❌ Firebase
* ❌ Cloud database
* ❌ Online authentication
* ❌ Paid APIs
* ❌ External backend

The user's saved item information and photos remain stored locally on the device.

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

### Tested Functionality

* ✅ Application build
* ✅ Application installation
* ✅ Application launch
* ✅ Welcome flow
* ✅ Account creation
* ✅ Existing account selection
* ✅ Multiple accounts
* ✅ Duplicate account validation
* ✅ Home screen
* ✅ Add Item
* ✅ Camera
* ✅ Photo Picker
* ✅ Photo preview
* ✅ Photo replacement
* ✅ Photo removal
* ✅ Item validation
* ✅ Custom locations
* ✅ Multi-level locations
* ✅ Location editing
* ✅ Search
* ✅ Location search
* ✅ Notes search
* ✅ Search suggestions
* ✅ Recent searches
* ✅ Sorting and filtering
* ✅ Favorites / pinning
* ✅ Item details
* ✅ Item editing
* ✅ Item deletion
* ✅ Room persistence
* ✅ Application restart persistence
* ✅ Settings
* ✅ About screen

---

## 🧠 Image Recognition

An experimental on-device image recognition feature was explored during development.

The goal was to automatically identify objects in item photos and provide suggestions.

However, general-purpose image recognition models did not provide sufficiently reliable results for the application's intended use cases.

Therefore, image recognition is currently considered **experimental and paused**.

The core application does **not depend on image recognition**.

---

## 🎨 Design Goals

The application was designed around the following principles:

### Simple

Users should be able to save an item quickly.

### Fast

Finding a saved item should take only a few seconds.

### Private

Personal belongings and their locations should remain local.

### Flexible

Users should be able to define locations based on their actual storage system.

### Offline

The core application should remain functional without internet connectivity.

### User Friendly

The interface uses a clean Material 3 design with a soft pastel visual style.

---

## 🚧 Current Status

### ✅ Completed

* Android project setup
* Jetpack Compose UI
* Material 3 design
* Welcome experience
* Multiple local accounts
* Account validation
* Duplicate account protection
* Home screen
* Add Item workflow
* Camera integration
* Photo Picker
* Local photo storage
* Room database
* Offline persistence
* Custom hierarchical locations
* Search
* Search suggestions
* Recent searches
* Sorting
* Filtering
* Favorites / pinning
* Item details
* Item editing
* Item deletion
* Settings
* About
* Physical-device testing

### 🧪 Experimental

* On-device image recognition

### ⏸️ Paused

* Advanced automatic image recognition due to recognition accuracy limitations

---

## 🗺️ Future Roadmap

Possible future improvements include:

* ⏰ Item reminders
* 🏷️ Categories
* 🗺️ Location Explorer
* 🖼️ Grid/List view
* 📤 Share item information
* 🔐 App lock
* 📊 Usage statistics
* ⚠️ Duplicate item detection
* 📍 Advanced location navigation
* 🔎 More advanced search capabilities
* ⚡ Performance optimization
* ♿ Accessibility improvements
* 🧪 Expanded automated testing

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

## 🌟 Project Highlights

### 📍 Flexible Location System

Create detailed locations instead of being restricted to predefined options.

### 📸 Real Photos

Attach actual photos of belongings for easier identification.

### 🔎 Powerful Search

Find items through their name, location, or notes.

### 👤 Multiple Users

Maintain separate local collections for different users.

### 🔐 Privacy Focused

No cloud backend is required for the core application.

### 💾 Persistent Storage

Saved information remains available after closing and reopening the application.

### 📱 Real Android Application

Built and tested as a native Android application using Kotlin and Jetpack Compose.

---

## 📊 Example

Suppose you store your headphones in a drawer.

You can save:

```text
📦 Item
Headphones

📍 Location
Home → Bedroom → Study Table → Top Drawer

📝 Notes
Kept inside the black pouch.
```

Later, search:

```text
Headphones
```

and the application shows:

```text
📦 Headphones

📍 Home → Bedroom → Study Table → Top Drawer

📝 Kept inside the black pouch.
```

No need to remember where you put them. ✨

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

## ⭐ Support

If you find this project interesting:

* ⭐ Star the repository
* 🍴 Fork the project
* 🐛 Report bugs
* 💡 Suggest improvements
* 🤝 Contribute ideas

---

## 📄 License

This project currently does not specify an open-source license.

A suitable license can be added in the future if the project is released as an open-source project.

---

## 📌 Summary

**Where Did I Keep It?** is an offline-first Android application that helps users remember where they stored their belongings.

### The idea is simple:

**📸 Save it → 📍 Remember where → 🔎 Find it later**

> **Remember where. Find when. ✨**

````

### ⚠️ Very important when you paste it

Your current screenshot shows this:

```text
# 📍 Where Did I Keep It?
````

appearing literally on the page.

That usually means the README content itself has been pasted **inside a Markdown code block**.

Make sure your GitHub `README.md` starts exactly like this:

```text
# 📍 Where Did I Keep It?

### Remember where. Find when. ✨
```

**Do not put three backticks before the first `#` or after the last line.**

### 📸 For your screenshots

Create this folder in your GitHub repository:

```text
screenshots
```

Then upload your actual app screenshots:

```text
screenshots/
├── welcome.png
├── account.png
├── home.png
├── add-item.png
├── location.png
├── search.png
├── item-details.png
└── settings.png
```

Once those files are uploaded, the README will display them automatically.

For the **APK**, I'd keep it in **GitHub Releases rather than inside the repository**. Then this line:

```markdown
[Download APK](../../releases/latest)
```

will take users directly to your latest release.

This gives your GitHub project the professional flow:

**README → Features → Screenshots → Tech Stack → Architecture → Source Code → Download APK → Testing → Roadmap**.
