package com.mvgreen.data.network.search

import com.mvgreen.data.exception.UnexpectedResponseException
import com.mvgreen.data.network.search.api.SearchApi
import com.mvgreen.data.network.search.entity.Genre
import com.mvgreen.data.network.search.entity.MovieObject
import com.mvgreen.data.network.search.entity.SearchResponse
import com.mvgreen.data.utils.getOrUnexpected
import com.mvgreen.domain.entity.GenreData
import com.mvgreen.domain.entity.MovieContainer
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.repository.GenreStorage
import com.mvgreen.domain.repository.SearchRepository
import io.reactivex.Single
import org.joda.time.DateTime
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi,
    private val genreStorage: GenreStorage
) : SearchRepository {

    override fun loadGenres(): Single<Map<Int, GenreData>> {
        return searchApi
            .getGenreList()
            .map { result ->
                val genreList = result.genres ?: throw UnexpectedResponseException()
                genreList.associate { genre ->
                    val id = getOrUnexpected(genre.id)
                    val name = getOrUnexpected(genre.name)
                    Pair(id, GenreData(id, name))
                }
            }
    }

    override fun search(query: String, page: Int): Single<MovieContainer> {
        return searchApi
            .search(query, page)
            .convertToMovieContainer()
    }

    private fun Single<SearchResponse>.convertToMovieContainer(): Single<MovieContainer> {
        return flatMap { response ->
            // Сохраняем данные о количестве страниц чтобы включить его в итоговый объект
            val pagesTotal = getOrUnexpected(response.totalPages)
            val currentPage = getOrUnexpected(response.page)

            // Разбиваем полученные элементы и обрабатываем их по очереди
            Single.just(getOrUnexpected(response.results))
                .map { list ->
                    MovieContainer(
                        currentPage,
                        pagesTotal,
                        list
                            .map { item -> toMovieData(item) }
                            .toMutableList()
                    )
                }
        }
    }

    override fun getMovieDetails(id: Int): Single<MovieData> {
        return searchApi
            .getMovieDetails(id)
            .map { response ->
                toMovieData(response)
            }
    }

    private fun toMovieData(response: MovieObject) = MovieData(
        getOrUnexpected(response.id),
        response.posterPath,
        response.title,
        response.originalTitle,
        parseNullableDate(response.releaseDate),
        parseGenres(response.genres, response.genreIds),
        response.voteAverage,
        response.voteCount,
        response.runtime,
        response.overview
    )

    private fun parseNullableDate(str: String?): DateTime? {
        return if (str.isNullOrEmpty()) null else DateTime.parse(str)
    }

    private fun parseGenres(genreList: List<Genre>?, genreIdList: List<Int>?): List<GenreData> {
        val existingList = genreIdList ?: genreList?.map { item -> item.id }
        return existingList?.mapNotNull { id ->
            if (id == null) return@mapNotNull null
            val genre = genreStorage.getGenre(id)
            if (genre == null) null else GenreData(id, genre)
        } ?: listOf()
    }

}