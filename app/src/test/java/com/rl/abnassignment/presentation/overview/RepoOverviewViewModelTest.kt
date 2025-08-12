package com.rl.abnassignment.presentation.overview

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.rl.abnassignment.data.client.GithubApi
import com.rl.abnassignment.data.client.RepositoryDTO
import com.rl.abnassignment.data.database.AppDatabase
import com.rl.abnassignment.data.mapper.toDbModel
import com.rl.abnassignment.data.repository.GithubRepository
import com.rl.abnassignment.koinModules
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
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

@RunWith(RobolectricTestRunner::class)
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
        coEvery { mockApi.getRepositories("abnamrocoesd", 1, 10) } returns listOf(
            RepositoryDTO(
                id = 1,
                name = "Test Repo"
            )
        )

        val viewModel = RepoOverviewViewModel(githubRepository)
        viewModel.uiState.test {
            assertTrue(awaitItem() is UiState.Loading)
            val state = awaitItem()

            assertTrue(state is UiState.Content)
            assertEquals(1, state.content.size)
            assertEquals("Test Repo", state.content[0].name)
            coVerify { mockApi.getRepositories("abnamrocoesd", 1, 10) }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `When last item on list is shown next page is fetched`() = runTest {
        // Set up mocks for pagination
        coEvery { mockApi.getRepositories("abnamrocoesd", 1, 10) } returns List(10) { i ->
            RepositoryDTO(id = i + 1, name = "Test Repo ${i + 1}")
        }
        coEvery { mockApi.getRepositories("abnamrocoesd", 2, 10) } returns List(10) { i ->
            RepositoryDTO(id = 10 + i + 1, name = "Test Repo ${10 + i + 1}")
        }
        val viewModel = RepoOverviewViewModel(githubRepository)

        viewModel.uiState.test {
            val initial = awaitItem()
            assertTrue(initial is UiState.Content)
            assertEquals(10, initial.content.size)
            coVerify(exactly = 1) { mockApi.getRepositories("abnamrocoesd", 1, 10) }
            coVerify(exactly = 0) { mockApi.getRepositories("abnamrocoesd", 2, 10) }

            // Simulate scrolling to the last item
            viewModel.onRepositoryVisible(9)

            coVerify(exactly = 1) { mockApi.getRepositories("abnamrocoesd", 2, 10) }
            val loadingItem = awaitItem()
            assertTrue(loadingItem is UiState.Content && loadingItem.isLoadingMore)
            val after = awaitItem()
            assertTrue(after is UiState.Content)
            assertEquals(20, after.content.size)

            cancelAndIgnoreRemainingEvents()
        }

        repositoryDao.getAll().test {
            val repos = awaitItem()
            assertEquals(20, repos.size)
            assertEquals("Test Repo 1", repos[0].name)
            assertEquals("Test Repo 20", repos[19].name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `When connection fails cached results are still available`() = runTest {
        // Set up initial data in the database
        repositoryDao.insertAll(
            List(5) { i ->
                RepositoryDTO(id = i + 1, name = "Cached Repo ${i + 1}").toDbModel()
            }
        )

        // Mock API to fail on fetch
        coEvery { mockApi.getRepositories("abnamrocoesd", 1, 10) } throws Exception("Network error")

        val viewModel = RepoOverviewViewModel(githubRepository)
        viewModel.uiState.test {
            val state = awaitItem()

            assertTrue(state is UiState.Content)
            assertEquals(5, state.content.size)
            assertEquals("Cached Repo 1", state.content[0].name)
            assertEquals("Cached Repo 5", state.content[4].name)

            cancelAndIgnoreRemainingEvents()
        }
    }
}