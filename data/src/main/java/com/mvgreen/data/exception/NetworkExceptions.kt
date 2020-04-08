package com.mvgreen.data.exception

open class NetworkException(cause: Throwable? = null) : Exception(cause)

class ConnectionException(cause: Throwable) : NetworkException(cause)

class ServerException : NetworkException()

class UnexpectedResponseException : NetworkException()

class InvalidInputException(cause: Throwable) : NetworkException(cause)

class CredentialsException : NetworkException()

