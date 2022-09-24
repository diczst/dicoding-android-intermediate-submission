package com.neonusa.submission1.core.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.neonusa.submission1.adapter.StoryListAdapter
import com.neonusa.submission1.core.data.source.remote.network.Resource
import com.neonusa.submission1.core.data.source.remote.network.State
import com.neonusa.submission1.core.data.source.remote.request.LoginRequest
import com.neonusa.submission1.core.data.source.remote.request.RegisterRequest
import com.neonusa.submission1.utils.CoroutinesTestRule
import com.neonusa.submission1.utils.DataDummy
import com.neonusa.submission1.utils.PagedTestDataSource
import com.neonusa.submission1.utils.ServiceDummy
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AppRepositoryTest {
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var repoMock: AppRepository

    private val loginRequest = LoginRequest("johan@mail.com", "12345678")
    private val loginResponse = DataDummy.generateLoginResponse()

    private val registerRequest = RegisterRequest("Wolfgang Grimmer","wolfganggrimmer@mail.com","12345678")
    private val registerResponse = DataDummy.generateRegisterResponse()

    private val addResponse = DataDummy.generateAddResponse()
    private val dummyDesc = DataDummy.generateRequestBody()
    private val dummyMultipart = DataDummy.generateMultipartFile()

    @Test
    fun `when login is success`() = runBlocking {
        val service = ServiceDummy()
        val expectedResponse = loginResponse
        val actualResponse = service.login(loginRequest).body()
        assertNotNull(actualResponse)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `when login is failed`() = runBlocking {
        val service = ServiceDummy()
        val expected = DataDummy.generateLoginError()
        val actual = service.loginError()
        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun `when register is success`() = runBlocking {
        val service = ServiceDummy()
        val expected = registerResponse
        val actual = service.register(registerRequest).body()
        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun `when register is failed`() = runBlocking {
        val service = ServiceDummy()
        val expected = DataDummy.generateRegisterError()
        val actual = service.registerError()
        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun `Get stories location success`() = runBlocking {
        val expected = flowOf(
            Resource.success(DataDummy.generateStoriesLocationResponse().listStory)
        )
        Mockito.`when`(repoMock.getStoriesLocations()).thenReturn(expected)
        repoMock.getStoriesLocations().collect { result ->
            if (result.state == State.SUCCESS) {
                assertNotNull(result)
                expected.collect {
                    assertEquals(it, result)
                }
            }
        }
    }

    @Test
    fun `Get stories location fails`() = runBlocking {
        val expectedResult = flowOf(Resource.error("Error", null))
        Mockito.`when`(repoMock.getStoriesLocations()).thenReturn(expectedResult)
        repoMock.getStoriesLocations().collect { result ->
            if (result.state == State.ERROR) {
                assertNotNull(result)
                expectedResult.collect{
                    assertEquals(it, result)
                }
            }
        }
    }

    @Test
    fun `add story success`(): Unit = runBlocking {
        val service = ServiceDummy()
        val expected = addResponse
        val actual = service.createStory(dummyMultipart, dummyDesc, null, null).body()
        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun `add story fails`(): Unit = runBlocking {
        val service = ServiceDummy()
        val expected = DataDummy.generateAddError()
        val actual = service.uploadError()
        assertNotNull(actual)
        assertEquals(expected, actual)
    }


    @Test
    fun `get paginated storied success`() = runTest {
        val dummyData = DataDummy.generateStories()
        val data = PagedTestDataSource.snapshot(dummyData)
        val expected = flowOf(data)

        val response = DataDummy.generateStoriesLocationResponse()

        Mockito.`when`(repoMock.getPaginatedStories()).thenReturn(expected)
        repoMock.getPaginatedStories().collect{actual ->
            val differ = AsyncPagingDataDiffer(
                diffCallback = StoryListAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = coroutinesTestRule.testDispatcher,
                workerDispatcher = coroutinesTestRule.testDispatcher
            )
            differ.submitData(actual)
            assertNotNull(differ.snapshot())
            assertEquals(response.listStory.size, differ.snapshot().size)
        }
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

}