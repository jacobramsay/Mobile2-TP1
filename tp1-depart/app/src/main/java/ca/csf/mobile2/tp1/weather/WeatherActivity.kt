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

class WeatherActivity : AppCompatActivity() {

    private lateinit var temperatureTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var weatherPreviewImageView : ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorImageView: ImageView
    private lateinit var errorTextView: TextView
    private lateinit var retryButton: Button

    private var weatherWebService = WeatherWebService()

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

        progressBar.visibility = View.VISIBLE
        temperatureTextView.visibility = View.INVISIBLE
        cityTextView.visibility = View.INVISIBLE
        weatherPreviewImageView.visibility = View.INVISIBLE
        errorImageView.visibility = View.INVISIBLE
        errorTextView.visibility = View.INVISIBLE
        retryButton.visibility = View.INVISIBLE

        getWeather()
    }

    private fun isConnectedToInternet() : Boolean{
        return (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo.isConnected
    }

    private fun getWeather(){
        if(isConnectedToInternet()){
            weatherWebService.callResquest()
        }
        else{
            // No internet connection error
        }
    }

}