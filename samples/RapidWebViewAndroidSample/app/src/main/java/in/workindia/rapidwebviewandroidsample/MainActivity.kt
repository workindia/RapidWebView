package `in`.workindia.rapidwebviewandroidsample

import `in`.workindia.rapidwebview.assetcache.RapidAssetCacheDownloader
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RapidAssetCacheDownloader.initialise(
            "https://rapid-app-demo.vercel.app/_next/static/assets-manifest.json"
        )
        navigationButton.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            startActivity(intent)
        }
    }
}