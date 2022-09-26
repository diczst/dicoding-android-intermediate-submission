package com.neonusa.submission1.ui.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.neonusa.submission1.core.data.repository.AppRepository
import com.neonusa.submission1.core.data.source.model.Story
import com.neonusa.submission1.core.data.source.remote.network.Resource
import com.neonusa.submission1.core.data.source.remote.network.State
import com.neonusa.submission1.core.data.source.remote.response.BaseListResponse
import com.neonusa.submission1.utils.CoroutinesTestRule
import com.neonusa.submission1.utils.DataDummy
import com.neonusa.submission1.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
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
    lateinit var repository: AppRepository

    lateinit var viewModel: MapsViewModel

    @Before
    fun setup(){
        viewModel = MapsViewModel(repository)
    }


    @Test
    fun `Get stories with location success`() = runTest {
        val expected = flowOf(
            Resource.success(DataDummy.generateStoriesLocationResponse().listStory)
        )

        Mockito.`when`(repository.getStoriesLocations()).thenReturn(expected)

        val actual = viewModel.storiesLocations().getOrAwaitValue()
        Mockito.verify(repository).getStoriesLocations()

        expected.collect{
            assertNotNull(actual)
            assertEquals(actual.data, it.data)
        }
    }

    @Test
    fun `Get stories with location fails`() = runTest {
        val expected = flowOf(
            Resource.error("Error",null)
        )

        Mockito.`when`(repository.getStoriesLocations()).thenReturn(expected)

        val actual = viewModel.storiesLocations().getOrAwaitValue()
        Mockito.verify(repository).getStoriesLocations()

        expected.collect{
            assertNotNull(actual)
            assertTrue(actual.state == State.ERROR)
        }

    }
}