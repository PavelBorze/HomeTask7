package com.pavelb.hometask.domain.entities

data class ErrorEntity (
    val message: String,
    val type: ErrorType = ErrorType.UNKNOWN
    )

enum class ErrorType {
    NETWORK,
    AUTH,
    ENCRYPTION,
    UNKNOWN
}