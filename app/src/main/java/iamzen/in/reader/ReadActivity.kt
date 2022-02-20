package iamzen.`in`.reader

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import kotlinx.android.synthetic.main.activity_read.*

const val ARTICLE_LINK_READ = "articleLinkRead"
private const val TAG = "ReadActivity"
class ReadActivity : AppCompatActivity(), View.OnTouchListener {

    private lateinit var actionBar: ActionBar
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        actionBar = supportActionBar!!
        actionBar.hide()

        val window: Window = this.window
        window.statusBarColor = ContextCompat.getColor(this,R.color.white)

        scrollView.setOnTouchListener(this)

        val url = intent.getStringExtra(ARTICLE_LINK_READ)
        if (url != null) {
            Log.d(TAG,"url is not null")
            articleShow.settings.javaScriptEnabled = true
            articleShow.loadUrl(url)
        }

        scrollView.viewTreeObserver.addOnScrollChangedListener {


        }







    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return false
    }
}