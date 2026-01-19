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
        
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { toolbar, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            
            toolbar.updatePadding(top = systemBars.top)
            
            insets
        }
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.layoutRoot) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            
            view.updatePadding(bottom = systemBars.bottom)
            
            insets
        }

        RapidAssetCacheDownloader.initialise(
            "https://rapid-web-view.netlify.app/_next/static/assets-manifest.json",
            2
        )

        binding.navigationButton.setOnClickListener {
            startActivity(Intent(this, WebViewActivity::class.java))
        }
    }
}