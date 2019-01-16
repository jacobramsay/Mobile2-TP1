package ca.csf.mobile2.tp1.weather

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import ca.csf.mobile2.tp1.R
import kotlinx.android.synthetic.main.activity_weather.view.*

class WeatherActivity: AppCompatActivity(), IEventListener{

    private lateinit var temperatureTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var weatherPreviewImageView : ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorImageView: ImageView
    private lateinit var errorTextView: TextView
    private lateinit var retryButton: Button

    private var weatherWebService = WeatherWebService()
    private var weather = Weather("error",0,"error")

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

        weatherWebService.registerToEvent(this)

        getWeather()
    }

    override fun onWeatherFetched(weather: Weather) {
        this.weather = weather
        applyWeatherInfo()
        showWeather()
    }

    override fun onError(error:String){
        applyErrorInfo(error)
        showError()
    }

    private fun isConnectedToInternet() : Boolean{
        return (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo.isConnected
    }

    private fun getWeather(){
        showLoading()
        if(isConnectedToInternet()){
            weatherWebService.execute("https://m2t1.csfpwmjv.tk/api/v1/weather")
        }
        else{
            applyErrorInfo("You are not connected to the internet")
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
        //TODO: Faire que l'image change selon le type soit SUNNY, PARTLY_SUNNY, CLOUDY, RAIN ou SNOW
    }

    private fun applyErrorInfo(error: String){
        errorTextView.text = error
    }

    private fun retryButton(){
        getWeather()
    }



}