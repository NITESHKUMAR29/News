package com.example.shortnews


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shortnews.databinding.ListItemBinding
import com.example.shortnews.models.Article


class MyAdapter(val context: Context,val activity: Activity, val newsList: List<Article>):

    RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context)
        val binding:ListItemBinding= DataBindingUtil.inflate(view,R.layout.list_item,parent,false)
        return  MyViewHolder(binding)
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=newsList[position]
        with(holder.binding){
          heading.text=currentItem.title
            Glide.with(context)
                .load(currentItem.urlToImage)
                .centerCrop()
                .placeholder(R.drawable.news_placeholder)
                .into(mainimage)
            contentmain.text=currentItem.content
            expandNews .setOnClickListener(View.OnClickListener { // initializing object for custom chrome tabs.
                val customIntent = CustomTabsIntent.Builder()

                customIntent.setToolbarColor(
                    ContextCompat.getColor(
                        context,
                        R.color.purple_200
                    )
                )

                openCustomTab(activity, customIntent.build(), Uri.parse(currentItem.url))
            })
        }
    }
    override fun getItemCount(): Int {
        return newsList.size
    }
    class MyViewHolder(val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root){

    }

    fun openCustomTab(activity: Activity, customTabsIntent: CustomTabsIntent, uri: Uri?) {
        // package name is the default package
        // for our custom chrome tab
        val packageName = "com.android.chrome"
        if (packageName != null) {

            // we are checking if the package name is not null
            // if package name is not null then we are calling
            // that custom chrome tab with intent by passing its
            // package name.
            customTabsIntent.intent.setPackage(packageName)

            // in that custom tab intent we are passing
            // our url which we have to browse.
            if (uri != null) {
                customTabsIntent.launchUrl(activity, uri)
            }
        } else {
            // if the custom tabs fails to load then we are simply
            // redirecting our user to users device default browser.
            activity.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }










}