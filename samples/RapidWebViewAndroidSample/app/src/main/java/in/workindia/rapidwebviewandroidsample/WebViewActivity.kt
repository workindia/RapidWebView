package `in`.workindia.rapidwebviewandroidsample

import `in`.workindia.rapidwebview.RapidWebViewClient
import `in`.workindia.rapidwebview.RapidWebViewJSInterface
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

import `in`.workindia.rapidwebviewandroidsample.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityWebViewBinding
    
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable Edge-to-Edge for consistent behavior
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        // Modern View Binding approach
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up the custom toolbar as action bar
        setSupportActionBar(binding.toolbar)
        
        // Enable back button in toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        
        // Handle window insets to extend toolbar into status bar
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { toolbar, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())

            // Add top padding to toolbar to account for status bar height
            toolbar.updatePadding(top = systemBars.top)

            insets
        }
        
        // Handle insets for the main content area
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())

            // Only apply bottom padding for navigation bar
            view.updatePadding(bottom = systemBars.bottom)

            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.rapidWebView) { view, insets ->
            val displayCutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout())

            // Only apply bottom padding for navigation bar
            view.updatePadding(left = displayCutout.left, right = displayCutout.right)

            insets
        }
        
        WebView.setWebContentsDebuggingEnabled(true)

        val rapidWebViewClient = RapidWebViewClient()
        binding.rapidWebView.webViewClient = rapidWebViewClient

        binding.rapidWebView.addJavascriptInterface(
            RapidWebViewJSInterface(this, this, binding.rapidWebView),
            "app"
        )

        val webSettings = binding.rapidWebView.settings
        webSettings.javaScriptEnabled = true

        binding.rapidWebView.loadUrl("https://rapid-web-view.netlify.app/examples/js-demo")
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}