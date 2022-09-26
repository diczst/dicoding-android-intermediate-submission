package com.neonusa.submission1.core.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.neonusa.submission1.adapter.StoryListAdapter
import com.neonusa.submission1.core.data.source.model.Story
import com.neonusa.submission1.core.data.source.remote.RemoteDataSource
import com.neonusa.submission1.core.data.source.remote.network.Resource
import com.neonusa.submission1.core.data.source.remote.network.State
import com.neonusa.submission1.core.data.source.remote.request.LoginRequest
import com.neonusa.submission1.core.data.source.remote.request.RegisterRequest
import com.neonusa.submission1.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AppRepositoryTest {
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val loginRequest = LoginRequest("johan@mail.com", "12345678")

    private val registerRequest = RegisterRequest("Wolfgang Grimmer","wolfganggrimmer@mail.com","12345678")
    private val registerResponse = DataDummy.generateRegisterResponse()

    private val addResponse = DataDummy.generateAddResponse()
    private val dummyDesc = DataDummy.generateRequestBody()
    private val dummyMultipart = DataDummy.generateMultipartFile()

    lateinit var appRepository: AppRepository

    @Mock
    lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setup() {
        appRepository = AppRepository(remoteDataSource)
    }

    @Test
    fun `when login is success`() = runTest {
        val expected = flowOf(
            Resource.success(DataDummy.generateLoginResponse().loginResult)
        )
        appRepository.login(loginRequest).collect { actual ->
            if (actual.state == State.SUCCESS) {
                assertNotNull(actual)
                expected.collect {
                    assertEquals(it, actual)
                }
            }
        }
    }

    @Test
    fun `when login is failed`() = runBlocking {
        val expected = flowOf(
            Resource.error("Error",DataDummy.generateLoginError())
        )

        appRepository.login(loginRequest).collect { actual ->
            if (actual.state == State.ERROR){
                assertNotNull(actual)
                expected.collect {
                    assertEquals(it.message, actual.message)
                }
            }
        }
    }

    @Test
    fun `when register is success`() = runBlocking {
        val expected = flowOf(
            Resource.success(DataDummy.generateRegisterResponse())
        )
        appRepository.register(registerRequest).collect { actual ->
            if (actual.state == State.SUCCESS) {
                assertNotNull(actual)
                expected.collect {
                    assertEquals(it, actual)
                }
            }
        }
    }

    @Test
    fun `when register is failed`() = runBlocking {
        val expected = flowOf(
            Resource.error("Error",null)
        )
        appRepository.register(registerRequest).collect { actual ->
            if (actual.state == State.ERROR) {
                assertNotNull(actual)
                expected.collect {
                    assertEquals(it, actual)
                }
            }
        }
    }

    @Test
    fun `Get stories location success`() = runBlocking {
        val expected = flowOf(
            Resource.success(DataDummy.generateStoriesLocationResponse().listStory)
        )
        appRepository.getStoriesLocations().collect { result ->
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
        val expected = flowOf(Resource.error("Error", null))
        appRepository.getStoriesLocations().collect { result ->
            if (result.state == State.ERROR) {
                assertNotNull(result)
                expected.collect{
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
    fun `Get paginated stories success`() = runTest {
        val dataDummy = DataDummy.generateStories()
        val data = PagedTestDataSource.snapshot(dataDummy)

        val stories = MutableLiveData<PagingData<Story>>()
        stories.value = data

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = coroutinesTestRule.testDispatcher,
            workerDispatcher = coroutinesTestRule.testDispatcher
        )
        differ.submitData(data)
        advanceUntilIdle()

        assertNotNull(differ.snapshot())
        assertEquals(dataDummy.size, differ.snapshot().size)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

}