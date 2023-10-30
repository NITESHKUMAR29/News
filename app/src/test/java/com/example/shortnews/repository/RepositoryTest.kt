package com.example.shortnews.repository


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.shortnews.NewsApi
import com.example.shortnews.models.Article
import com.example.shortnews.models.News
import com.example.shortnews.models.Source

import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock

import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class RepositoryTest {


    @get:Rule
    val rule = InstantTaskExecutorRule () // This allows LiveData to be observed on a background thread

    @Mock
    private lateinit var newsApi: NewsApi

    private lateinit var repository: Repository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = Repository(newsApi)
    }

    @Test
    fun testGetNewsSuccess() {
        val source1 = Source("source1", "Source 1")
        val source2 = Source("source2", "Source 2")

        val testArticles = listOf(
            Article(
                author = "Author 1",
                content = "Content 1",
                description = "Description 1",
                publishedAt = "2023-10-30T12:00:00Z",
                source = source1,
                title = "Article Title 1",
                url = "https://example.com/article1",
                urlToImage = "https://example.com/image1"
            ),
            Article(
                author = "Author 2",
                content = "Content 2",
                description = "Description 2",
                publishedAt = "2023-10-29T14:30:00Z",
                source = source2,
                title = "Article Title 2",
                url = "https://example.com/article2",
                urlToImage = "https://example.com/image2"
            )
            // Add more articles as needed
        )

        val testNews = News(
            articles = testArticles,
            status = "SampleStatus",
            totalResults = testArticles.size
        )

        val testCall = Mockito.mock(Call::class.java) as Call<News>

        // Mocking the behavior of the Retrofit API call
        Mockito.`when`(newsApi.getNews(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn(testCall)

        // Mocking the behavior of the API response (success)
        Mockito.`when`(testCall.enqueue(Mockito.any())).thenAnswer {
            val callback = it.getArgument(0) as Callback<News>
            callback.onResponse(testCall, Response.success(testNews))
        }

        val data = MutableLiveData<News>()
        val error = MutableLiveData<Throwable>()

        repository.getNews("TestQuery", "TestDate", "TestApiKey", data, error)

        // Verify that the data LiveData contains the expected testNews
        assertEquals(testNews, data.value)

        // Verify that the error LiveData is not set
        assertNull(error.value)
    }

    @Test
    fun testGetNewsFailure() {
        // Define a test Throwable to simulate a network error
        val testThrowable = Throwable("Network error")

        // Create LiveData objects for data and error
        val data = MutableLiveData<News>()
        val error = MutableLiveData<Throwable>()

        // Mock the behavior of the Retrofit API call (failure)
        val testCall = Mockito.mock(Call::class.java) as Call<News>
        Mockito.`when`(newsApi.getNews(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn(testCall)

        // Mock the behavior of the API response (failure)
        Mockito.`when`(testCall.enqueue(Mockito.any())).thenAnswer {
            val callback = it.getArgument(0) as Callback<News>
            callback.onFailure(testCall, testThrowable)
        }

        // Call the repository method
        repository.getNews("TestQuery", "TestDate", "TestApiKey", data, error)

        // Assert that the data LiveData is null (no data received due to failure)
        assert(data.value == null)

        // Assert that the error LiveData contains the expected testThrowable
        assert(error.value == testThrowable)
    }



}