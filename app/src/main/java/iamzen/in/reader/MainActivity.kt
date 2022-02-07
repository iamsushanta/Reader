package iamzen.`in`.reader

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val window: Window = this.window

        window.statusBarColor = ContextCompat.getColor(this,R.color.navigationBackGroundColor)

        val intent = intent
        val action = intent.action
        val type = intent.type
        val  homeScreen = HomeScreen()
        val loveScreen = LoveUser()
        val noteScreen = UserNoteScreen()
        val addFolderScreen = AddFolderScreen()

        setCurrentFragment(homeScreen)


        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                handleSendText(intent) // Handle text being sent
            }
        }


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
            Log.d(TAG, "handleSenText is called")
        }
    }


}