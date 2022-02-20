package iamzen.`in`.reader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import iamzen.`in`.reader.daos.UserDao
import iamzen.`in`.reader.model.User
import kotlinx.android.synthetic.main.activity_first_sreen.*
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class FirstScreen : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    private lateinit var actionBar: ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_sreen)
        actionBar = supportActionBar!!
        actionBar.hide()

        val window: Window = this.window
        window.statusBarColor = ContextCompat.getColor(this,R.color.white)

        auth = FirebaseAuth.getInstance()

        first_screen_signup.setOnClickListener{
            goAnotherScreen(first_screen_signup)
        }
        first_screen_login.setOnClickListener{
            goAnotherScreen(first_screen_login)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun goAnotherScreen(view: View){
           if(view.id == first_screen_signup.id){
               val intent = Intent(this,SignupActivity::class.java)
               startActivity(intent)
           } else{
               val intent = Intent(this,SigningActivity::class.java)
               startActivity(intent)
           }
    }



    private fun updateUI(firebaseUser: FirebaseUser?){
        if(firebaseUser != null){
            val user =   User(firebaseUser.uid, firebaseUser.email.toString())
            val dao = UserDao()
            dao.addUser(user)
            val mainActivity = Intent(this,MainActivity::class.java)
            startActivity(mainActivity)
            finish()
        }
    }
}