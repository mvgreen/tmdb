package com.mvgreen.tmdbapp

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.mvgreen.data.network.search.entity.GenreResponse
import com.mvgreen.data.network.search.entity.MovieObject
import com.mvgreen.data.network.search.entity.SearchResponse
import com.mvgreen.tmdbapp.internal.di.DI
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class SearchApiTest {

    lateinit var appContext: Context

    val immediate = object : Scheduler() {
        override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
            return super.scheduleDirect(run, 0, unit)
        }

        override fun createWorker(): Worker {
            return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
        }
    }

    @Before
    fun init() {
        RxJavaPlugins.setInitIoSchedulerHandler { immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        DI.init(appContext)
    }

    @Test
    fun whenRequestSuccessfulResponseIsParsed() {
        // given
        val api = DI.appComponent.searchApi()

        var result: SearchResponse? = null
        // when
        api.search("приключения", 1)
            .subscribe(
                { response ->
                    // then
                    result = response
                },
                {
                    throw AssertionError()
                })
        assertTrue(result?.results != null && result?.statusMessage == null)
    }

    @Test
    fun whenGenresRequestedResponseIsParsed() {
        // given
        val api = DI.appComponent.searchApi()

        var result: GenreResponse? = null
        // when
        api.getGenreList()
            .subscribe(
                { response ->
                    // then
                    result = response
                },
                {
                    throw AssertionError()
                })
        assertTrue(!result?.genres.isNullOrEmpty() && result?.statusMessage == null)
    }

    @Test
    fun whenDetailsRequestedResponseContainsRuntime() {
        // given
        val api = DI.appComponent.searchApi()

        var result: MovieObject? = null
        // when
        api.getMovieDetails(550)
            .subscribe(
                { response ->
                    // then
                    result = response
                },
                {
                    throw AssertionError()
                })
        assertTrue(result?.statusMessage == null && result?.runtime != null)
    }

}