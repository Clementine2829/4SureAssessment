package com.clementine.weatherapp.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor

class WeatherDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_WEATHER (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TOWN_NAME TEXT,
                $COLUMN_LONGITUDE REAL,
                $COLUMN_LATITUDE REAL,
                $COLUMN_WEATHER TEXT,
                $COLUMN_UNIT_TYPE TEXT,
                $COLUMN_HUMIDITY INTEGER,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_PRESSURE INTEGER
            )
        """
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WEATHER")
        onCreate(db)
    }

    fun insertWeather(townName: String, longitude: Double, latitude: Double, weather: String, unitType: String, humidity: Int, description: String, pressure: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TOWN_NAME, townName)
            put(COLUMN_LONGITUDE, longitude)
            put(COLUMN_LATITUDE, latitude)
            put(COLUMN_WEATHER, weather)
            put(COLUMN_UNIT_TYPE, unitType)
            put(COLUMN_HUMIDITY, humidity)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_PRESSURE, pressure)
        }
        db.insert(TABLE_WEATHER, null, values)
        db.close()
    }

    fun getAllWeather(): List<WeatherEntity> {
        val weatherList = mutableListOf<WeatherEntity>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_WEATHER", null)

        if (cursor.moveToFirst()) {
            do {
                val weather = WeatherEntity(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOWN_NAME)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEATHER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIT_TYPE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HUMIDITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRESSURE))
                )
                weatherList.add(weather)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return weatherList
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "WeatherDatabase"
        private const val TABLE_WEATHER = "weather_table"

        private const val COLUMN_ID = "id"
        private const val COLUMN_TOWN_NAME = "town_name"
        private const val COLUMN_LONGITUDE = "longitude"
        private const val COLUMN_LATITUDE = "latitude"
        private const val COLUMN_WEATHER = "weather"
        private const val COLUMN_UNIT_TYPE = "unit_type"
        private const val COLUMN_HUMIDITY = "humidity"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_PRESSURE = "pressure"
    }
}

