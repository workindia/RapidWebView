package `in`.workindia.rapidwebview.events

interface CustomJSEvent {
    /**
     * Generates the JavaScript event to be sent to the WebView.
     *
     * @return JavaScript event as a String.
     */
    fun generateJavaScript(): String
}
