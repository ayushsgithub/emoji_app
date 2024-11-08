# Emojicon

**Emojicon** is an open-source Android app that enables users to broadcast their emoji-only status with ease! Using Google One Tap sign-in, users can instantly log in with their Google account and set fun, emoji-based statuses that friends can view. This project integrates several Firebase services, such as Authentication, and Firestore, to provide a seamless, secure, and interactive experience.

![screenshot](https://github.com/ayushsgithub/emoji_app/blob/main/app/images/Screenshot%202024-11-08%20153102.png?raw=true)

## Features

- **Emoji-Only Status Updates**: Set statuses with emojis for fun and expressive communication.
- **Google One Tap Sign-In**: Quick and easy authentication with Google accounts.
- **Logout Option**: Secure logout functionality with Firebase.
- **Firestore Database**: Real-time storage and retrieval of user statuses.
- **Open-Source**: Fork, modify, and contribute to this project!

---

## App Architecture

This project uses Firebase services to streamline user experience and handle backend tasks:

- **Authentication**: Manages user accounts with Google One Tap sign-in and logout.
- **Firestore**: Stores user data, including emoji status updates, in real-time.

## Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/ayushsgithub/emoji_app.git
   ```

2. **Open the project in Android Studio**:

   - Open Android Studio and go to **File > Open**.
   - Select the directory where you cloned the project.

3. **Configure Firebase**:
   - Create a Firebase project in the [Firebase Console](https://console.firebase.google.com/).
   - Enable **Google One Tap Authentication** and **Firestore**.
   - Download `google-services.json` from Firebase and place it in the `app` directory of your project.

4. **Run the app**:
   - Connect your Android device or start an emulator.
   - Select **Run > Run 'app'** to build and install.

---

## How It Works

- **Sign In with Google One Tap**: Users can sign in seamlessly using Firebase Authentication’s Google One Tap, making the login experience quick and easy.
- **Set Status**: Users can set their emoji-only status, stored in Firebase Firestore for real-time access.
- **Logout**: Users can securely log out, with Firebase handling session management.

## Video Demo

**TO_BE_ADDED_SOON** _Add a youtube video of the sign-in flow, emoji status setting, and logout functionality to provide a visual overview._

## Relevant Links

- **YouTube Tutorials**: **TO_BE_ADDED_SOON** Step-by-step guide on building this app on YOUTUBE
- **Firebase Documentation**: [Firebase Docs](https://firebase.google.com/docs)

## Contributing

We welcome contributions to EmojiStatus! To contribute:

1. Fork the repository and create a new branch (`git checkout -b feature/AmazingFeature`).
2. Make your changes and test thoroughly.
3. Commit your changes (`git commit -m 'Add AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a pull request.

---

## License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](http://www.apache.org/licenses/LICENSE-2.0) file for more details.
