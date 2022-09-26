package com.neonusa.submission1.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.neonusa.submission1.core.data.repository.AppRepository
import com.neonusa.submission1.core.data.source.model.User
import com.neonusa.submission1.core.data.source.remote.network.Resource
import com.neonusa.submission1.core.data.source.remote.network.State
import com.neonusa.submission1.core.data.source.remote.request.LoginRequest
import com.neonusa.submission1.ui.register.RegisterViewModel
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
class LoginViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var repository: AppRepository
    lateinit var viewModel: LoginViewModel

    @Before
    fun setup(){
        viewModel = LoginViewModel(repository)
    }

    @Test
    fun `when login is success`() = runTest {
        val request = LoginRequest("jolibert@mail.com", "12345678")
        val expected = flowOf(
            Resource.success(DataDummy.generateLoginResponse().loginResult)
        )

        Mockito.`when`(repository.login(request)).thenReturn(expected)

        val actual = viewModel.login(request).getOrAwaitValue()

        expected.collect{
            assertNotNull(actual)
            assertEquals(actual.data, it.data)
        }
        Mockito.verify(repository).login(request)
    }

    @Test
    fun `when login is fails`() = runTest {
        val request = LoginRequest("jolibert@mail.com", "12345678")
        val expected = flowOf(
            Resource.error("Error",null)
        )

        Mockito.`when`(repository.login(request)).thenReturn(expected)

        val actual = viewModel.login(request).getOrAwaitValue()
        expected.collect{
            assertNotNull(actual)
            assertEquals(actual.message, it.message)
        }
        Mockito.verify(repository).login(request)
    }
}