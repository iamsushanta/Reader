package iamzen.`in`.reader

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import iamzen.`in`.reader.daos.ReadDao
import iamzen.`in`.reader.daos.UserArticleSave
import iamzen.`in`.reader.model.UserAddLink
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeScreen.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val TAG = "HomeScreen"

@DelicateCoroutinesApi
class HomeScreen (): Fragment(), ItemClick {


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

//    mUserAddDao = UserArticleSave()
    private lateinit var mHomeAdapter:HomeAdapter

    private lateinit var mUserReadDao: ReadDao
    private lateinit var mUserAddDao:UserArticleSave
    private lateinit var mAuth: FirebaseAuth

    var pageHeight = 1120
    var pagewidth = 792
    var bmp: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        mAuth = FirebaseAuth.getInstance()

        bmp = BitmapFactory.decodeResource(resources, R.drawable.man);

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG,"onViewCreated is start")

        mUserReadDao = ReadDao(mAuth.uid!!)
        mUserAddDao = mAuth.uid?.let { UserArticleSave(it) }!!
        val postCollection = mUserAddDao.userCollection
        val query = postCollection.orderBy("articleLink",Query.Direction.DESCENDING)

        Log.d(TAG, postCollection.toString())
        val option = FirestoreRecyclerOptions.Builder<UserAddLink>().setQuery(query, UserAddLink::class.java).build()
         mHomeAdapter = HomeAdapter(option,this)
        createRecyclerView()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeScreen.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeScreen().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun createRecyclerView(){
        Log.d(TAG,"createRecyclerView is start ")
        home_recycler_view.layoutManager = LinearLayoutManager(context)
        home_recycler_view.adapter = mHomeAdapter
    }

    override fun onStart() {
        super.onStart()
        mHomeAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mHomeAdapter.stopListening()
    }


    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {

        menu.clear()
        menuInflater.inflate(R.menu.top_app_bar,menu)

    }

    override fun itemClick(itemId: String, url:String) {
        Log.d(TAG,"item Click is called $itemId url is $url")
        val intent = Intent(requireContext(),ReadActivity::class.java)
        intent.putExtra(ARTICLE_LINK_ID,itemId)
        startActivity(intent)
    }

    override fun downLoadArticle(itemId: String) {
        GlobalScope.launch(Dispatchers.IO) {
//            val user = mUserReadDao.getUserId(itemId).await().toObject(UserAddLink::class.java)
//            val userTitle = user?.authorName?.replace(" ", "")
//
//            val pdfDocument = PdfDocument()
//
//            val title = Paint()
//
//            val mypageInfo = PageInfo.Builder(pagewidth, pageHeight, 10).create()
//
//            val myPage = pdfDocument.startPage(mypageInfo)
//            val canvas: Canvas = myPage.canvas
//
//
//            title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
//            title.textSize = 28F;
////
//            title.color = ContextCompat.getColor(requireContext(), R.color.black);
//
//            canvas.drawText("A portal for IT professionals.", 50f, 50f, title);
//            canvas.drawText("Geeks for Geeks", 50f, 80f, title);
//
//            var itemCount = 50f
//            var itemCount2 = 80f
//
//            for (item in user?.userUrlData!!){
//                canvas.drawText("\n$item",itemCount,itemCount2,title)
//                itemCount2 += 20f
//            }
////            title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL);
////            title.color = ContextCompat.getColor(requireContext(), R.color.purple_200);
////            title.textSize = 15f;
////
////            title.textAlign = Paint.Align.CENTER;
////            canvas.drawText("This is sample document which we have created.", 396f, 560f, title);
//
//            pdfDocument.finishPage(myPage)
//
//            var file = File(Environment.getExternalStorageDirectory(),"$userTitle.pdf")
//            try{
//                pdfDocument.writeTo(FileOutputStream(file))
//                Log.d(TAG,"download is complete")
//            } catch(e: Exception){
//                Log.d(TAG,"error throw download ${e.printStackTrace()}")
//            }
//            pdfDocument.close()
        }
    }

    fun searchingUser(query:String){
        val mUserDao = mUserAddDao.userCollection
        val querySearch = mUserDao.orderBy("articleLink").startAfter(query)
        val option = FirestoreRecyclerOptions.Builder<UserAddLink>().setQuery(querySearch, UserAddLink::class.java).build()
        mHomeAdapter.updateOptions(option)

    }




}



