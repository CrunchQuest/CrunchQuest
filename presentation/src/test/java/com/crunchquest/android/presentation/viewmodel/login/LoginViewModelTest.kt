package com.crunchquest.presentation.viewmodel.login


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.crunchquest.domain.entities.User
import com.crunchquest.android.domain.usecase.user.LoginUseCase
import com.crunchquest.presentation.ui.login.LoginState
import com.crunchquest.presentation.ui.login.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val loginUseCase = mock(com.crunchquest.android.domain.usecase.user.LoginUseCase::class.java)
    private val loginViewModel = LoginViewModel(loginUseCase)

    @Test
    fun `login success updates state to Success`() = runBlockingTest {
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

        `when`(loginUseCase(email, password)).thenReturn(Result.success(user))

        val observer = mock(Observer::class.java) as Observer<LoginState>
        loginViewModel.loginState.observeForever(observer)

        loginViewModel.login(email, password)

        verify(observer).onChanged(LoginState.Loading)
        verify(observer).onChanged(LoginState.Success(user))
    }

    @Test
    fun `login failure updates state to Error`() = runBlockingTest {
        val email = "test@example.com"
        val password = "wrongpassword"
        val errorMessage = "Login failed"

        `when`(loginUseCase(email, password)).thenReturn(Result.failure(Exception(errorMessage)))

        val observer = mock(Observer::class.java) as Observer<LoginState>
        loginViewModel.loginState.observeForever(observer)

        loginViewModel.login(email, password)

        verify(observer).onChanged(LoginState.Loading)
        verify(observer).onChanged(LoginState.Error(errorMessage))
    }
}