package iamzen.`in`.reader

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import iamzen.`in`.reader.daos.UserArticleSave
import iamzen.`in`.reader.model.UserAddLink
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.concurrent.Executors


private const val TAG = "MainActivity"

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = intent
        val action = intent.action
        val type = intent.type
        mAuth = FirebaseAuth.getInstance()
        val window: Window = this.window

        window.statusBarColor = ContextCompat.getColor(this,R.color.navigationBackGroundColor)

        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                handleSendText(intent) // Handle text being sent
            }
        }



        val  homeScreen = HomeScreen()
        val loveScreen = LoveUser()
        val noteScreen = UserNoteScreen()
        val addFolderScreen = AddFolderScreen()


        setCurrentFragment(homeScreen)







        navigation.setOnNavigationItemSelectedListener{ menuItem ->
            Log.d(TAG, "bottomNavigationView is clicked")
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    Log.d(TAG, "bottomNavigationView is clicked home button")
                    setCurrentFragment(homeScreen)

                }
                R.id.nav_love -> {
                    Log.d(TAG, "bottomNavigationView is clicked love button")
                    setCurrentFragment(loveScreen)

                }

                R.id.nav_note -> {
                    Log.d(TAG, "bottomNavigationView is clicked note button ")
                    setCurrentFragment(noteScreen)
                }
                R.id.nav_addFolder -> {
                    Log.d(TAG,"BottomNavigationView is clicked addFolder screen open")
                    setCurrentFragment(addFolderScreen)
                }

            }
            true
        }



    }



    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container,fragment)
            commit()
        }


    private fun handleSendText(intent: Intent) {
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (sharedText != null) {
            // Update UI to reflect text being shared
            Log.d(TAG,"url is $sharedText")
            doMyTask(sharedText)



            val currentUser = mAuth.currentUser
            addLink(currentUser,sharedText)

            finish()

        }
    }

    @DelicateCoroutinesApi
    private fun addLink(firebaseUser: FirebaseUser?, userLink:String){
        if(firebaseUser != null){
            Log.d(TAG, "addLink is called firebase user is not null")

            val userArticle = UserAddLink(firebaseUser.uid,"","",userLink)
            val userArticleSave = UserArticleSave(firebaseUser.uid)

            Log.d(TAG,"userArticle Save is start and AddUserLink is tab ")
            userArticleSave.addUserLink(userLink,userArticle)
        }

    }


    // do MY TASK is not ready this time
    @SuppressLint("SetJavaScriptEnabled")
    fun doMyTask(vararg url:String){

        val myExecutor = Executors.newSingleThreadExecutor()
        val myHandler = Handler(Looper.getMainLooper())

//        myExecutor.execute {
//            var result = URL(url[0]).readText()
//            var getContent = URL(url[0])
//            var content = getContent.content
//            Log.d(TAG,"content is that $content")
//            myHandler.post {
//                Log.d(TAG,"result is $result")
//
//
//            }
//        }
    }




}


