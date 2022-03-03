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
import java.io.File


private val TAG = "UserArticleSave"

@DelicateCoroutinesApi
class UserArticleSave(var UserID: String) {


    private val db = FirebaseFirestore.getInstance()
    val userCollection =
        db.collection("userArticleSave").document(UserID).collection("yourArticleSave")

    private fun userArticleSave(userAddLink: UserAddLink?) {
        userAddLink?.let {
            GlobalScope.launch(Dispatchers.IO) {
                Log.d(TAG, "userArticleSave is called and user addLink is called")
                userCollection.document().set(it)
            }
        }
    }

    private fun getUser(): Task<DocumentSnapshot> {
        return userCollection.document().get()
    }


    fun addUserLink(webSiteLink: String, userId: String, context: Context): Boolean {
        GlobalScope.launch(Dispatchers.IO) {
            // start this line
            val user =
                db.collection("userArticleSave").document(userId).collection("yourArticleSave")
            user.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val doc = task.result
                    val value = doc.documents
                    var checkUserUrlHave = true
                    for (urlCount in 0 until value.size) {
                        val firebaseUrl = value[urlCount]["articleLink"]
                        if (webSiteLink == firebaseUrl) {
                            checkUserUrlHave = false
                        }
                    }

                    if (checkUserUrlHave) {
                        Log.d(TAG, "checkUserUrl Have is $checkUserUrlHave")
                        readUrl(webSiteLink, context)

                        return@addOnCompleteListener
                    } else if (value.size == 0) {
                        Log.d(TAG, "value size is 0")
                        readUrl(webSiteLink, context)
                        return@addOnCompleteListener
                    } else {
                        Log.d(TAG, "Your url is already have")
                        return@addOnCompleteListener
                    }


                }
            }.addOnFailureListener {
                Log.d(TAG, "exception is called")
            }
        }
        return false
    }


    private fun readUrl(userSaveUrl: String, context: Context) {

        GlobalScope.launch(Dispatchers.IO) {

            val getData: ArrayList<String> = arrayListOf()
            var webSiteImage = ""

            try {

                val htmlData: Document = Jsoup.connect(userSaveUrl).get()
                val title = htmlData.select("title")
                val imageLink = htmlData.head().select("link[rel*=icon]")

//                  Log.d(TAG,"image attrabute ${imageLink[0]}")
                Log.d(TAG, "image link is ${imageLink.attr("href")}")

//                    Log.d(TAG,"for loop is start in item is ${item.attr("href")}")
                webSiteImage = imageLink.attr("href")




                Log.d(TAG, "image Link is $imageLink")

                Log.d(TAG, "title is $title")
                val file = File(
                    context.filesDir
                        .toString() + "/" + File.pathSeparator + "${title.text()}.html"
                )


                file.createNewFile()
                if (file.exists()) {
                    Log.d(TAG, "file is exists..")
                    for (item in htmlData.allElements) {
                        getData.add(item.toString())
                        file.appendText("\n${item}")
                    }

                    val userArticle = UserAddLink(
                        UserID,
                        title.text(),
                        webSiteImage,
                        userSaveUrl,
                        false,
                        getData,
                        file.absoluteFile.toString()
                    )
                    userArticleSave(userArticle)

                    Log.d(TAG, "file $file ")
//                    Log.d(TAG,"outputStreamWriter is add data ${getData[0]}")


                }


            } catch (e: Exception) {
                Log.d(TAG, "error throw ${e.printStackTrace()}")
            }
        }

    }


}


