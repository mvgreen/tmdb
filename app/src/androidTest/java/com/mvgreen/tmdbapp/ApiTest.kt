package com.mvgreen.tmdbapp

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.mvgreen.data.network.entity.auth.CreateSessionRequest
import com.mvgreen.data.network.entity.auth.ValidateTokenRequest
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
    fun whenRequestIsSentResponseIsParsed() {
        // given
        val api = DI.appComponent.api()

        // when
        lateinit var requestToken: String
        api
            .getRequestToken()
            .subscribe { res, err ->
                assertTrue(err == null)
                requestToken = res.requestToken!!
            }

        api
            .validateRequestToken(
                ValidateTokenRequest(
                    "mvgreen_test",
                    "Fe6txkBX6kBWWN5",
                    requestToken
                )
            )
            .subscribe { _, err ->
                assertTrue(err == null)
            }

        var sessionToken: String? = null
        api
            .createSession(CreateSessionRequest(requestToken))
            .subscribe { res, _ ->
                sessionToken = res.sessionId
            }

        // then
        assertTrue(!sessionToken.isNullOrEmpty())
    }

}