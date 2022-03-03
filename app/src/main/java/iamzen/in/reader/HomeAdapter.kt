package iamzen.`in`.reader

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import iamzen.`in`.reader.model.UserAddLink

class HomeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val webSiteUrl: TextView = itemView.findViewById(R.id.article_link)
    val downLoadArticle: ImageView = itemView.findViewById(R.id.downLoadArticle)
    val webSiteImage:ImageView = itemView.findViewById(R.id.author_image)
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
            listener.downLoadArticle(snapshots.getSnapshot(view.adapterPosition).id)
        }
        return view

    }



    @SuppressLint("SetTextI18n", "CheckResult")
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int, model: UserAddLink) {
        Log.d(TAG,"onBindViewHolder called")
        holder.webSiteUrl.text = model.authorName

        imageLoad(holder,model)

    }

    @SuppressLint("CheckResult")
    fun imageLoad(holder: HomeViewHolder, model: UserAddLink){


            Glide.with(holder.itemView.context).load(model.authorImage).circleCrop()
                .error(R.drawable.man).listener(object :
                RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            }).into(holder.webSiteImage)



    } // image loader function is end..
}


interface ItemClick{
    fun itemClick(itemId:String,url:String)
    fun downLoadArticle(itemId: String)
}