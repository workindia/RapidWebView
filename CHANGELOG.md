# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),

## [2.1.0] - 2026-01-20
### Added
- Compatibility for Android 15
- Latest build tools and dependencies
- Edge-to-edge display requirements

### Breaking Changes
- Minimum Java version requirement increased to Java 11
- Requires Android Gradle Plugin 8.8.0 or higher
- ViewBinding migration may require updates in consuming applications

## [2.0.0] - 2025-06-30
### Changed
- Updated the DownloadCompletionEvent to fire an event with the status set to Failure if the download fails.
- dispatchDownloadCompletionEvent now uses a modified function signature that is not backward compatible.

## [1.3.0] - 2024-10-03
### Added
- Upgraded library to API 34
- Added downloadFileLocally & downloadFileLocallyAndOpenIntent Functionality to RapidWebViewJSInterface

### Changed
- Updated the ShowNotification function to handle "POST_NOTIFICATIONS" permission on Android 13.0+ devices
- Updated uploadFile function to handle Media Permissions on Android 13.0+ devices and updated corresponding function signatures 
- Removed a few default permission declaration from library manifest
- Updated documentation site according to the changes made

## [1.2.1] - 2022-11-11
### Fixed
- CrashFix
  - Pending Intent Crash Fixed for Android 12 and above
- Whatsapp Image Fix
  - Error while sharing images to whatsapp

## [1.2.0] - 2022-09-19
### Changed
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
