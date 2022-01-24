package `in`.workindia.rapidwebviewandroidsample

import `in`.workindia.rapidwebview.RapidWebViewClient
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val rapidWebViewClient = RapidWebViewClient()
        rapidWebView.webViewClient = rapidWebViewClient
        WebView.setWebContentsDebuggingEnabled(true)
        rapidWebView.loadUrl("https://rapid-web-view.netlify.app/")
    }
}