package iamzen.`in`.reader.model

data class UserAddLink(var uid:String = "",var authorName:String = "",
                       var authorImage:String = "",
                       var articleLink:String = "",
                       var likeArticle:Boolean = false,
                       var userUrlData:Map<String,String> = mapOf())