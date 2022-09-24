package com.neonusa.submission1.ui.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.neonusa.submission1.core.data.source.model.Story
import com.neonusa.submission1.core.data.source.remote.network.Resource
import com.neonusa.submission1.core.data.source.remote.network.State
import com.neonusa.submission1.core.data.source.remote.response.BaseListResponse
import com.neonusa.submission1.utils.CoroutinesTestRule
import com.neonusa.submission1.utils.DataDummy
import com.neonusa.submission1.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var viewModel: MapsViewModel

    @Test
    fun `Get stories with location success`() = runTest {
        val expected = DataDummy.generateStoriesLocationResponse() // BaseListResponse<Story>
        val data = MutableLiveData<Resource<List<Story>>>()
        data.value = Resource.success(expected.listStory)

        Mockito.`when`(viewModel.storiesLocations()).thenReturn(data)

        val actual = viewModel.storiesLocations().getOrAwaitValue()
        Mockito.verify(viewModel).storiesLocations()
        advanceUntilIdle()

        assertNotNull(actual)
        assertEquals(actual.data, expected.listStory)
    }

    @Test
    fun `Get stories with location fails`() = runTest {
        val data = MutableLiveData<Resource<List<Story>>>()
        data.value = Resource.error("Error", null)
        Mockito.`when`(viewModel.storiesLocations()).thenReturn(data)

        val actual = viewModel.storiesLocations().getOrAwaitValue()
        Mockito.verify(viewModel).storiesLocations()

        advanceUntilIdle()

        assertNotNull(actual)
        assertTrue(actual.state == State.ERROR)
    }
}