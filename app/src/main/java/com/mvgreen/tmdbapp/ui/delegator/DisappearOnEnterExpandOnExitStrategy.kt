package com.mvgreen.tmdbapp.ui.delegator

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.view.View
import com.mvgreen.tmdbapp.R
import com.redmadrobot.lib.sd.base.InitialState
import com.redmadrobot.lib.sd.base.State
import com.redmadrobot.lib.sd.base.StateChangeStrategy
import java.lang.ref.WeakReference

class DisappearOnEnterExpandOnExitStrategy(private val context: Context) :
    StateChangeStrategy<Visibility> {

    override fun onStateEnter(state: State<Visibility>, prevState: State<Visibility>?) {
        for (view in state.viewsGroup) {
            view.visibility = View.GONE
            view.scaleY = 0.0f
        }
    }

    override fun onStateExit(state: State<Visibility>, nextState: State<Visibility>?) {
        for (view in state.viewsGroup) {
            AnimatorInflater.loadAnimator(context, R.animator.expand_view).apply {
                setTarget(view)
                addListener(ExpandListener(WeakReference(view)))
                start()
            }
        }
    }

}

private class ExpandListener(private val target: WeakReference<View>) :
    Animator.AnimatorListener {
    override fun onAnimationStart(animation: Animator?) {
        target.get()?.visibility = View.VISIBLE
    }

    override fun onAnimationEnd(animation: Animator?) {}
    override fun onAnimationCancel(animation: Animator?) {}
    override fun onAnimationRepeat(animation: Animator?) {}
}

object ShowFirstInitialState : InitialState<Visibility> {
    override fun apply(states: Array<out State<Visibility>>) {
        val list = states.first().viewsGroup
        for (view in list) {
            view.visibility = View.VISIBLE
        }
    }
}