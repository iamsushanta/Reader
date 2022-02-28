package iamzen.`in`.reader

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import iamzen.`in`.reader.daos.ReadDao
import iamzen.`in`.reader.daos.checkArticleIsLike
import iamzen.`in`.reader.model.UserAddLink
import kotlinx.android.synthetic.main.activity_read.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await


const val ARTICLE_LINK_READ = "articleLinkRead"
const val ARTICLE_LINK_ID = "articleLinkId"
private const val TAG = "ReadActivity"

@DelicateCoroutinesApi
class ReadActivity : AppCompatActivity(), View.OnTouchListener {

    private lateinit var actionBar: ActionBar
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mReadDao: ReadDao

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        mAuth = FirebaseAuth.getInstance()
        actionBar = supportActionBar!!
        actionBar.hide()
        mReadDao = ReadDao(mAuth.uid!!)

        val window: Window = this.window
        window.statusBarColor = ContextCompat.getColor(this,R.color.white)

        scrollView.setOnTouchListener(this)

        val url = intent.getStringExtra(ARTICLE_LINK_READ)!!
        val urlId = intent.getStringExtra(ARTICLE_LINK_ID)!!


//         first time user come to this screen that time check user article is like and not
       getReadLike(urlId)

        readLove.setOnClickListener {
            val checkIsLikeArticle = checkArticleIsLike
            userLikeWork(urlId,checkIsLikeArticle)
        } // readLove is end

        if (url != null) {
            Log.d(TAG,"url is not null")
            articleShow.settings.javaScriptEnabled = true

//            articleShow.loadUrl(url)
        }

        scrollView.viewTreeObserver.addOnScrollChangedListener {


        }




    }

    private fun userLikeWork(urlId: String,checkIsLike: Boolean){
        if(checkIsLike){
            Log.d(TAG,"checkIsLikeArticle is true")
            readLove.setImageResource(R.drawable.ic_read_dont_love)
            mReadDao.setLike(urlId)
        } else {
            Log.d(TAG,"checkIsLikeArticle is false")
            readLove.setImageResource(R.drawable.ic_love)
            mReadDao.setLike(urlId)
        }
    }

    private fun getReadLike(urlId: String){
        GlobalScope.launch (Dispatchers.IO){
            val user = mReadDao.getUserId(urlId).await().toObject(UserAddLink::class.java)
            withContext(Dispatchers.Main){
                if(user?.likeArticle!!){
                    readLove.setImageResource(R.drawable.ic_love)
                } else {
                    readLove.setImageResource(R.drawable.ic_read_dont_love)
                }
            }

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return false
    }



}