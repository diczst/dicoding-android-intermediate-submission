package com.neonusa.submission1.core.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.neonusa.submission1.core.data.source.model.Story
import com.neonusa.submission1.core.data.source.remote.RemoteDataSource

class StoryPagingSource(private val remoteDataSource: RemoteDataSource) : PagingSource<Int, Story>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = remoteDataSource.getPaginatedStories(page, params.loadSize)
            Log.i("StoryPagingSource", "load: $responseData")
            LoadResult.Page(
                data = responseData.body()!!.listStory,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.body()?.listStory.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}