# 📄 Resume Generator App

A **Jetpack Compose** Android application that dynamically generates and displays professional resumes by fetching data from an **Express.js REST API**. Users can view resume details such as personal information, skills, projects, and summary in a modern, customizable UI.

---

## 🚀 Features

* Fetch resume details from [Express.js Resume API](https://expressjs-api-resume-random.onrender.com/resume?name=insert-your-name-here)
* Displays candidate name, contact info, skills, projects, and summary
* Built with **Jetpack Compose** for a fully declarative UI
* **MVVM architecture** with `ViewModel`, `Flow`, and coroutines for state management
* **Customization Controls**

  * Change font size
  * Change font color
  * Change background color
* Location integration: shows user’s **latitude & longitude** with runtime permission handling

---

## 🛠️ Tech Stack

* **Kotlin**
* **Jetpack Compose**
* **Retrofit2** (API calls)
* **Kotlin Coroutines & Flow** (asynchronous data + state management)
* **MVVM architecture**
* **Accompanist Permissions** (runtime permissions)
* **Material3 Components** (UI design)

---

## 📦 API Usage

This project consumes the following API:

```
GET https://expressjs-api-resume-random.onrender.com/resume?name=<candidate-name>
```

Example:

```
https://expressjs-api-resume-random.onrender.com/resume?name=Rahul
```

---

## 📸 Screenshots

(Add screenshots here once you run the app on your device/emulator)

---

## 🧑‍💻 How to Run

1. Clone this repository:

   ```bash
   git clone https://github.com/<your-username>/<repo-name>.git
   ```
2. Open in **Android Studio**.
3. Sync Gradle and run on an emulator/physical device.
4. Ensure internet permission is added in `AndroidManifest.xml`:

   ```xml
   <uses-permission android:name="android.permission.INTERNET" />
   ```

---

## 📌 Future Enhancements

* Add support for multiple resumes
* Export resume as PDF
* Offline mode with local caching
* Better theming and typography options

---

## 🤝 Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you’d like to change.

---

## 📄 License

This project is licensed under the MIT License.
