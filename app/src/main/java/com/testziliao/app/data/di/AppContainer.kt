package com.testziliao.app.data.di

import android.content.Context
import androidx.room.Room
import com.testziliao.app.BuildConfig
import com.testziliao.app.data.local.AppDatabase
import com.testziliao.app.data.remote.ContentApiService
import com.testziliao.app.data.repository.ContentRepository
import com.testziliao.app.data.repository.FavoriteRepository
import com.testziliao.app.data.repository.HistoryRepository
import com.testziliao.app.data.repository.SearchRepository
import com.testziliao.app.data.repository.impl.DefaultContentRepository
import com.testziliao.app.data.repository.impl.DefaultFavoriteRepository
import com.testziliao.app.data.repository.impl.DefaultHistoryRepository
import com.testziliao.app.data.repository.impl.DefaultSearchRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class AppContainer(context: Context) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.CONTENT_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val apiService: ContentApiService = retrofit.create(ContentApiService::class.java)

    val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "testapp.db"
    ).build()

    val contentRepository: ContentRepository = DefaultContentRepository(
        apiService = apiService,
        categoryDao = database.categoryDao(),
        articleDao = database.articleDao(),
        articleContentDao = database.articleContentDao(),
        questionSetDao = database.questionSetDao(),
        questionItemDao = database.questionItemDao()
    )

    val favoriteRepository: FavoriteRepository = DefaultFavoriteRepository(
        favoriteArticleDao = database.favoriteArticleDao(),
        favoriteQuestionSetDao = database.favoriteQuestionSetDao()
    )

    val historyRepository: HistoryRepository = DefaultHistoryRepository(
        historyDao = database.historyDao()
    )

    val searchRepository: SearchRepository = DefaultSearchRepository(
        searchHistoryDao = database.searchHistoryDao()
    )
}
