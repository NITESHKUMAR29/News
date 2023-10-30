package com.example.shortnews

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shortnews.models.Article
import com.example.shortnews.models.News
import com.example.shortnews.models.Source
import com.example.shortnews.viewModels.MyViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.test.ext.junit.rules.activityScenarioRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


class MainActivityTest {
    @get:Rule
    val activityScenarioRule= activityScenarioRule<MainActivity>()

    lateinit var viewModel: MyViewModel

    private lateinit var mainActivity: MainActivity

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mainActivity = MainActivity()
        mainActivity.viewModel = viewModel
    }

    @Test
    fun testTrendingNews() {
        // Create a sample list of articles
        val articles = mutableListOf<Article>()
        articles.add(
            Article(
                "Author 1",
                "Content 1",
                "Description 1",
                "2023-10-30",
                Source("ID 1", "Source 1"),
                "Title 1",
                "url1",
                "urlToImage1"
            )
        )
        articles.add(
            Article(
                "Author 2",
                "Content 2",
                "Description 2",
                "2023-10-29",
                Source("ID 2", "Source 2"),
                "Title 2",
                "url2",
                "urlToImage2"
            )
        )

        // Create a sample News response
        val newsResponse = News(articles, "ok", 2)

        // Mock the behavior of viewModel.getNews() with MutableLiveData
        val liveData = MutableLiveData<News>()
        liveData.value = newsResponse
        Mockito.`when`(viewModel.getNews(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn(liveData)

        // Call the method to test
        mainActivity.trendingNews("All")

        // Verify that the adapter and recyclerView are updated
        Assert.assertEquals(2, mainActivity.newslist1.size)

        // Now, create a real instance of MyAdapter
        val adapter = MyAdapter(mainActivity, mainActivity, mainActivity.newslist1)

        // Set the adapter to the recyclerView
        mainActivity.binding.recyclerView.adapter = adapter
        mainActivity.binding.recyclerView.layoutManager = LinearLayoutManager(mainActivity)

        // Verify that the adapter and recyclerView are updated
        Mockito.verify(mainActivity.binding.recyclerView).adapter = adapter
        Mockito.verify(mainActivity.binding.recyclerView).layoutManager = Mockito.any()
    }

    @Test
    fun testSourceEquals() {
        val source1 = Source("ID 1", "Source 1")
        val source2 = Source("ID 1", "Source 1")

        Assert.assertEquals(source1, source2)
    }


    @Test
    fun testAllButtonClickListener() {
        // Mock the click event for the 'all' button
        mainActivity.binding.all.performClick()

        // Assert that the button click behavior is as expected
        // You can add more assertions based on your specific requirements
        Assert.assertEquals(Color.WHITE, mainActivity.binding.all.currentTextColor)
    }

    // Repeat similar tests for other button click listeners (sports, politics, movie, india, ipl, search)
}
