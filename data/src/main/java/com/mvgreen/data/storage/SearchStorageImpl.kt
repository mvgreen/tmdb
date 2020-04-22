package com.mvgreen.data.storage

import androidx.paging.PagedList
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.repository.SearchStorage
import javax.inject.Inject

class SearchStorageImpl @Inject constructor(): SearchStorage {

    override var savedList: PagedList<MovieData>? = null

    override var savedListPosition: Int = 0

    override var savedQuery: String = ""

}