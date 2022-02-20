package iamzen.`in`.reader

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.media.MediaCodec
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import iamzen.`in`.reader.daos.UserArticleSave
import iamzen.`in`.reader.model.UserAddLink
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.*

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

    private lateinit var mUserAddDao:UserArticleSave
    private lateinit var mAuth: FirebaseAuth





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        mAuth = FirebaseAuth.getInstance()


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
        mUserAddDao = mAuth.uid?.let { UserArticleSave(it) }!!
        val postCollection = mUserAddDao.userCollection
        val query = postCollection.orderBy("articleLink",Query.Direction.DESCENDING)
        postCollection.orderBy("articleLink",Query.Direction.DESCENDING)
//        val query2 = postCollection.whereGreaterThanOrEqualTo("articleLink","")
//        val mutableList: MutableList<String> = mutableListOf("https://jamesclear.com/3-2-1/january-20-2022")
//        val query3 = postCollection.whereIn("articleLink",mutableList)

//        val reference = FirebaseDatabase.getInstance().getReference("userArticleSave")
//        val query3 = reference.child("yourArticleSave").orderByChild("articleLink").orderByValue()
        val query2 = postCollection.orderBy("articleLink").startAfter("gd")
        Log.d(TAG, postCollection.toString())
        val option = FirestoreRecyclerOptions.Builder<UserAddLink>().setQuery(query2, UserAddLink::class.java).build()
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

    override fun itemClick(uid: String, url:String) {
        Log.d(TAG,"item Click is called $uid url is $url")
        val intent = Intent(requireContext(),ReadActivity::class.java)
        intent.putExtra(ARTICLE_LINK_READ,url)
        startActivity(intent)
    }

    fun searchingUser(query:String){
        val mUserDao = mUserAddDao.userCollection
        val querySearch = mUserDao.orderBy("articleLink").startAfter(query)
        val option = FirestoreRecyclerOptions.Builder<UserAddLink>().setQuery(querySearch, UserAddLink::class.java).build()
        mHomeAdapter.updateOptions(option)

    }


}



