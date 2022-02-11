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
<hr />

<h3 align="center">If you like our work you can show support by staring ⭐ this repository 👏</h4>

<hr />

<p align="center">Getting started and documentation on our website: <a href="https://rapid-web-view.netlify.app/">Link</a></p>

<hr />

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


<hr />   

## Contribution

We're looking to improve this project, open source contribution is encouraged. WorkIndia team will be reviewing the pull requests.  
[Pull requests](https://github.com/workindia/RapidWebView/pulls)
