package iamzen.`in`.reader.daos

import android.util.Log
import com.google.android.gms.tasks.Task
import iamzen.`in`.reader.model.UserAddLink
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import com.google.firebase.firestore.*

import com.google.firebase.firestore.DocumentSnapshot


private val TAG = "UserArticleSave"
@DelicateCoroutinesApi
class UserArticleSave (UserID:String){



    private val db = FirebaseFirestore.getInstance()
     val userCollection = db.collection("userArticleSave").document(UserID).collection("yourArticleSave")
    private fun userArticleSave(userAddLink: UserAddLink?){
        userAddLink?.let {
            GlobalScope.launch(Dispatchers.IO){
                Log.d(TAG,"userArticleSave is called and user addLink is called")
                userCollection.document().set(it)
            }
        }
    }

    private fun getUser(): Task<DocumentSnapshot> {
            return userCollection.document().get()
    }




    fun addUserLink(webSiteLink:String , userAddLink:UserAddLink?):Boolean{
        GlobalScope.launch(Dispatchers.IO){
            // start this line
            val user = userAddLink?.let { db.collection("userArticleSave").document(it.uid).collection("yourArticleSave") }
            user?.get()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val doc = task.result
                    val value = doc.documents
                    Log.d(TAG,"article Link is $value")
                    var checkUserUrlHave = true
                    for(urlCount in 0 until value.size){
                        Log.d(TAG,"get data  this is value ${value[urlCount]["articleLink"]}  value size is ${value.size}")
                        val firebaseUrl = value[urlCount]["articleLink"]
                        if(webSiteLink == firebaseUrl){
                            checkUserUrlHave = false
                        }
                    }

                    if(checkUserUrlHave){
                        Log.d(TAG,"creckUserUrl Have is $checkUserUrlHave")
                        userArticleSave(userAddLink)
                        return@addOnCompleteListener
                    }
                    else if(value.size == 0 ){
                        Log.d(TAG,"value size is 0")
                        userArticleSave(userAddLink)
                        return@addOnCompleteListener
                    }
                    else{
                        Log.d(TAG,"Your url is already have")
                        return@addOnCompleteListener
                    }


                }
            }?.addOnFailureListener {
                Log.d(TAG, "exception is called")
            }
        }
        return false
    }







}


