package com.neonusa.submission1.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.neonusa.submission1.core.data.source.remote.network.Resource
import com.neonusa.submission1.core.data.source.remote.request.RegisterRequest
import com.neonusa.submission1.core.data.source.remote.response.BasicResponse
import com.neonusa.submission1.utils.DataDummy
import com.neonusa.submission1.utils.getOrAwaitValue
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
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

    @Mock
    lateinit var viewModel: RegisterViewModel

    @Test
    fun `when register is success`() = runTest {
        val expected = DataDummy.generateRegisterResponse()
        val request = RegisterRequest("Wolfgang Grimmer","wolfganggrimmer@mail.com","12345678")
        val data = MutableLiveData<Resource<BasicResponse>>()
        data.value = Resource.success(expected)

        Mockito.`when`(viewModel.register(request)).thenReturn(data)
        val actual = viewModel.register(request).getOrAwaitValue()
        Mockito.verify(viewModel).register(request)

        advanceUntilIdle()

        assertNotNull(actual)
        assertEquals(actual.data, expected)
    }

    @Test
    fun `when register is fails`() = runTest {
        val request = RegisterRequest("Wolfgang Steiner", "wsteiner@mail.com", "12345678")
        val data = MutableLiveData<Resource<BasicResponse>>()
        data.value = Resource.error("Error", null)

        Mockito.`when`(viewModel.register(request)).thenReturn(data)
        val actual = viewModel.register(request).getOrAwaitValue()
        Mockito.verify(viewModel).register(request)

        advanceUntilIdle()

        assertNotNull(actual)
        assertEquals(data.value, actual)
    }
}