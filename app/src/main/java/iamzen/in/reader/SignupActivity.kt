package iamzen.`in`.reader

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import iamzen.`in`.reader.daos.UserDao
import iamzen.`in`.reader.model.User
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.coroutines.DelicateCoroutinesApi
import java.lang.Exception
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.AuthResult
import java.util.*


private const val TAG = "SignupActivity"

private var USER_EMAIL = "email"

@DelicateCoroutinesApi
class SignupActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val window: Window = this.window
        window.statusBarColor = ContextCompat.getColor(this,R.color.white)

        Signup_complete.setOnClickListener{
            if(Signup_email.text.isNotEmpty() && Signup_password.text.isNotEmpty()){
                signUpEmailCheck(Signup_email.text.toString(),Signup_password.text.toString())
                USER_EMAIL = Signup_email.text.toString()
                Signup_email.visibility = View.GONE
                Signup_password.visibility = View.GONE
                Signup_app_name.visibility = View.GONE
                Signup_complete.visibility = View.GONE
                Signup_progressBar.visibility = View.VISIBLE
                Signup_alert.visibility = View.GONE
            }
        }






    }


    private fun signUpEmailCheck(email:String, password:String){


        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    try {
                        throw task.exception!!
                    } catch (e: Exception) {
                        Log.e(TAG, e.message!!)
                        Signup_alert.text = e.message
                        updateUI(null)
                    }

                }
            }.addOnFailureListener { task ->
                Log.d(TAG,"some of issue")
                Toast.makeText(baseContext,task.message,Toast.LENGTH_LONG).show()

            }

    }

    @SuppressLint("RestrictedApi")
    private fun updateUI(firebaseUser: FirebaseUser?) {
        if(firebaseUser != null){
            Log.d(TAG,"firebase user not null")
            val user: User = if(USER_EMAIL == "email"){
                User(firebaseUser.uid, firebaseUser.email.toString())
            }else{
                User(firebaseUser.uid, USER_EMAIL)
            }
            val dao = UserDao()
            dao.addUser(user)
            val mainActivity = Intent(this,MainActivity::class.java)
            startActivity(mainActivity)
            finish()
        } else {
            Log.d(TAG,"firebase user is null")

            Signup_email.visibility = View.VISIBLE
            Signup_password.visibility = View.VISIBLE
            Signup_app_name.visibility = View.VISIBLE
            Signup_complete.visibility = View.VISIBLE
            Signup_progressBar.visibility = View.GONE
            Signup_alert.visibility = View.VISIBLE

        }
    }




}