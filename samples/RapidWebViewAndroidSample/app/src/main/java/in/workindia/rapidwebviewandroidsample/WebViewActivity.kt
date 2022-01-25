package `in`.workindia.rapidwebviewandroidsample

import `in`.workindia.rapidwebview.RapidWebViewClient
import `in`.workindia.rapidwebview.RapidWebViewJSInterface
import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        WebView.setWebContentsDebuggingEnabled(true)

        val rapidWebViewClient = RapidWebViewClient()
        rapidWebView.webViewClient = rapidWebViewClient

        rapidWebView.addJavascriptInterface(
            RapidWebViewJSInterface(this, this, rapidWebView),
            "app"
        )

        val webSettings = rapidWebView.settings
        webSettings.javaScriptEnabled = true

        rapidWebView.loadUrl("https://rapid-web-view.netlify.app/examples/js-demo")
    }
}