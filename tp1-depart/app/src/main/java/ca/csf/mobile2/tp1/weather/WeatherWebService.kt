package ca.csf.mobile2.tp1.weather

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.*
import java.io.IOException

class WeatherWebService{

    private val okHTTPClient : OkHttpClient = OkHttpClient()

    private var request: Request = Request.Builder()
        .url("https://m2t1.csfpwmjv.tk/api/v1/weather")
        .build()

    fun callResquest() : String {
        var body:String
        try {
            var response: Response = okHTTPClient.newCall(request).execute()
            if(response.isSuccessful){
                /blabla TODO SUCK MY ICK
            }
            else{

            }

        }
        catch (e){

        }


    }
}