package com.rl.codingassignment.presentation.overview

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rl.codingassignment.data.api.GithubApi
import com.rl.codingassignment.data.api.model.RepositoryDTO
import com.rl.codingassignment.data.database.AppDatabase
import com.rl.codingassignment.data.mapper.toDbModel
import com.rl.codingassignment.data.repository.GithubRepository
import com.rl.codingassignment.koinModules
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val user = "abnamrocoesd"

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class RepoOverviewViewModelTest : AutoCloseKoinTest() {

    private val githubRepository by inject<GithubRepository>()
    private val mockApi = mockk<GithubApi>()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()
    private val appDatabase = Room
        .inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
        .setTransactionExecutor(testDispatcher.asExecutor())
        .setQueryExecutor(testDispatcher.asExecutor())
        .allowMainThreadQueries()
        .build()
    private val repositoryDao = appDatabase.repositoryDao()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val testModule = module {
            single<AppDatabase> { appDatabase }
            single<GithubApi> { mockApi }
            single<CoroutineDispatcher> { testDispatcher }
        }

        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(
                koinModules() + testModule,
            )
        }
    }


    @Test
    fun `When viewmodel is loaded then repositories are fetched`() = runTest {
        coEvery { mockApi.getRepositories(user, 1, 10) } returns listOf(
            RepositoryDTO.createMock(id = 1, name = "Test Repo")
        )

        val viewModel = RepoOverviewViewModel(githubRepository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        val state = viewModel.uiState.value

        coVerify { mockApi.getRepositories(user, 1, 10) }
        assertTrue(state is UiState.Content, "Expected Content but got ${state::class.simpleName}")
        assertEquals(1, state.content.size)
        assertEquals("Test Repo", state.content[0].name)
    }

    @Test
    fun `When last item on list is shown next page is fetched`() = runTest {
        // Set up mocks for pagination
        coEvery { mockApi.getRepositories(user, 1, 10) } returns
            RepositoryDTO.createMockList(count = 10, startId = 1)
        
        coEvery { mockApi.getRepositories(user, 2, 10) } returns
            RepositoryDTO.createMockList(count = 10, startId = 11)
            
        val viewModel = RepoOverviewViewModel(githubRepository)
        
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        // Simulate scrolling to the last item
        viewModel.onRepositoryVisible(9)
        
        // Check that also the second page was loaded
        coVerify(exactly = 1) { mockApi.getRepositories(user, 1, 10) }
        coVerify(exactly = 1) { mockApi.getRepositories(user, 2, 10) }
        val finalState = viewModel.uiState.value
        assertTrue(finalState is UiState.Content)
        assertEquals(20, finalState.content.size)

        // Verify database has all items
        val repos = repositoryDao.getAll().first()
        assertEquals(20, repos.size)
        assertEquals(1, repos[0].id)
        assertEquals(20, repos[19].id)
    }

    @Test
    fun `When connection fails cached results are still available`() = runTest {
        // Set up initial data in the database
        repositoryDao.insertAll(
            RepositoryDTO.createMockList(count = 5, startId = 1)
                .map { dto -> 
                    dto.copy(name = "Cached Repo ${dto.id}").toDbModel()
                }
        )

        // Mock API to fail on fetch
        coEvery { mockApi.getRepositories(user, 1, 10) } throws Exception("Network error")

        val viewModel = RepoOverviewViewModel(githubRepository)
        
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }
        
        // The API fails but we get cached data
        val state = viewModel.uiState.value
        
        assertTrue(state is UiState.Content)
        assertEquals(5, state.content.size)
        assertEquals("Cached Repo 1", state.content[0].name)
        assertEquals("Cached Repo 5", state.content[4].name)
    }
}