package com.crunchquest.android.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.crunchquest.android.domain.repository.UserRepository
import com.crunchquest.android.domain.usecase.user.LoginUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class LoginUseCaseTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val userRepository = mock(com.crunchquest.android.domain.repository.UserRepository::class.java)
    private val loginUseCase =
        com.crunchquest.android.domain.usecase.user.LoginUseCase(userRepository)

    @Test
    fun `login success returns user`() = runBlockingTest {
        val email = "test@example.com"
        val password = "password"
        val user = com.crunchquest.domain.entities.User(
            "1",
            email,
            "password",
            "Doe",
            "John",
            "1234567890",
            4.8,
            "Apt 1",
            "Anytown",
            "CA",
            "12345",
            "USA",
            "1234567890",
            "123 Main St",
            "Apt 1",
            "10201",
            "Active",
            "Customer",
            "Active",
            "Expert",
        )

        `when`(userRepository.login(email, password)).thenReturn(Result.success(user))

        val result = loginUseCase(email, password)

        assertEquals(Result.success(user), result)
    }

    @Test
    fun `login failure returns error`() = runBlockingTest {
        val email = "test@example.com"
        val password = "wrongpassword"
        val exception = Exception("Login failed")

        `when`(userRepository.login(email, password)).thenReturn(Result.failure(exception))

        val result = loginUseCase(email, password)

        assertEquals(Result.failure<com.crunchquest.domain.entities.User>(exception), result)
    }
}