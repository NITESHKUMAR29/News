package com.example.shortnews

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shortnews.application.MyApplication
import com.example.shortnews.databinding.ActivityMainBinding
import com.example.shortnews.models.Article
import com.example.shortnews.models.News
import com.example.shortnews.repository.Repository
import com.example.shortnews.viewModels.MyViewModel
import com.example.shortnews.viewModels.MyViewModelFactory
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var adapter: MyAdapter
    lateinit var newslist1: List<Article>
    lateinit var viewModel: MyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        val getApi = MyApplication.retrofit.create(NewsApi::class.java)
        val repository = Repository(getApi)
        viewModel = ViewModelProvider(this, MyViewModelFactory(repository))[MyViewModel::class.java]


        with(binding) {
            trendingNews("All")
            all.setTextColor(Color.WHITE)
            all.setBackgroundResource(R.drawable.trending_background)
            all.setOnClickListener {
                all.setTextColor(Color.WHITE)
                sports.setTextColor(Color.BLACK)
                movie.setTextColor(Color.BLACK)
                india.setTextColor(Color.BLACK)
                politics.setTextColor(Color.BLACK)
                ipl.setTextColor(Color.BLACK)
                trendingNews(all.text.toString())
                all.setBackgroundResource(R.drawable.trending_background)
                sports.setBackgroundResource(R.drawable.search_main)
                politics.setBackgroundResource(R.drawable.search_main)
                movie.setBackgroundResource(R.drawable.search_main)
                india.setBackgroundResource(R.drawable.search_main)
                ipl.setBackgroundResource(R.drawable.search_main)
            }
            sports.setOnClickListener {
                all.setTextColor(Color.BLACK)
                movie.setTextColor(Color.BLACK)
                india.setTextColor(Color.BLACK)
                politics.setTextColor(Color.BLACK)
                ipl.setTextColor(Color.BLACK)
                sports.setTextColor(Color.WHITE)
                trendingNews(sports.text.toString())
                sports.setBackgroundResource(R.drawable.trending_background)
                all.setBackgroundResource(R.drawable.search_main)
                politics.setBackgroundResource(R.drawable.search_main)
                movie.setBackgroundResource(R.drawable.search_main)
                india.setBackgroundResource(R.drawable.search_main)
                ipl.setBackgroundResource(R.drawable.search_main)
            }

            politics.setOnClickListener {
                all.setTextColor(Color.BLACK)
                sports.setTextColor(Color.BLACK)
                movie.setTextColor(Color.BLACK)
                india.setTextColor(Color.BLACK)
                ipl.setTextColor(Color.BLACK)
                politics.setTextColor(Color.WHITE)
                trendingNews(politics.text.toString())
                politics.setBackgroundResource(R.drawable.trending_background)
                sports.setBackgroundResource(R.drawable.search_main)
                all.setBackgroundResource(R.drawable.search_main)
                movie.setBackgroundResource(R.drawable.search_main)
                india.setBackgroundResource(R.drawable.search_main)
                ipl.setBackgroundResource(R.drawable.search_main)
            }
            movie.setOnClickListener {
                all.setTextColor(Color.BLACK)
                sports.setTextColor(Color.BLACK)
                india.setTextColor(Color.BLACK)
                politics.setTextColor(Color.BLACK)
                ipl.setTextColor(Color.BLACK)
                movie.setTextColor(Color.WHITE)
                trendingNews(movie.text.toString())
                movie.setBackgroundResource(R.drawable.trending_background)
                sports.setBackgroundResource(R.drawable.search_main)
                politics.setBackgroundResource(R.drawable.search_main)
                all.setBackgroundResource(R.drawable.search_main)
                india.setBackgroundResource(R.drawable.search_main)
                ipl.setBackgroundResource(R.drawable.search_main)
            }
            india.setOnClickListener {
                all.setTextColor(Color.BLACK)
                sports.setTextColor(Color.BLACK)
                movie.setTextColor(Color.BLACK)
                politics.setTextColor(Color.BLACK)
                ipl.setTextColor(Color.BLACK)
                india.setTextColor(Color.WHITE)
                trendingNews(india.text.toString())
                india.setBackgroundResource(R.drawable.trending_background)
                sports.setBackgroundResource(R.drawable.search_main)
                politics.setBackgroundResource(R.drawable.search_main)
                movie.setBackgroundResource(R.drawable.search_main)
                all.setBackgroundResource(R.drawable.search_main)
                ipl.setBackgroundResource(R.drawable.search_main)
            }
            ipl.setOnClickListener {
                all.setTextColor(Color.BLACK)
                sports.setTextColor(Color.BLACK)
                movie.setTextColor(Color.BLACK)
                india.setTextColor(Color.BLACK)
                politics.setTextColor(Color.BLACK)
                ipl.setTextColor(Color.WHITE)
                trendingNews(ipl.text.toString())
                ipl.setBackgroundResource(R.drawable.trending_background)
                sports.setBackgroundResource(R.drawable.search_main)
                politics.setBackgroundResource(R.drawable.search_main)
                movie.setBackgroundResource(R.drawable.search_main)
                india.setBackgroundResource(R.drawable.search_main)
                all.setBackgroundResource(R.drawable.search_main)
            }

            search.setOnClickListener {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)

            }
        }
    }

    private fun trendingNews(searchType: String) {
        newslist1 = ArrayList()
        binding.recyclerView.visibility=View.GONE
        binding.progressBar.visibility=View.GONE


        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val previousDay = dateFormat.format(calendar.time)

//        viewModel.getNews(searchType, previousDay, "5bc8f27a8cd74ccbbf7a5d678cb7b9cd")
//            .observe(this) {
//                newslist1= it.articles
//                adapter= MyAdapter(this@MainActivity ,this@MainActivity ,newslist1)
//                binding.recyclerView.adapter=adapter
//                binding.recyclerView.layoutManager= LinearLayoutManager(this@MainActivity)
//            }

        lifecycleScope.launch {
            viewModel.getNews(searchType, previousDay, "5bc8f27a8cd74ccbbf7a5d678cb7b9cd").onStart {
               binding.progressBar.visibility= View.VISIBLE
            }.onCompletion {
                binding.progressBar.visibility= View.GONE
                binding.recyclerView.visibility= View.VISIBLE
            }.collect{

                newslist1= it.articles
                adapter= MyAdapter(this@MainActivity ,this@MainActivity ,newslist1)
                binding.recyclerView.adapter=adapter
                binding.recyclerView.layoutManager= LinearLayoutManager(this@MainActivity)
            }
        }




    }



}