package ca.csf.mobile2.tp1.weather
import android.os.AsyncTask
import com.beust.klaxon.Klaxon
import okhttp3.*
import java.lang.Exception


class WeatherWebService: AsyncTask<String, Void, String?>() {

    private val okHTTPClient =OkHttpClient()
    private var body: String? =""
    private var weather:Weather? = Weather("error",0,"error")
    private val myMutableListenerList: MutableList<IEventListener> = mutableListOf()

    fun registerToEvent(listener: IEventListener){
        myMutableListenerList.add(listener)
    }

    fun notifyRequestEvent(){
        for(myListener in myMutableListenerList){
            if(weather != null) {
                myListener.onWeatherFetched(weather!!)
            }
        }
    }

    fun notifyError(error: String){
        for(myListener in myMutableListenerList){
            myListener.onError(error)
        }
    }

    override fun doInBackground(vararg url: String?): String? {
         var request: Request = Request.Builder()
            .url(url[0])
            .build()

        try {
            var response: Response = okHTTPClient.newCall(request).execute()
            if(response.isSuccessful){
                body = response?.body()?.string()
            }
            response.close()
        }
        catch (e: Exception){
            notifyError("Problème de connection au service de météo.")
            throw e
        }
        return body
    }

    override fun onPostExecute(result: String?) {
        if(result != null && !result.isBlank()){
            weather = Klaxon().parse<Weather>(result)
            notifyRequestEvent()
        }
    }
}