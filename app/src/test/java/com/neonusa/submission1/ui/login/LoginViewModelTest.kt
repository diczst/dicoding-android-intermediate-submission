package com.neonusa.submission1.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.neonusa.submission1.core.data.source.model.User
import com.neonusa.submission1.core.data.source.remote.network.Resource
import com.neonusa.submission1.core.data.source.remote.network.State
import com.neonusa.submission1.core.data.source.remote.request.LoginRequest
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
class LoginViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val loginResponse = DataDummy.generateLoginResponse()

    @Mock
    lateinit var viewModel: LoginViewModel

    @Test
    fun `when login is success`() = runTest {
        val expectedResponse = loginResponse
        val data = MutableLiveData<Resource<User>>()
        data.value = Resource(State.SUCCESS,expectedResponse.loginResult,"Success")

        val request = LoginRequest("jolibert@mail.com", "12345678")
        Mockito.`when`(viewModel.login(request)).thenReturn(data)
        val actual = viewModel.login(request).getOrAwaitValue()
        Mockito.verify(viewModel).login(request)
        advanceUntilIdle()
        assertNotNull(actual)
        assertEquals(actual.data, expectedResponse.loginResult)
    }

    @Test
    fun `when login is fails`() = runTest {
        val data = MutableLiveData<Resource<User>>()
        data.value = Resource.error("Error", null)
        val request = LoginRequest("email@mail.com", "password")
        Mockito.`when`(viewModel.login(request)).thenReturn(data)
        val actual = viewModel.login(request).getOrAwaitValue()
        Mockito.verify(viewModel).login(request)

        advanceUntilIdle()

        assertNotNull(actual)
        assertEquals(data.value, actual)
    }
}