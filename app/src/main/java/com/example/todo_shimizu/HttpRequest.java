package com.example.todo_shimizu;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class HttpRequest {

    String COMP_URL = "https://www.whaletech.work/todo-shimizu/read_complate.php";
    String UNF_URL = "https://www.whaletech.work/todo-shimizu/read_unfinish.php";
    public List<DataList> getRequest(boolean frag) {
        // HttpURLConnectionの作成
        try {
            URL url;
            if (frag) {
                url = new URL(COMP_URL);
            } else {
                url = new URL(UNF_URL);
            }

            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.connect();

            // サーバーからのレスポンスを標準出力へ出す
            BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
            StringBuilder xml = new StringBuilder();
            String line = "";
            while((line = reader.readLine()) != null)
                xml.append(line);

            ObjectMapper jsonMapper = new ObjectMapper();
            List<DataList> dataLists = Arrays.asList(jsonMapper.readValue(xml.toString(), DataList[].class));
            for (DataList data : dataLists) {
                System.out.println(data.getId());
                System.out.println(data.getTitle());
                System.out.println(data.getExp());
                System.out.println(data.getStatus());
                System.out.println(data.getDay());
            }
            reader.close();

            return dataLists;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void postRequest(boolean frag) {
        try {
            URL url;
            if (frag) {
                url = new URL(COMP_URL);
            } else {
                url = new URL(UNF_URL);
            }

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //POSTに指定
            connection.setRequestMethod("POST");
            //アウトプット可能
            connection.setDoOutput(true);
            //入力可能
            connection.setDoInput(true);
            //cache無し
            connection.setUseCaches(false);
            // データタイプをjsonに指定する
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            //コネクション、通信開始
            connection.connect();
            // jsonデータを出力ストリームへ書き出す
            String body = "userName=hyman1993&password=123456";
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            writer.write(body);
            writer.close();

            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                // 通信に成功した
                // GETメソッドを同様に
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
