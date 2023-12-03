package com.restapi4.dsa2024;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Result{
    private static final String RESTURL = "https://jsonmock.hackerrank.com/api/food_outlets";
    public static  List<String> getRelevantFoodOutlets(String city,String pagenumber) throws IOException {
        String response=getresponseperpage(city,pagenumber);
        JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
        int totalpages=jsonResponse.get("total_pages").getAsInt();
        JsonArray data=jsonResponse.getAsJsonArray("data");
        List<Double> ratinglist=new ArrayList<>();
        List<String> resturants=new ArrayList<>();
        for(int i=0;i<data.size();i++) {
           Double average= data.get(i).getAsJsonObject().get("user_rating").getAsJsonObject().get("average_rating").getAsDouble();
           ratinglist.add(average);
        }
        double max=ratinglist.stream().max(Double::compareTo).get();
        for(int i=0;i<data.size();i++){
            if(data.get(i).getAsJsonObject().get("user_rating").getAsJsonObject().get("average_rating").getAsDouble()==max){
               resturants.add(String.valueOf(data.get(i).getAsJsonObject().get("name")));
            }
        }
        return resturants;
    }
    private static String getresponseperpage(String city, String pagenumber) throws IOException {
        String newurl= RESTURL+"?city="+city+"&page="+pagenumber;
        System.out.println(newurl);
        URL url=new URL(newurl);
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type","application/json");

        BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response;
        StringBuilder sb=new StringBuilder();
        while((response=br.readLine())!=null){
            sb.append(response);
        }
        return sb.toString();
    }
}
public class GetFoodOutlets {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\SAYAN\\Desktop\\test.txt"));

        String city = bufferedReader.readLine();

        String pagenumber = bufferedReader.readLine();

        List<String> result = Result.getRelevantFoodOutlets(city, pagenumber);

        bufferedWriter.write(result.toString());
        bufferedWriter.write("\n");

        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
