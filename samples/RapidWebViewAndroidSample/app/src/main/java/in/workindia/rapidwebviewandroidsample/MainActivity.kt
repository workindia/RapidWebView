package `in`.workindia.rapidwebviewandroidsample

import `in`.workindia.rapidwebview.assetcache.RapidAssetCacheDownloader
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import `in`.workindia.rapidwebviewandroidsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // MANDATORY for Android 15 - Enable Edge-to-Edge
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        
        // Modern View Binding approach
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up the custom toolbar as action bar
        setSupportActionBar(binding.toolbar)
        
        // Handle window insets to extend toolbar into status bar
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { toolbar, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            
            // Add top padding to toolbar to account for status bar height
            toolbar.updatePadding(top = systemBars.top)
            
            insets
        }
        
        // Handle insets for the main content area
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            
            // Only apply bottom padding for navigation bar
            view.updatePadding(bottom = systemBars.bottom)
            
            insets
        }

        try {
            RapidAssetCacheDownloader.initialise(
                "https://rapid-web-view.netlify.app/_next/static/assets-manifest.json",
                2
            )
        } catch (e: Exception) {
            TODO("Not yet implemented")
        } finally {
        }

        binding.navigationButton.setOnClickListener {
            startActivity(Intent(this, WebViewActivity::class.java))
        }
    }
}