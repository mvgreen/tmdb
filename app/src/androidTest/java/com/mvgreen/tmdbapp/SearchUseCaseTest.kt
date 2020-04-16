package com.mvgreen.tmdbapp

import android.content.Context
import android.util.Log
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import androidx.test.platform.app.InstrumentationRegistry
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.tmdbapp.internal.di.DI
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Before
import org.junit.Test
import java.lang.Thread.sleep
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class SearchUseCaseTest {

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

    // TODO убрать
    @Test
    fun whenSearchIsPerformedPagedListIsLoaded() {
        // given
        val useCase = DI.appComponent.searchUseCase()
        useCase.initSearch()

        // when
        val compositeDisposable = CompositeDisposable()
        val dataSourceFactory = useCase.search("adventures", compositeDisposable)
        val config = PagedList.Config.Builder().setPrefetchDistance(5).build()
        val pagedList = RxPagedListBuilder(dataSourceFactory, config)

        val observable = pagedList.buildObservable()

        var result: PagedList<MovieData>? = null
        // then
        val disp = observable
            .subscribe(
                {
                    result = it
                },
                {
                    Log.e("Test", it.message, it)
                }
            )
        compositeDisposable.add(disp)

        sleep(60_000)
        result!!.loadAround(result!!.size - 1)

        sleep(60_000)

    }


}