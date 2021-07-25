package com.example.nasaphotoday.ui.main.picture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasaphotoday.BuildConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NasaPhotoDayViewModel (
    private val liveDataForViewToObserve: MutableLiveData<NasaPhotoDayData> = MutableLiveData(),
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
    ) :
        ViewModel() {

        fun getData(date: String): LiveData<NasaPhotoDayData> {
            sendServerRequest(date)
            return liveDataForViewToObserve
        }

        private fun sendServerRequest(date: String) {
            liveDataForViewToObserve.value = NasaPhotoDayData.Loading(null)
            val apiKey: String = BuildConfig.NASA_API_KEY
            if (apiKey.isBlank()) {
                NasaPhotoDayData.Error(Throwable("You need API key"))
            } else {
                retrofitImpl.getRetrofitImpl().getNasaPhotoDay(apiKey, date).enqueue(object :
                    Callback<PODServerResponseData> {
                    override fun onResponse(
                        call: Call<PODServerResponseData>,
                        response: Response<PODServerResponseData>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveDataForViewToObserve.value =
                                NasaPhotoDayData.Success(response.body()!!)
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()) {
                                liveDataForViewToObserve.value =
                                    NasaPhotoDayData.Error(Throwable("Unidentified error"))
                            } else {
                                liveDataForViewToObserve.value =
                                    NasaPhotoDayData.Error(Throwable(message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                        liveDataForViewToObserve.value = NasaPhotoDayData.Error(t)
                    }
                })
            }
        }
    }
