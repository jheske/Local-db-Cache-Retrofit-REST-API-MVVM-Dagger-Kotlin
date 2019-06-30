package com.heske.myrecipes.di

import android.app.Application
import androidx.room.Room
import com.heske.myrecipes.persistence.RecipeDao
import com.heske.myrecipes.persistence.RecipeDatabase
import com.heske.myrecipes.requests.responses.RecipeApi
import com.heske.myrecipes.util.*
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/* Copyright (c) 2019 Jill Heske All rights reserved.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
@Module
class AppModule {
    @Singleton
    @Provides
    internal fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            // establish connection to server
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            // time between each byte read from the server
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            // time between each byte sent to server
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitService(client: OkHttpClient): RecipeApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(RecipeApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): RecipeDatabase {
        return Room
            .databaseBuilder(app, RecipeDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: RecipeDatabase): RecipeDao {
        return db.recipeDao
    }
}