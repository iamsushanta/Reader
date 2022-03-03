package iamzen.`in`.reader.daos

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import iamzen.`in`.reader.model.UserAddLink
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


private val TAG = "UserArticleSave"
@DelicateCoroutinesApi
class UserArticleSave (var UserID:String){



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




    fun addUserLink(webSiteLink:String ,userId:String,context:Context):Boolean{
        GlobalScope.launch(Dispatchers.IO){
            // start this line
            val user =  db.collection("userArticleSave").document(userId).collection("yourArticleSave")
            user.get().addOnCompleteListener { task ->
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
                        Log.d(TAG,"checkUserUrl Have is $checkUserUrlHave")
                        readUrl(webSiteLink,userId,true)

                        return@addOnCompleteListener
                    } else if(value.size == 0 ){
                        Log.d(TAG,"value size is 0")
                        readUrl(webSiteLink,userId,true)
                        return@addOnCompleteListener
                    } else{
                        Log.d(TAG,"Your url is already have")
                        return@addOnCompleteListener
                    }


                }
            }.addOnFailureListener {
                Log.d(TAG, "exception is called")
            }
        }
        return false
    }



    private fun readUrl(userSaveUrl:String,userId:String,check:Boolean = false) {

        GlobalScope.launch(Dispatchers.IO) {


            try {
                if(check) {

                    val htmlData: Document = Jsoup.connect(userSaveUrl).get()
                    val cleanData = htmlData.select("title,p,h1,h2,h3,h4,h5,h6,link[rel*=icon]")


                    val userAddLink = UserAddLink(userId)
                    userAddLink.articleLink = userSaveUrl
                    var linkUnderGo = true
                    for(item in cleanData){
//                        Log.d(TAG,"item is show ${item.tag()} is ${item}")
                        when(item.tag().toString()){
                            "link" -> {
                                if(linkUnderGo){
                                    Log.d(TAG,"icon is ${item.tag()}, ${item.attr("href")} ${(item.attr("sizes") == "32×32")}")
                                    userAddLink.authorImage = item.attr("href")
                                    linkUnderGo = false
                                }

                            }
                            "title" -> {
                                userAddLink.authorName = item.text()
                            }
                            "p" -> {
                                userAddLink.userUrlData = mapOf("p" to item.toString())

                            }
                            "h1" -> {
                                userAddLink.userUrlData = mapOf("h1" to item.toString() )
                            }
                            "h2" -> {
                                userAddLink.userUrlData = mapOf("h2" to item.toString() )
                            }
                            "h3" -> {
                                userAddLink.userUrlData = mapOf("h3" to item.toString())
                            }
                            "h4" -> {
                                userAddLink.userUrlData = mapOf("h4" to item.toString())

                            }
                            "h5" -> {
                                userAddLink.userUrlData = mapOf("h5" to item.toString())
                            }
                            "h6"->{
                                userAddLink.userUrlData = mapOf("h6" to item.toString())
                            }
                        }

                    }
                    userArticleSave(userAddLink)

                }
            } catch(e: Exception){
                Log.d(TAG,"error throw ${e.printStackTrace()}")
            }
        }

    }







}


