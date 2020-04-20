package com.mvgreen.tmdbapp.ui.delegator

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.redmadrobot.lib.sd.StubState
import com.redmadrobot.lib.sd.ZeroStubView

class StubView(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), ZeroStubView {

    lateinit var targetState: StubState

    override fun setUpZero(state: StubState) {
        if (state != targetState) {
            visibility = View.GONE
        }
    }

}