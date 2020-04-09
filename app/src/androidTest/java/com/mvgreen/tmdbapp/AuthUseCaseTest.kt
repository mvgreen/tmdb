package com.mvgreen.tmdbapp

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.mvgreen.data.exception.NetworkException
import com.mvgreen.tmdbapp.CredentialsConstants.BAD_LOGIN
import com.mvgreen.tmdbapp.CredentialsConstants.BAD_PASSWORD
import com.mvgreen.tmdbapp.CredentialsConstants.VALID_LOGIN
import com.mvgreen.tmdbapp.CredentialsConstants.VALID_PASSWORD
import com.mvgreen.tmdbapp.internal.di.DI
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class AuthUseCaseTest {

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
    fun whenLoginSuccessfullyDataIsSavedInPrefs() {
        // given
        val useCase = DI.appComponent.authUseCase()
        val tokenStorage = DI.appComponent.userDataStorage()

        // when
        useCase
            .login(VALID_LOGIN, VALID_PASSWORD)
            .subscribe({}, { throw AssertionError() })

        // then
        val savedToken = tokenStorage.getSessionToken()
        val (login, password) = tokenStorage.getCredentials()
        assertNotNull(savedToken)
        assertNotNull(login)
        assertNotNull(password)
    }

    @Test
    fun whenLoginFailedNetworkexceptionIsThrown() {
        // given
        val useCase = DI.appComponent.authUseCase()

        // when
        var error : Throwable? = null
        useCase
            .login(BAD_LOGIN, BAD_PASSWORD)
            .subscribe({ throw AssertionError() }, { error = it })

        // then
        assertNotNull(error)
        assertTrue(error is NetworkException)
    }
}