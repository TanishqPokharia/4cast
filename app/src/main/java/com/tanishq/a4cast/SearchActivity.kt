package com.tanishq.a4cast
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.SearchView
import android.widget.Toast
import com.tanishq.a4cast.databinding.ActivityMainBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class SearchActivity : AppCompatActivity() {
    private var x1:Float = 0.0f
    private var x2:Float = 0.0f
    private var y1:Float = 0.0f
    private var y2:Float = 0.0f

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrieveWeatherData("chennai")
        SearchCity()
    }

    private fun SearchCity() {
        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(city: String?): Boolean {
                if (city != null) {
                    retrieveWeatherData(city)
                }
                return true
            }
        })
    }


    private fun retrieveWeatherData(cityName:String) {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/").build()
            .create(OpenWeatherAPIInterface::class.java)

        val response = retrofit.getWeatherData(cityName,"7b2b5e2fd4eeaf7ca74c33f51aa97b7d","metric")
        response.enqueue(object :Callback<forecast>{
            override fun onResponse(call: Call<forecast>, response: Response<forecast>) {
                val responseBody = response.body()
                if(response.isSuccessful && responseBody!=null){
                    val temperature = responseBody.main.temp.toString()
                    val mintemp = responseBody.main.temp_min.toString()
                    val maxtemp = responseBody.main.temp_max.toString()
                    val sea = responseBody.main.pressure.toString()
                    val condition = responseBody.weather.firstOrNull()?.main?:"unknown"
                    val humidity = responseBody.main.humidity.toString()
                    val wind = responseBody.wind.speed.toString()
                    val sunrise = responseBody.sys.sunrise.toLong()
                    val sunset = responseBody.sys.sunset.toLong()
                    binding.txtweather.text = condition
                    binding.txtTemp.text = "$temperature °C"
                    binding.txtMax.text = "Max:$maxtemp °C"
                    binding.txtMin.text = "Min:$mintemp °C"
                    binding.txtHumidity.text = "$humidity %"
                    binding.txtWind.text = "$wind m/s"
                    binding.txtConditions.text = condition
                    binding.txtSunrise.text = "${getTime(sunrise)}"
                    binding.txtSunset.text = "${getTime(sunset)}"
                    binding.txtSea.text = "$sea hPa"
                    binding.txtlocation.text = cityName
                    binding.txtDay.text = getDay(System.currentTimeMillis())
                    binding.txtDate.text = getDate()
                    changeBackground(condition)
                }else{
                    Toast.makeText(this@SearchActivity,"Error: Location does not exist", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<forecast>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })


    }
    private fun getDay(time:Long):String{
        return SimpleDateFormat("EEEE", Locale.getDefault()).format((Date()))
    }
    private fun getDate():String{
        return SimpleDateFormat("dd MMMM yyyy",Locale.getDefault()).format((Date()))
    }
    private fun getTime(time:Long): String{
        return SimpleDateFormat("HH:mm",Locale.getDefault()).format((Date(time*1000)))
    }

    private  fun changeBackground(weather:String){
        when(weather){
            "Clear Sky","Sunny","Clear"->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sunny_lottie)

            }

            "Clouds","Foggy","Mist","Overcast","Partly Clouds","Haze"->{
                binding.root.setBackgroundResource(R.drawable.cloudy_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloudy_lottie)

            }

            "Light Rain","Moderate Rain","Heavy Rain","Drizzle","Showers","Rain"->{
                binding.root.setBackgroundResource(R.drawable.rainny_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain_lottie)

            }

            "Light Snow","Moderate Snow","Heavy Snow","Blizzard","Snow"->{
                binding.root.setBackgroundResource(R.drawable.snowy_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow_lottie)

            }
            else->{
                binding.root.setBackgroundResource(R.drawable.cloudy_background)
                binding.lottieAnimationView.setAnimation(R.raw.animation_lmbypws1)

            }
        }
        binding.lottieAnimationView.playAnimation()


    }

    override fun onTouchEvent(touch:MotionEvent): Boolean {
        return when (touch.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = touch.x
                y1 = touch.y
                true
            }
            MotionEvent.ACTION_UP -> {
                x2 = touch.x
                y2 = touch.y
                if(x1<x2 && y1<y2){
                    {}
                }
                else if (x1 < x2) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.left2,R.anim.right2)
                }
                true
            }
            else -> false
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left2,R.anim.right2)
    }


}
