package com.airwaves.airwavesweb.integration


import org.junit.Test
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublisher
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.util.*

class SimulationTest {
    var BASE_URL = "http://localhost:8080"
    var GPS_POST_URL = BASE_URL + "/gps"
    var SONGS_POST_URL = BASE_URL + "/fav-songs"
    var SONG_GET_URL = BASE_URL + "/song"

    @Test
    fun runTest() {
        val users: MutableList<TestUser> = ArrayList()
        val rand = Random()
        for (i in 0..99) {
            val user = TestUser()
            user.id = rand.nextLong().toString()
            user.latitude = rand.nextDouble()
            user.longitude = rand.nextDouble()
            user.fav_song_1 = "x"
            user.fav_song_2 = "y"
            user.fav_song_3 = "z"
            users.add(user)
        }
        while (true) {
            for (user in users) {
                println(user.id)
                user.latitude = rand.nextDouble()
                user.longitude = rand.nextDouble()
                postGps(user)
                postSongs(user)
                println(getSong(user))
                Thread.sleep(10)
            }
            Thread.sleep(10000)
        }
    }

    fun postGps(user: TestUser) {
        val arguments = mapOf(
                "latitude" to java.lang.Double.toString(user.latitude),
                "longitude" to java.lang.Double.toString(user.longitude)
        )
        post(GPS_POST_URL, arguments, user.id!!)
    }

    fun postSongs(user: TestUser) {
        val arguments = mapOf(
                "fav_song_1" to user.fav_song_1,
                "fav_song_2" to user.fav_song_2,
                "fav_song_3" to user.fav_song_3
        )
        post(SONGS_POST_URL, arguments, user.id!!)
    }

    fun getSong(user: TestUser): String {
        return get(SONG_GET_URL, null, user.id!!)
    }

    fun post(url: String, arguments: Map<String, String?>, id: String) {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder(URI.create(url))
                .header("Authorization", id)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(ofFormData(arguments))
                .build()
        try {
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // Print out the response message
        //System.out.println(EntityUtils.toString(response.getEntity()));
    }

    fun get(url: String, arguments: Map<String, String?>?, id: String): String {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder(URI.create(url))
                .header("Authorization", id)
                .GET()
                .build()
        try {
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }

        // Print out the response message
        //System.out.println(EntityUtils.toString(response.getEntity()));
    }

    fun ofFormData(data: Map<String, String?>): BodyPublisher? {
        val builder = StringBuilder()
        for ((key, value) in data) {
            if (builder.isNotEmpty()) {
                builder.append("&")
            }
            builder.append(URLEncoder.encode(key.toString(), StandardCharsets.UTF_8))
            builder.append("=")
            builder.append(URLEncoder.encode(value.toString(), StandardCharsets.UTF_8))
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString())
    }
}