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
    private var baseUrl = "http://localhost:8080"

    //var BASE_URL = "https://airwaves-web-gc7hm476va-uc.a.run.app/"
    private var gpsPostUrl = "$baseUrl/gps"
    private var songsPostUrl = "$baseUrl/fav-songs"
    private var songGetUrl = "$baseUrl/song"

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
                Thread.sleep(1000)
            }
            Thread.sleep(10000)
        }
    }

    private fun postGps(user: TestUser) {
        val arguments = mapOf(
                "latitude" to user.latitude.toString(),
                "longitude" to user.longitude.toString()
        )
        post(gpsPostUrl, arguments, user.id!!)
    }

    private fun postSongs(user: TestUser) {
        val arguments = mapOf(
                "fav_song_1" to user.fav_song_1,
                "fav_song_2" to user.fav_song_2,
                "fav_song_3" to user.fav_song_3
        )
        post(songsPostUrl, arguments, user.id!!)
    }

    private fun getSong(user: TestUser): String? {
        return get(songGetUrl, null, user.id!!)
    }

    private fun post(url: String, arguments: Map<String, String?>, id: String): String? {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder(URI.create(url))
                .header("Authorization", id)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(ofFormData(arguments))
                .build()
        return try {
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            response.body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        // Print out the response message
        //System.out.println(EntityUtils.toString(response.getEntity()));
    }

    fun get(url: String, arguments: Map<String, String?>?, id: String): String? {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder(URI.create(url))
                .header("Authorization", id)
                .GET()
                .build()
        return try {
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            response.body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        // Print out the response message
        //System.out.println(EntityUtils.toString(response.getEntity()));
    }

    private fun ofFormData(data: Map<String, String?>): BodyPublisher? {
        val builder = StringBuilder()
        for ((key, value) in data) {
            if (builder.isNotEmpty()) {
                builder.append("&")
            }
            builder.append(URLEncoder.encode(key, StandardCharsets.UTF_8))
            builder.append("=")
            builder.append(URLEncoder.encode(value.toString(), StandardCharsets.UTF_8))
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString())
    }
}