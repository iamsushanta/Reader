package iamzen.`in`.reader.daos

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import iamzen.`in`.reader.model.UserAddLink
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val TAG = "ReadDao"
var checkArticleIsLike = false
@DelicateCoroutinesApi
class ReadDao(private var firebaseUserId:String) {

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






}