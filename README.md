# Android Utility Apps Collection

This repository contains a collection of four Android applications developed using **Android Studio, Java, and XML**.  
These apps were created to practice core Android development concepts such as activities, intents, layouts, media handling, sensors, camera usage, gallery access, and SharedPreferences.

## Apps Included

### 1. Currency Converter

A simple currency converter app that allows users to convert between different currencies.

**Features:**
- Converts values between multiple currencies
- Clean and simple user interface
- Supports light and dark theme toggle
- Uses SharedPreferences to save theme preference
- Built using Java and XML

**Concepts Used:**
- Activities
- UI components
- Input handling
- SharedPreferences
- Theme switching

---

### 2. Vintage Video Audio Player

A media player application with a vintage-style user interface.

**Features:**
- Play audio/video from local files
- Play media from a URL
- Play, pause, stop, and restart controls
- Retro-inspired UI design
- Uses Android media playback components

**Concepts Used:**
- VideoView
- Media playback
- File selection
- URL-based media loading
- Button controls
- Custom UI design

---

### 3. Sensor App

An Android app that displays real-time sensor data from the device.

**Features:**
- Displays accelerometer readings
- Displays light sensor values
- Displays proximity sensor data
- Real-time sensor updates
- Simple and readable interface

**Concepts Used:**
- SensorManager
- Accelerometer sensor
- Light sensor
- Proximity sensor
- Real-time data updates
- Android lifecycle handling

---

### 4. Cameras Photos App

A camera and gallery-based Android application that allows users to capture and manage photos.

**Features:**
- Capture photos using the device camera
- Select a folder for storing images
- Display captured images in a gallery
- View image details
- Delete selected images
- Uses Android Storage Access Framework

**Concepts Used:**
- Camera intent
- Storage Access Framework
- DocumentFile
- RecyclerView
- Image handling
- URI permissions
- Gallery display

---

## Technologies Used

- Java
- XML
- Android Studio
- Android SDK
- RecyclerView
- SharedPreferences
- SensorManager
- VideoView
- Storage Access Framework

---

## Repository Structure

```txt
Android-Utility-Apps/
│
├── Cameras_Photos_app/
│   └── Camera and gallery based Android app
│
├── Currency_Convertor/
│   └── Currency converter Android app
│
├── SensorApp/
│   └── Sensor data Android app
│
├── VintageVideoAudioPlayer/
│   └── Audio and video player Android app
│
└── .gitignore
```

# Learning Outcomes

Through these projects, I learned and practiced:

- Building Android apps using Java and XML
- Designing layouts using XML
- Handling multiple activities and intents
- Working with device sensors
- Using media playback features
- Capturing and displaying images
- Managing files and permissions
- Using RecyclerView for gallery-style layouts
- Saving small user preferences using SharedPreferences
- Debugging Android apps in Android Studio
# How to Run
Clone the repository:
``` git clone https://github.com/YugSaini29/Exploring-Android-Development-Four-Apps ```
- Open the required app folder in Android Studio.
- Let Gradle sync completely.
- Connect an Android device or start an emulator.
- Click Run to install and test the app.
