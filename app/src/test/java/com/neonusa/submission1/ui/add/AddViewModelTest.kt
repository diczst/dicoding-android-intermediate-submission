package com.neonusa.submission1.ui.add

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.neonusa.submission1.core.data.source.remote.network.Resource
import com.neonusa.submission1.core.data.source.remote.network.State
import com.neonusa.submission1.core.data.source.remote.response.BasicResponse
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
class AddViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val dummyDesc = DataDummy.generateRequestBody()
    private val dummyMultipart = DataDummy.generateMultipartFile()
    private val dummyLat = DataDummy.generateRequestBody()
    private val dummylon = DataDummy.generateRequestBody()

    @Mock
    lateinit var viewModel: AddViewModel

    @Test
    fun `add story success`() = runTest {
        val expected = DataDummy.generateAddResponse()
        val data = MutableLiveData<Resource<BasicResponse>>()
        data.value = Resource.success(expected)

        Mockito.`when`(viewModel.createStory(dummyMultipart, dummyDesc,dummyLat,dummylon)).thenReturn(data)
        val actual = viewModel.createStory(dummyMultipart, dummyDesc,dummyLat,dummylon).getOrAwaitValue()
        Mockito.verify(viewModel).createStory(dummyMultipart, dummyDesc,dummyLat,dummylon)

        advanceUntilIdle()

        assertNotNull(actual)
        assertEquals(actual.data, expected)
    }

    @Test
    fun `add story fails`() = runTest {
        val data = MutableLiveData<Resource<BasicResponse>>()
        data.value = Resource.error("Error", null)

        Mockito.`when`(viewModel.createStory(dummyMultipart, dummyDesc,dummyLat,dummylon)).thenReturn(data)
        val actual = viewModel.createStory(dummyMultipart, dummyDesc,dummyLat,dummylon).getOrAwaitValue()
        Mockito.verify(viewModel).createStory(dummyMultipart, dummyDesc,dummyLat,dummylon)

        advanceUntilIdle()

        assertNotNull(actual)
        assertTrue(actual.state == State.ERROR)
    }
}