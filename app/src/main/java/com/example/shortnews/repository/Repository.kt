package com.example.shortnews.repository

import androidx.lifecycle.MutableLiveData
import com.example.shortnews.NewsApi
import com.example.shortnews.models.News
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(private val restApi: NewsApi) {


    fun getNews(
        q: String,
        from: String,
        apiKey: String,
        data: MutableLiveData<News>,
        error: MutableLiveData<Throwable>
    ) {
        restApi.getNews(q,from,apiKey).enqueue(object : Callback<News?> {
            override fun onResponse(call: Call<News?>, response: Response<News?>) {
               if (response.body()!=null){
                   data.value=response.body()
               }
            }

            override fun onFailure(call: Call<News?>, t: Throwable) {
               error.value=t
            }
        })
    }

}