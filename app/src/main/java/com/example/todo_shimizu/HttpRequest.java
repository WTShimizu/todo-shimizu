package com.example.todo_shimizu;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class HttpRequest {

    String READ_COMP_URL = "https://www.whaletech.work/todo-shimizu/read_complate.php";
    String READ_UNF_URL = "https://www.whaletech.work/todo-shimizu/read_unfinish.php";
    String STORE_COMP_URL = "https://www.whaletech.work/todo-shimizu/store_complete.php";
    String STORE_UNF_URL = "https://www.whaletech.work/todo-shimizu/store_unfinish.php";
    String DEL_COMP_URL = "https://www.whaletech.work/todo-shimizu/destory_complate.php";
    String DEL_UNF_URL = "https://www.whaletech.work/todo-shimizu/destory_unfinish.php";
    String EDIT_COMP_URL = "https://www.whaletech.work/todo-shimizu/destory_complate.php";
    String EDIT_UNF_URL = "https://www.whaletech.work/todo-shimizu/destory_unfinish.php";
    public List<DataList> getRequest(boolean frag) {
        // HttpURLConnectionの作成
        try {
            URL url;
            if (frag) {
                url = new URL(READ_COMP_URL);
            } else {
                url = new URL(READ_UNF_URL);
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
            reader.close();

            return dataLists;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ID指定
    public DataList getIdRequest(boolean frag, int id) {
        // HttpURLConnectionの作成
        try {
            URL url;
            if (frag) {
                url = new URL(READ_COMP_URL + "?id=" + id);
            } else {
                url = new URL(READ_UNF_URL  + "?id=" + id);
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
            reader.close();

            return dataLists.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String postRequest(boolean frag, DataList dataList) {
        try {
            URL url;
            if (frag) {
                url = new URL(STORE_COMP_URL);
            } else {
                url = new URL(STORE_UNF_URL);
            }
            String sendDataJson = "{\"title\":" + "\"" + dataList.mTitle + "\"" +
                    ",\"exp\":" + "\"" + dataList.mExp + "\"" +",\"status\":" + "\"" + dataList.mStatus
                    + "\"" + ",\"day\":" + "\"" + dataList.mDay + "\"" + "}";

            Log.d("tag", sendDataJson);

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http = (HttpURLConnection) url.openConnection();
            http.setDoOutput(true);
            http.connect();

            OutputStream os = http.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            writer.write(sendDataJson);
            writer.flush();
            writer.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
            StringBuilder xml = new StringBuilder();
            String line = "";
            while((line = reader.readLine()) != null)
                xml.append(line);
            Log.d("tag", xml.toString());
            os.close();

            return http.getResponseMessage();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "NO";
    }

    // Delete
    public String deleteRequest(boolean frag, int id) {
        try {
            URL url;
            if (frag) {
                url = new URL(DEL_COMP_URL);
            } else {
                url = new URL(DEL_UNF_URL);
            }
            String sendDataJson = "{\"id\":" + "\"" + id + "\"" + "}";

            Log.d("tag", sendDataJson);

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http = (HttpURLConnection) url.openConnection();
            http.setDoOutput(true);
            http.connect();

            OutputStream os = http.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            writer.write(sendDataJson);
            writer.flush();
            writer.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
            StringBuilder xml = new StringBuilder();
            String line = "";
            while((line = reader.readLine()) != null)
                xml.append(line);
            Log.d("tag", xml.toString());
            os.close();

            return http.getResponseMessage();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "NO";
    }

    public String editRequest(boolean frag, DataList dataList) {
        try {
            URL url;
            if (frag) {
                url = new URL(EDIT_COMP_URL);
            } else {
                url = new URL(EDIT_UNF_URL);
            }
            String sendDataJson = "{\"id\":" + "\"" + dataList.mId + "\""
                    + ",\"title\":" + "\"" + dataList.mTitle + "\"" +
                    ",\"exp\":" + "\"" + dataList.mExp + "\"" +",\"status\":" + "\"" + dataList.mStatus
                    + "\"" + ",\"day\":" + "\"" + dataList.mDay + "\"" + "}";

            Log.d("tag", sendDataJson);

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http = (HttpURLConnection) url.openConnection();
            http.setDoOutput(true);
            http.connect();

            OutputStream os = http.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            writer.write(sendDataJson);
            writer.flush();
            writer.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
            StringBuilder xml = new StringBuilder();
            String line = "";
            while((line = reader.readLine()) != null)
                xml.append(line);
            Log.d("tag", xml.toString());
            os.close();

            return http.getResponseMessage();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "NO";
    }
}
