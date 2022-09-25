package com.neonusa.submission1.ui.add

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import com.neonusa.submission1.core.data.repository.AppRepository
import com.neonusa.submission1.core.data.source.remote.network.Resource
import com.neonusa.submission1.core.data.source.remote.network.State
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
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val dummyDesc = DataDummy.generateRequestBody()
    private val dummyMultipart = DataDummy.generateMultipartFile()
    private val dummyLat = DataDummy.generateRequestBody()
    private val dummylon = DataDummy.generateRequestBody()

    lateinit var viewModel: AddViewModel

    // mock untuk argument viewmodel
    @Mock
    lateinit var repository: AppRepository

    @Before
    fun setup() {
        viewModel = AddViewModel(repository)
    }

    @Test
    fun `add story success`() = runTest {
        val expected = flowOf(
            Resource.success(DataDummy.generateAddResponse())
        )

        `when`(repository.addStory(dummyMultipart, dummyDesc,dummyLat,dummylon)).thenReturn(expected)

        val actual = viewModel.createStory(dummyMultipart, dummyDesc,dummyLat,dummylon).getOrAwaitValue()
        Mockito.verify(repository).addStory(dummyMultipart, dummyDesc,dummyLat,dummylon)
        advanceUntilIdle()
        // observeForever tidak perlu lifecycle
        expected.asLiveData().observeForever {
            assertNotNull(actual)
            assertEquals(it.data, actual.data)
        }
    }


    @Test
    fun `add story fails`() = runTest {
        val expected = flowOf(
            Resource.error("Error",null)
        )

        `when`(repository.addStory(dummyMultipart, dummyDesc,dummyLat,dummylon)).thenReturn(expected)
        val actual = viewModel.createStory(dummyMultipart, dummyDesc,dummyLat,dummylon).getOrAwaitValue()
        Mockito.verify(repository).addStory(dummyMultipart, dummyDesc,dummyLat,dummylon)

        expected.asLiveData().observeForever {
            assertNotNull(actual)
            assertTrue(actual.state == State.ERROR)
        }
    }
}