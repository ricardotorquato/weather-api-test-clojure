# Weather API Test (Clojure)

## About this project
This project is being built to be an weather forecast api based in two data files: `city_list.json` and `weather_list.json`.

The idea is provide:

- List of cities
- List of cities with avaiable weather information
- Information of a single city with its weather information
- Filtered information by date periods about a single city with its wather information

## Environment configuration
I am using `http://boot-clj.com/` to run this project

## Running
In the terminal you can run by doing `boot repl` and inside the repl

```
(require 'main)
```

```
(main/start)
```

The server will run on `http://localhost:8890`

## API

 - `http://localhost:8890/cities` - List of cities
 - `http://localhost:8890/cities?that-has-weather=1` - List of cities that has weather
 - `http://localhost:8890/cities/{some_id}` - One city
 - `http://localhost:8890/cities/{some_id}?with-weather=1` - One city with its weather information
 - `http://localhost:8890/cities/{some_id}?with-weather=1&date-start={yyyy-mm-dd}&date-end={yyyy-mm-dd}` - One city with its weather information filtered by date
 
