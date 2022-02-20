package iamzen.`in`.reader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import iamzen.`in`.reader.daos.UserDao
import iamzen.`in`.reader.model.User
import kotlinx.android.synthetic.main.activity_siging.*
import kotlinx.android.synthetic.main.activity_siging.Signing_alert
import kotlinx.android.synthetic.main.activity_siging.Signing_app_name
import kotlinx.android.synthetic.main.activity_siging.Signing_email
import kotlinx.android.synthetic.main.activity_siging.Signing_password
import kotlinx.android.synthetic.main.activity_siging.Signing_progressBar
import kotlinx.coroutines.DelicateCoroutinesApi

import com.google.firebase.auth.FirebaseAuthUserCollisionException

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

import java.lang.Exception


private const val TAG = "SigningActivity"

@DelicateCoroutinesApi
class SigningActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_siging)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val window: Window = this.window
        window.statusBarColor = ContextCompat.getColor(this,R.color.white)

        mAuth = FirebaseAuth.getInstance()

        Signing_complete.setOnClickListener{
            if(Signing_email.text.isNotEmpty() && Signing_password.text.isNotEmpty()){
                userEmailSigningEmail(Signing_email.text.toString(),Signing_password.text.toString())
                Signing_email.visibility = View.GONE
                Signing_password.visibility = View.GONE
                Signing_app_name.visibility = View.GONE
                Signing_complete.visibility = View.GONE
                Signing_forgot_password.visibility = View.GONE
                Signing_progressBar.visibility = View.VISIBLE
                Signing_alert.visibility = View.GONE
            }
        }
        
        Signing_forgot_password.setOnClickListener{
            val intent = Intent(this,ForgotPassword::class.java)
            startActivity(intent)
        }
    }



    private fun userEmailSigningEmail(email:String,password:String){

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){ task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success")
                val user = mAuth.currentUser
                updateUI(user)
            }
            else{
            try {
                throw task.exception!!
            } catch (e: Exception) {
                Log.e(TAG, e.message!!)
                Signing_alert.text = e.message
                updateUI(null)
            }


        }



        }
    }

    private fun updateUI(firebaseUser: FirebaseUser?){
        if(firebaseUser!=null){
            Log.d(TAG,"firebase user not null")
            val user = User(firebaseUser.uid,firebaseUser.email.toString())
            val dao = UserDao()
            dao.addUser(user)
            val mainActivity = Intent(this,MainActivity::class.java)
            startActivity(mainActivity)
            finish()
        } else {
            Log.d(TAG,"firebase user is null")

            Signing_email.visibility = View.VISIBLE
            Signing_password.visibility = View.VISIBLE
            Signing_app_name.visibility = View.VISIBLE
            Signing_complete.visibility = View.VISIBLE
            Signing_progressBar.visibility = View.GONE
            Signing_alert.visibility = View.VISIBLE
            Signing_forgot_password.visibility = View.VISIBLE
        }
    }
}