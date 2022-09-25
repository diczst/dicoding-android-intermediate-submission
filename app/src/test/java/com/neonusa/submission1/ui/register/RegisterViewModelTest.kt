package com.neonusa.submission1.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.neonusa.submission1.core.data.repository.AppRepository
import com.neonusa.submission1.core.data.source.remote.network.Resource
import com.neonusa.submission1.core.data.source.remote.request.RegisterRequest
import com.neonusa.submission1.core.data.source.remote.response.BasicResponse
import com.neonusa.submission1.ui.add.AddViewModel
import com.neonusa.submission1.utils.CoroutinesTestRule
import com.neonusa.submission1.utils.DataDummy
import com.neonusa.submission1.utils.getOrAwaitValue
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    lateinit var viewModel: RegisterViewModel

    @Mock
    lateinit var repository:AppRepository

    @Before
    fun setup() {
        viewModel = RegisterViewModel(repository)
    }

    @Test
    fun `when register is success`() = runTest {
        val request = RegisterRequest("Wolfgang Grimmer","wolfganggrimmer@mail.com","12345678")
        val expected = flowOf(
            Resource.success(DataDummy.generateRegisterResponse())
        )

        Mockito.`when`(repository.register(request)).thenReturn(expected)
        val actual = viewModel.register(request).getOrAwaitValue()
        expected.asLiveData().observeForever {
            assertNotNull(actual)
            assertEquals(actual.data, it.data)
        }
        Mockito.verify(repository).register(request)
    }

    @Test
    fun `when register is fails`() = runTest {
        val request = RegisterRequest("Wolfgang Grimmer","wsteiner@mail.com","12345678")
        val expected = flowOf(
            Resource.error("Error",null)
        )

        Mockito.`when`(repository.register(request)).thenReturn(expected)
        val actual = viewModel.register(request).getOrAwaitValue()
        Mockito.verify(repository).register(request)

        expected.asLiveData().observeForever {
            assertNotNull(actual)
            assertEquals(actual.message, it.message)
        }
    }
}