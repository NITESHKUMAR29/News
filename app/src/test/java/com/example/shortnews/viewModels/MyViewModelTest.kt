package com.example.shortnews.viewModels


import android.arch.core.executor.testing.InstantTaskExecutorRule

import androidx.lifecycle.MutableLiveData
import com.example.shortnews.models.Article
import com.example.shortnews.models.News
import com.example.shortnews.models.Source
import com.example.shortnews.repository.Repository

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
@RunWith(MockitoJUnitRunner::class)
class MyViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var myViewModel: MyViewModel

    @Mock
    private lateinit var repository: Repository

    @Captor
    private lateinit var newsCallbackCaptor: ArgumentCaptor<Callback<News?>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        myViewModel = MyViewModel(repository)
    }

    @Test
    fun getNewsSuccess() {
        // Define the test data
        val testSource = Source("source_id", "Source Name")
        val testArticle = Article(
            "Author",
            "Content",
            "Description",
            "2023-10-30",
            testSource,
            "Title",
            "https://example.com",
            "https://example.com/image.jpg"
        )
        val testArticles = listOf(testArticle)
        val testNews = News(testArticles, "ok", testArticles.size)
        val newsLiveData = MutableLiveData<News>()
        val errorLiveData = MutableLiveData<Throwable>()

        // Mock the repository's getNews method to capture the callback
        Mockito.`when`(repository.getNews("testQuery", "testFrom", "testApiKey",newsLiveData,errorLiveData))
            .thenAnswer { invocation ->
                val callback = invocation.getArgument<Callback<News?>>(3)
                callback.onResponse(Mockito.mock(Call::class.java) as Call<News?>, Response.success(testNews))
            }

        // Observe the LiveData
       // myViewModel.news.observeForever { newsLiveData.value = it }

        // Trigger the ViewModel method
        myViewModel.getNews("testQuery", "testFrom", "testApiKey")

        // Verify that the LiveData has been updated correctly
        newsLiveData.observeForever { news ->
            val capturedError = myViewModel.error.value

            assert(news == testNews)
            assert(capturedError == null)
        }
    }

    @Test
    fun getNewsError() {
        // Define the test error
        val testError =null
        val errorLiveData = MutableLiveData<Throwable>()
        val newsLiveData = MutableLiveData<News>()

        // Mock the repository's getNews method to capture the callback
        Mockito.`when`(repository.getNews("testQuery", "testFrom", "testApiKey",newsLiveData,errorLiveData))
            .thenAnswer { invocation ->
                val callback = invocation.getArgument<Callback<News?>>(3)
                callback.onFailure(Mockito.mock(Call::class.java) as Call<News?>, testError)
            }

        // Observe the error LiveData
        myViewModel.error.observeForever { errorLiveData.value = it }

        // Trigger the ViewModel method
        myViewModel.getNews("testQuery", "testFrom", "testApiKey")



        // Verify that the error LiveData has been updated correctly
        val capturedError = errorLiveData.value
        println("capturedError: $capturedError")
        println("testError: $testError")
        assert(capturedError == testError)
    }

}

