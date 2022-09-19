# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),

## [Unreleased]
- Changed File Provider and Authorities
  - changed file provider and authorities for RapidWebView SDK
  - Fixed Image Sharing Intent
- Migration to Android 13
  - Changed CompileSDK and TargetSDK to 33
  - Changed Notification Pending Intents for new API android

***

## [1.1.0] - 2022-07-01
### Changed
- Changed UploadFile function in JavaScriptInterface
    - removed callback as a parameter
    - added upload type as a parameter (possible types : PUT and POST)
    - a callback will be generated everytime once there is a failure or a success
    - callback can be listened from javascript using event listener under the name "rapid-web-view-upload-listener"
    - under callback the javascript client will get object with following attribute { detail: { 'status' : "success",'uploadUrl' : '$uploadUrl','uploadFileName' : '$fileName' } }

- Changed Permission function in JavaScriptInterface
    - removed callback as a parameter
    - added permission type as a parameter (possible types : PUT and POST)
    - a callback will be generated everytime once there is a failure or a success
    - callback can be listened from javascript using event listener under the name "rapid-web-view-permission-listener"
    - under callback the javascript client will get object with following attribute { detail: { 'status' : "failure" } }

- Changed method to fetch file from Internal Storage

## [1.0.1] - 2022-02-22
### Changed
- Made RapidWebViewJSInterface an open class so that it can be overridden / extended

## [1.0.0] - 2022-02-17
### Added
- First published release
- Early beta implementation of RapidWebViewDownloader, RapidWebViewInterceptor & RapidWebViewJSInterface