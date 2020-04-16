package com.mvgreen.tmdbapp.ui.base.fragment

import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.mvgreen.tmdbapp.ui.base.activity.AppActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


abstract class BaseFragment : Fragment {

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    constructor() : super()

    private val compositeDisposable by lazy { CompositeDisposable() }
    private val globalListeners by lazy { mutableListOf<ViewTreeObserver.OnGlobalLayoutListener>() }

    open fun onBackPressed(): Boolean {
        return false
    }

    override fun onPause() {
        globalListeners.unsubscribeAll()
        compositeDisposable.clear()
        super.onPause()
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    protected fun changeSystemColors(
        @ColorRes statusBarColor: Int? = null,
        @ColorRes navigationBarColor: Int? = null,
        @ColorRes navigationBarDividerColor: Int? = null
    ) {
        val activity = requireActivity() as AppActivity
        if (statusBarColor != null) activity.updateStatusBar(statusBarColor)
        if (navigationBarColor != null) activity.updateNavigationBar(navigationBarColor)
        if (navigationBarDividerColor != null) activity.updateDividerColor(navigationBarDividerColor)
    }

    protected fun bindToFragmentLifecycle(listener: ViewTreeObserver.OnGlobalLayoutListener) {
        globalListeners.add(listener)
        view?.viewTreeObserver?.addOnGlobalLayoutListener(listener)
    }

    protected fun Disposable.disposeOnViewModelDestroy(): Disposable {
        compositeDisposable.add(this)
        return this
    }

    private fun MutableList<ViewTreeObserver.OnGlobalLayoutListener>.unsubscribeAll() {
        view?.let { root ->
            for (listener in this) {
                root.viewTreeObserver.removeOnGlobalLayoutListener(listener)
            }
        }
        this.clear()
    }

}