<p align="center"><a href="https://github.com/workindia/RapidWebView"><img src="https://github.com/workindia/RapidWebView/blob/master/images/RapidWebViewLogo.png?raw=true"></a></p> 
<h2 align="center"><b>RapidWebView SDK</b></h2>
<h4 align="center">An Android library which utilises local caching to offer near native experience over WebView.</h4>

<p align="center">
<a href="https://jitpack.io/#workindia/RapidWebView" alt="Jitpack"><img src="https://jitpack.io/v/workindia/RapidWebView.svg"></a>
<a href="https://github.com/workindia/RapidWebView/issues" alt="GitHub release"><img src="https://img.shields.io/github/issues/workindia/RapidWebView" ></a>
<a href="https://github.com/workindia/RapidWebView" alt="GitHub stars"><img src="https://img.shields.io/github/stars/workindia/RapidWebView" ></a>
<a href="/LICENSE" alt="License: MIT"><img src="https://img.shields.io/badge/License-MIT-orange.svg"></a>
<a href="https://github.com/workindia/RapidWebView" alt="Forks"><img src="https://img.shields.io/github/forks/workindia/RapidWebView"></a>
</p>

***

<h3 align="center">If you like our work you can show support by staring ⭐ this repository 👏</h4>

***

<p align="center">Getting started and documentation on our website: <a href="https://rapid-web-view.netlify.app/">Link</a></p>

***

## Description

RapidWebView is a library for your Android application which allows you to load websites in a webview with a minimal TTI ([Time To Interactive](https://web.dev/interactive/)). It also provides a generic set of Javascript Interface functions which can let your website interact with some Android native functionalities.

### Features

- Asset cache - keeps a local copy of website assets provided by you during initialisation. Supports asset versioning as well.
- WebView request interceptor - intercepts requests made by webview and serves assets from local copy reducing the website "Time to interactive" time. 
- Javascript interface - a generic collection of javascript interface functions which can be used to interactive with native application

### Performance    
We enabled webview content debugging (`setWebContentsDebuggingEnabled(true)`) and used Lighthouse from Chrome DevTools to measure performance of
the pages loaded with and without RapidWebView SDK. The results can be seen below. A 67.2% improvement in TTI!   
![metrics](/images/Performance.png)

### Technologies used

- Kotlin
- Retrofit
- Android Architecture Components

## Getting started
Follow the guide [here](https://rapid-web-view.netlify.app/docs/getting-started)


***   

## Changelog
[CHANGELOG.md](https://github.com/workindia/RapidWebView/blob/master/CHANGELOG.md)

## Contribution

We're looking to improve this project, open source contribution is encouraged. WorkIndia team will be reviewing the pull requests.  
[Pull requests](https://github.com/workindia/RapidWebView/pulls)

# RapidWebView – Local Development Setup

This guide will help you set up the **RapidWebView** project locally to test and modify the library alongside the sample app.

## 📦 Prerequisites

- Android Studio **Koala** or later
- Git
- Node.js (for documentation/demo site updates)

---

## 🛠️ Steps to Set Up

### 1. Clone the Repository

```bash
git clone https://github.com/workindia/RapidWebView.git
```

---

### 2. Sync Dependencies

Open the project in **Android Studio Koala**. Ensure the Gradle version is compatible with this Android Studio version.

Use the **Gradle Wrapper** or upgrade the Gradle version if prompted by Android Studio.

---

### 3. Link the Sample App

Edit the `settings.gradle` file in the root directory and add the following:

```groovy
include ':app'
project(':app').projectDir = new File('./samples/RapidWebViewAndroidSample/app')
```

This includes the sample app module in the build.

---

### 4. Update Sample App Dependencies

In the  ./samples/RapidWebViewAndroidSample/app/build.gradle `build.gradle` file of the sample app module:

##### ❌ Comment out the existing GitHub dependency:
```groovy
// implementation 'com.github.workindia:rapidwebview:1.3.0'
```
##### ✅ Add this line to the dependencies block:
```groovy
implementation project(':RapidWebView')
```
This ensures the app uses the local version of the library for development and testing.

---

### 5. Modify & Test

- Make your changes in the `RapidWebView` library code.

- If needed, update the documentation demo site code:

  ```tsx
  samples/RapidWebViewSdkDocsSite/pages/examples/js-demo.tsx
  ```

- Run the sample app from Android Studio to see your changes reflected.

---

## ✅ You're All Set!

You're now ready to develop and test the `RapidWebView` library locally using the sample app.

Happy coding!
