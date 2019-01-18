package ca.csf.mobile2.tp1.weather

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import ca.csf.mobile2.tp1.R
import kotlinx.android.synthetic.main.activity_weather.view.*
import java.util.*
import android.net.NetworkInfo



class WeatherActivity: AppCompatActivity(), IEventListener{

    private lateinit var temperatureTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var weatherPreviewImageView : ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorImageView: ImageView
    private lateinit var errorTextView: TextView
    private lateinit var retryButton: Button
    private lateinit var connectivityErrorString : String
    private lateinit var serverErrorString : String

    private var language = Locale.getDefault().getDisplayLanguage();
    private var weatherWebService = WeatherWebService()
    private var weather = Weather("error",0,"error")


    private var WEATHER_TEMPERATURE_VALUE = "weatherTemperatureValue"
    private var WEATHER_CITY_VALUE = "weatherCityValue"
    private var WEATHER_TYPE_VALUE = "weatherTypeValue"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        temperatureTextView = findViewById(R.id.temperatureTextView)
        cityTextView = findViewById(R.id.cityTextView)
        weatherPreviewImageView = findViewById(R.id.weatherPreviewImageView)
        progressBar = findViewById(R.id.progressBar)
        errorImageView = findViewById(R.id.errorImageView)
        errorTextView = findViewById(R.id.errorTextView)
        retryButton = findViewById(R.id.retryButton)

        retryButton.setOnClickListener {
            retryButton()
        }

        if(language.equals("English")){
            connectivityErrorString = getString(R.string.error_connectivityEN);
            serverErrorString = getString(R.string.error_serverEN);
            retryButton.setText(getString(R.string.text_retryEN));
        }
        else if(language.equals("French")){
            connectivityErrorString = getString(R.string.error_connectivityFR);
            serverErrorString = getString(R.string.error_connectivityEN);
            retryButton.setText(getString(R.string.text_retryFR));
        }

        weatherWebService.registerToEvent(this)
        if(savedInstanceState == null){
           getWeather()
        }




    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putInt(WEATHER_TEMPERATURE_VALUE,weather.temperatureInCelsius)
        outState!!.putString(WEATHER_CITY_VALUE,weather.city)
        outState!!.putString(WEATHER_TYPE_VALUE,weather.type)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if(savedInstanceState != null){
            weather = Weather(savedInstanceState.getString(WEATHER_TYPE_VALUE),savedInstanceState.getInt(WEATHER_TEMPERATURE_VALUE),savedInstanceState.getString(WEATHER_CITY_VALUE))
            applyWeatherInfo()
            showWeather()
        }
    }

    override fun onWeatherFetched(weather: Weather) {
        this.weather = weather
        applyWeatherInfo()
        showWeather()
    }

    override fun onError(error : String){
        applyErrorInfo(serverErrorString)
        showError()
    }

    private fun isConnectedToInternet() : Boolean{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun getWeather(){
        showLoading()
        if(isConnectedToInternet()){
            weatherWebService.execute("https://m2t1.csfpwmjv.tk/api/v1/weather")
        }
        else{
            applyErrorInfo(connectivityErrorString)
            showError()
        }
    }

    private fun showWeather(){
        progressBar.visibility = View.INVISIBLE
        temperatureTextView.visibility = View.VISIBLE
        cityTextView.visibility = View.VISIBLE
        weatherPreviewImageView.visibility = View.VISIBLE
        errorImageView.visibility = View.INVISIBLE
        errorTextView.visibility = View.INVISIBLE
        retryButton.visibility = View.INVISIBLE
    }

    private fun showLoading(){
        progressBar.visibility = View.VISIBLE
        temperatureTextView.visibility = View.INVISIBLE
        cityTextView.visibility = View.INVISIBLE
        weatherPreviewImageView.visibility = View.INVISIBLE
        errorImageView.visibility = View.INVISIBLE
        errorTextView.visibility = View.INVISIBLE
        retryButton.visibility = View.INVISIBLE
    }

    private fun showError(){
        progressBar.visibility = View.INVISIBLE
        temperatureTextView.visibility = View.INVISIBLE
        cityTextView.visibility = View.INVISIBLE
        weatherPreviewImageView.visibility = View.INVISIBLE
        errorImageView.visibility = View.VISIBLE
        errorTextView.visibility = View.VISIBLE
        retryButton.visibility = View.VISIBLE
    }

    private fun applyWeatherInfo(){
        cityTextView.text = weather.city
        temperatureTextView.text = weather.temperatureInCelsius.toString()
        when(weather.type){
            WeatherType.PARTLY_SUNNY.toString() -> weatherPreviewImageView.setImageDrawable(getDrawable(R.drawable.ic_partly_sunny))
            WeatherType.CLOUDY.toString() -> weatherPreviewImageView.setImageDrawable(getDrawable(R.drawable.ic_cloudy))
            WeatherType.SUNNY.toString() -> weatherPreviewImageView.setImageDrawable(getDrawable(R.drawable.ic_sunny))
            WeatherType.RAIN.toString() -> weatherPreviewImageView.setImageDrawable(getDrawable(R.drawable.ic_rain))
            WeatherType.SNOW.toString() -> weatherPreviewImageView.setImageDrawable(getDrawable(R.drawable.ic_snow))
        }

    }

    private fun applyErrorInfo(error: String){
        errorTextView.text = error
    }

    private fun retryButton(){
        getWeather()
    }



}