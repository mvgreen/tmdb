package com.mvgreen.tmdbapp.ui.recycler

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvgreen.domain.bean.ListMode
import com.mvgreen.domain.bean.ListMode.Companion.LIST_MODE_GRID
import com.mvgreen.domain.bean.ListMode.Companion.LIST_MODE_LINEAR
import com.mvgreen.tmdbapp.R

class ListModeImpl : ListMode {

    override var modeId: Int = LIST_MODE_LINEAR

    override val listPosition: Int
        get() {
            // Присваиваем для сохранения возможности каста к нужному типу
            val position = when (val manager = layoutManager) {
                is LinearLayoutManager -> manager.findFirstVisibleItemPosition()
                is GridLayoutManager -> manager.findFirstVisibleItemPosition()
                else -> throw IllegalStateException()
            }
            return position
        }

    lateinit var layoutManager: RecyclerView.LayoutManager

    override fun nextModeId(): Int {
        return when(modeId) {
            LIST_MODE_LINEAR -> LIST_MODE_GRID
            LIST_MODE_GRID -> LIST_MODE_LINEAR
            else -> throw IllegalStateException()
        }
    }

    fun getMarginDecoration(resources: Resources): RecyclerView.ItemDecoration {
        return when(modeId) {
            LIST_MODE_LINEAR -> linearMarginDecoration(resources)
            LIST_MODE_GRID -> gridMarginDecoration(resources)
            else -> throw IllegalStateException()
        }
    }

    private fun linearMarginDecoration(resources: Resources) =
        object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.bottom = resources.getDimension(R.dimen.recycler_margin).toInt()
            }
        }

    private fun gridMarginDecoration(resources: Resources) =
        object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.left = resources.getDimension(R.dimen.recycler_margin).toInt()
                outRect.right = resources.getDimension(R.dimen.recycler_margin).toInt()
                outRect.bottom = resources.getDimension(R.dimen.recycler_margin).toInt()
            }
        }
}