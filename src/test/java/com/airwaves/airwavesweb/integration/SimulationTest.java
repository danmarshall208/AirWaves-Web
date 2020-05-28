package com.airwaves.airwavesweb.integration;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationTest {
    String BASE_URL = "http://localhost:8080";
    String GPS_POST_URL = this.BASE_URL + "/gps";
    String SONGS_POST_URL = this.BASE_URL + "/fav-songs";
    String SONG_GET_URL = this.BASE_URL + "/song";

    @Test
    public void runTest() throws InterruptedException {
        List<TestUser> users = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < 100; i++) {
            TestUser user = new TestUser();
            user.id = Long.toString(rand.nextLong());
            user.latitude = rand.nextDouble();
            user.longitude = rand.nextDouble();
            user.fav_song_1 = "x";
            user.fav_song_2 = "y";
            user.fav_song_3 = "z";
            users.add(user);
        }

        while (true) {
            for (TestUser user : users) {
                user.latitude = rand.nextDouble();
                user.longitude = rand.nextDouble();
                this.postGps(user);
                this.postSongs(user);
                Thread.sleep(10);
            }
            Thread.sleep(10000);
        }
    }

    public void postGps(TestUser user) {
        List<NameValuePair> arguments = new ArrayList<>();
        arguments.add(new BasicNameValuePair("latitude", Double.toString(user.latitude)));
        arguments.add(new BasicNameValuePair("longitude", Double.toString(user.longitude)));
        this.post(this.GPS_POST_URL, arguments, user.id);
    }

    public void postSongs(TestUser user) {
        List<NameValuePair> arguments = new ArrayList<>();
        arguments.add(new BasicNameValuePair("fav_song_1", user.fav_song_1));
        arguments.add(new BasicNameValuePair("fav_song_2", user.fav_song_2));
        arguments.add(new BasicNameValuePair("fav_song_3", user.fav_song_3));
        this.post(this.SONGS_POST_URL, arguments, user.id);
    }

    public void post(String url, List<NameValuePair> arguments, String id) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);

        try {
            post.addHeader(HttpHeaders.AUTHORIZATION, id);
            post.setEntity(new UrlEncodedFormEntity(arguments));
            HttpResponse response = client.execute(post);

            // Print out the response message
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
