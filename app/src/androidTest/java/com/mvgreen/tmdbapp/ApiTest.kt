package com.mvgreen.tmdbapp

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.mvgreen.data.exception.NetworkException
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

class ApiTest {

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
        val repository = DI.appComponent.authRepository()

        // when
        repository
            .login("mvgreen_test", "Fe6txkBX6kBWWN5")
            .subscribe { result, err ->
                // then
                assertTrue(err == null)
                assertTrue(!result.isNullOrEmpty())
            }
    }

    @Test
    fun whenRequestFailsNetworkExceptionIsThrown() {
        // given
        val repository = DI.appComponent.authRepository()

        // when
        var exception: Throwable? = null
        repository
            .login("badmail@redmadrobot.com", "bad_password")
            .subscribe { _, err ->
                exception = err
            }

        // then
        assertTrue(exception != null && exception is NetworkException)
    }

}