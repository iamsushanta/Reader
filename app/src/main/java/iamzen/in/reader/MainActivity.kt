package iamzen.`in`.reader

import android.Manifest
import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import iamzen.`in`.reader.daos.UserArticleSave
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.concurrent.Executors


private const val TAG = "MainActivity"

@DelicateCoroutinesApi
class MainActivity() : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var homeScreen:HomeScreen
    private lateinit var loveScreen:LoveUser
    private lateinit var noteScreen:UserNoteScreen
    private lateinit var addFolderScreen:AddFolderScreen

    var PERMISSION_ALL = 1
    var PERMISSIONS = arrayOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,

        )

    private lateinit var actionBar: ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = intent
        val action = intent.action
        val type = intent.type
        mAuth = FirebaseAuth.getInstance()
        val window: Window = this.window

       actionBar = supportActionBar!!

        actionBar.title = "Home"
        window.statusBarColor = ContextCompat.getColor(this,R.color.navigationBackGroundColor)

        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                handleSendText(intent) // Handle text being sent
            }
        }

        if (!hasPermissions(this, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


        homeScreen = HomeScreen()
         loveScreen = LoveUser()
         noteScreen = UserNoteScreen()
         addFolderScreen = AddFolderScreen()


        setCurrentFragment(homeScreen)


        navigation.setOnNavigationItemSelectedListener{ menuItem ->
            Log.d(TAG, "bottomNavigationView is clicked")
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    actionBar.title = "Home"
                    Log.d(TAG, "bottomNavigationView is clicked home button")
                    setCurrentFragment(homeScreen)

                }
                R.id.nav_love -> {

                    actionBar.title = "Love"
                    Log.d(TAG,"love screen is come action bar is $actionBar")
                    Log.d(TAG, "bottomNavigationView is clicked love button")
                    setCurrentFragment(loveScreen)

                }

                R.id.nav_note -> {
                    actionBar.title = "Note"
                    Log.d(TAG, "bottomNavigationView is clicked note button ")
                    setCurrentFragment(noteScreen)
                }
                R.id.nav_addFolder -> {
                    actionBar.title = "Add Folder"
                    Log.d(TAG,"BottomNavigationView is clicked addFolder screen open")
                    setCurrentFragment(addFolderScreen)
                }

            }
            true
        }



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG, "onCreate Option menu is called")
        menuInflater.inflate(R.menu.top_app_bar,menu)
        val searItem:MenuItem = menu.findItem(R.id.Search)
//        val searchView:SearchView = MenuItemCompat.getActionView(searItem) as SearchView

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.Search).actionView as SearchView
        val searchableInfo = searchManager.getSearchableInfo(componentName)
        searchView.setSearchableInfo(searchableInfo)

        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    homeScreen.searchingUser(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG,"onQueryTextChange is called")
                if(newText != null){
                    homeScreen.searchingUser(newText)
                }
                return true
            }

        })


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG,"hello option Item Selected is called")
        when(item.itemId){

            R.id.Setting -> {
                Log.d(TAG,"hello Setting item is clicked")
                return true
            }
            R.id.nav_home ->{
                Log.d(TAG,"hello Home menu is tab")
                return true
            }
            else ->{
               return super.onOptionsItemSelected(item)
            }
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

//            val userArticle = UserAddLink(firebaseUser.uid,"","",userLink)
            val userArticleSave = UserArticleSave(firebaseUser.uid)

            Log.d(TAG,"userArticle Save is start and AddUserLink is tab ")
            userArticleSave.addUserLink(userLink,firebaseUser.uid,this)
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

    fun hasPermissions(context: Context, vararg permissions:String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }





}




