package com.example.vmlogin.retrofit.service

abstract class BaseService {
    class InvalidDataError(message: String, cause: Throwable) : Throwable(message, cause)
    class ConnectionError(message: String, cause: Throwable) : Throwable(message, cause)
    class UnexpectedError(message: String, cause: Throwable) : Throwable(message, cause)
}