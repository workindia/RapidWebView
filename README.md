<p align="center"><a href="https://github.com/workindia/RapidWebView"><img src="https://github.com/workindia/RapidWebView/blob/master/images/RapidWebViewLogo.png?raw=true"></a></p> 
<h2 align="center"><b>RapidWebView SDK</b></h2>
<h4 align="center">An Android library which utilises local caching to offer near native experience over WebView.</h4>

<p align="center">
<a href="https://jitpack.io/#workindia/RapidWebView" alt="Jitpack"><img src="https://jitpack.io/v/workindia/RapidWebView.svg"></a>
<a href="https://github.com/workindia/RapidWebView/issues" alt="GitHub release"><img src="https://img.shields.io/github/issues/workindia/RapidWebView" ></a>
<a href="https://github.com/workindia/RapidWebView" alt="GitHub stars"><img src="https://img.shields.io/github/stars/workindia/RapidWebView" ></a>
<a href="/LICENSE" alt="License: MIT"><img src="https://img.shields.io/badge/License-MIT-orange.svg"></a>
<a href="https://github.com/workindia/RapidWebView" alt="Build Status"><img src="https://img.shields.io/github/forks/workindia/RapidWebView"></a>
</p>
<hr>

<h3 align="center">If you like our work you can show support by staring ⭐ this repository 👏</h4>

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
This guide will explain how to integrate this SDK into your application.

### Depedency
Add Jitpack to your root `build.gradle`:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

And in your app's `build.gradle` add dependency:
```
dependencies {
    ...
    implementation 'com.github.workindia:RapidWebView:[latest-version]'
}
```

### Initialising RapidClient   
Initialise RapidClient in `StartApplication` of your application:    

```java
import in.workindia.rapidwebview.assetcache.RapidAssetCacheDownloader;

...
RapidClient.createInstance(this);
```

### Initialising RapidAssetCacheDownloader
Initialise the asset cache downloader by passing it url to asset-manifest.json. Asset cache initialisation 
needs to be done soon after the application opens, before the webview is loaded. The operation does not run on UI 
thread and will not impact the users experience.    
<!-- TODO: Insert docs link which explains the asset-manifest.json concept -->

```java
import in.workindia.rapidwebview.assetcache.RapidAssetCacheDownloader;

...

RapidAssetCacheDownloader.initialise(
    "https://rapid-app-demo.vercel.app/_next/static/assets-manifest.json"
)
```

### Setting up a webview with RapidWebViewClient
Add RapidWebViewClient to a webview in your app. This client intercepts requests made by the webview and 
responds with a local asset copy if it's available.    

```java
import in.workindia.rapidwebview.RapidWebViewClient;

...
private WebView mWebView;
...

// Initialise mWebView and add WebSettings of your choice

mWebView.setWebViewClient(new RapidWebViewClient());
mWebView.loadUrl("https://rapid-app-demo.vercel.app/");
```

### Adding the RapidWebViewJSInterface
We have included a Javascript interface with a few generic use-cases which interacts with native android functionality.
<!-- TODO: Insert docs link which lists different functions of the interface -->

```java
import in.workindia.rapidwebview.RapidWebViewJSInterface;

...
mWebView.addJavascriptInterface(
    new RapidWebViewJSInterface(this, this, mWebView),
    "app"
)
```


<hr>   

## Contribution

We're looking to improve this project, open source contribution is encouraged. WorkIndia team will be reviewing the pull requests.  
[Pull requests](https://github.com/workindia/RapidWebView/pulls)
