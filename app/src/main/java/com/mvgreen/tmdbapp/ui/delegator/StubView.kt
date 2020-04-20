package com.mvgreen.tmdbapp.ui.delegator

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.mvgreen.tmdbapp.R
import com.redmadrobot.lib.sd.StubState
import com.redmadrobot.lib.sd.ZeroStubView
import kotlinx.android.synthetic.main.fragment_search.view.*

class StubView(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), ZeroStubView {

    lateinit var targetStates: List<StubState>

    override fun setUpZero(state: StubState) {
        if (state !in targetStates) {
            visibility = View.GONE
            return
        }
        zero_message?.text = when (state) {
            ErrorState -> resources.getString(R.string.check_connection)
            EmptyResponseState -> resources.getString(R.string.empty_response)
            else -> resources.getString(R.string.error_unknown)
        }
    }

}