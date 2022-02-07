package iamzen.`in`.reader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import android.widget.Toast

import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnFailureListener

import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.android.synthetic.main.activity_siging.*
import java.lang.Exception


private const val TAG = "ForgotPassword"
class ForgotPassword : AppCompatActivity() {
    private lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        mAuth = FirebaseAuth.getInstance()
        Forgot_button.setOnClickListener{
            if(Forgot_email.text.isNotEmpty()){
                recoverEmail(Forgot_email.text.toString())
                progressBar.visibility = View.VISIBLE
                Forgot_email.visibility = View.GONE
                Forgot_button.visibility = View.GONE
                Forgot_emai_alart.visibility = View.GONE

            }
        }
    }

    private fun recoverEmail(email:String){
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                progressBar.visibility = View.GONE
                Forgot_email.visibility = View.VISIBLE
                Forgot_button.visibility = View.VISIBLE
                Forgot_emai_alart.visibility = View.VISIBLE
                // if isSuccessful then done message will be shown
                // and you can change the password
                Toast.makeText(this, "Done sent", Toast.LENGTH_LONG).show()
                val intent = Intent(this,SigningActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Error Occured", Toast.LENGTH_LONG).show()
                try {
                    throw task.exception!!
                } catch (e: Exception) {
                    Log.e(TAG, e.message!!)
                    Forgot_emai_alart.text = e.message

                }
            }
        }.addOnFailureListener {
            progressBar.visibility = View.GONE
            Forgot_email.visibility = View.VISIBLE
            Forgot_button.visibility = View.VISIBLE
            Forgot_emai_alart.visibility = View.VISIBLE
            Toast.makeText(this, "Error Failed", Toast.LENGTH_LONG).show()
        }
    }
}