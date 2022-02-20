package iamzen.`in`.reader

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iamzen.`in`.reader.model.UserAddLink
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions



class HomeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val webSiteUrl: TextView = itemView.findViewById(R.id.article_link)
    val downLoadArticle: ImageView = itemView.findViewById(R.id.downLoadArticle)
}

private const val TAG = "HomeAdapter"
class HomeAdapter(userAdd:FirestoreRecyclerOptions<UserAddLink>,val listener:ItemClick):FirestoreRecyclerAdapter<UserAddLink,HomeViewHolder> (userAdd){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        Log.d(TAG,"onCreateViewHolder is called")
        val view = HomeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_fragment_list_of_item,parent,false))
        // setOnClickListener add after i come
        view.webSiteUrl.setOnClickListener {
            listener.itemClick(snapshots.getSnapshot(view.adapterPosition).id,view.webSiteUrl.text.toString())
        }
        view.downLoadArticle.setOnClickListener{
            Log.d(TAG,"downLoad button is clicked")
        }
        return view

    }



    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int, model: UserAddLink) {
        Log.d(TAG,"onBindViewHolder called")
        holder.webSiteUrl.text = "https://${model.articleLink}"
    }
}

interface ItemClick{
    fun itemClick(uid:String,url:String)
}