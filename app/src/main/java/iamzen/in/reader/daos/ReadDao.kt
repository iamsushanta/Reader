package iamzen.`in`.reader.daos

import android.util.Log
import android.webkit.WebView
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import iamzen.`in`.reader.model.UserAddLink
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.io.File

private const val TAG = "ReadDao"
var checkArticleIsLike = false
@DelicateCoroutinesApi
class ReadDao(firebaseUserId:String) {

    private val db = FirebaseFirestore.getInstance()
    val userCollection = db.collection("userArticleSave").document(firebaseUserId).collection("yourArticleSave")

     fun getUserId(userId:String): Task<DocumentSnapshot> {
        return userCollection.document(userId).get()
    }

    fun setLike(urlID: String){
        GlobalScope.launch(Dispatchers.IO) {

            val user = getUserId(urlID).await().toObject(UserAddLink::class.java)!!
            user.likeArticle = !checkArticleIsLike
            checkArticleIsLike = user.likeArticle
            Log.d(TAG,"user Like Article is ${user.likeArticle}")
            userCollection.document(urlID).set(user)
        }
    }

    fun showUserImage(urlID:String,webView: WebView){
        GlobalScope.launch(Dispatchers.IO){
            val user =  getUserId(urlID).await().toObject(UserAddLink::class.java)
            val file = File("${user?.pathFile}")

            if(file.createNewFile()){
                Log.d(TAG,"file is not exist")
                for(item in user?.userUrlData!!){
                    file.appendText("\n$item")
                }

            }

            withContext(Dispatchers.Main){
                webView.loadUrl("file:///${user?.pathFile}")
            }

        }


    }






}