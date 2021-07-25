package com.example.nasaphotoday.ui.main.picture

sealed class NasaPhotoDayData {
    data class Success(val serverResponseData: PODServerResponseData) : NasaPhotoDayData()
    data class Error(val error: Throwable) : NasaPhotoDayData()
    data class Loading(val progress: Int?) : NasaPhotoDayData()
}