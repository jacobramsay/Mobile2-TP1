package ca.csf.mobile2.tp1.weather

 interface IEventListener {
     fun onWeatherFetched(weather: Weather)
     fun onError(error:String)
}