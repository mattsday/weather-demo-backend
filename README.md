# Weather backend
This is a backend system that fronts openweathermap, caching results in Redis and providing a more simple format for a 5-day forecast.

You will need to set `weather.api.key` to your API key to use this. You can also change the default city if you like.

To use call the app with a path argument of your city, e.g. `https://weather-backend.cfapps.io/Swindon`
