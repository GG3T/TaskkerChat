package com.example.TaskkerChat.TaskkerService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TaskkerService {

    public String consultaGPT() throws Exception {
        OpenAiService service = new OpenAiService("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");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("Quantos é 2+1 ?")
                .model("text-davinci-003")
                .echo(true)
                .build();
        List<CompletionChoice> completionChoice = service.createCompletion(completionRequest).getChoices();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(completionChoice);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getCause());
        }
    }

    public static String sendQuery(String input, String endpoint, String apiKey) {
        // Build input and API key params
        JSONObject payload = new JSONObject();
        JSONObject message = new JSONObject();
        JSONArray messageList = new JSONArray();

        message.put("role", "user");
        message.put("content", input);
        messageList.put(message);

        payload.put("model", "gpt-3.5-turbo");
        payload.put("messages", messageList);
        payload.put("temperature", 0.7);


        StringEntity inputEntity = new StringEntity(payload.toString(), ContentType.APPLICATION_JSON);

        HttpPost post = new HttpPost(endpoint);
        post.setEntity(inputEntity);
        post.setHeader("Authorization", "Bearer " + apiKey);
        post.setHeader("Content-Type", "application/json");

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {
            HttpEntity resEntity = response.getEntity();
            String resJsonString = new String(resEntity.getContent().readAllBytes(), StandardCharsets.UTF_8);
            JSONObject resJson = new JSONObject(resJsonString);

            if (resJson.has("error")) {
                String errorMsg = resJson.getString("error");
                log.error("Chatbot API error: {}", errorMsg);
                return "Error: " + errorMsg;
            }

            JSONArray responseArray = resJson.getJSONArray("choices");
            List<String> responseList = new ArrayList<>();

            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject responseObj = responseArray.getJSONObject(i);
                String responseString = responseObj.getJSONObject("message").getString("content");
                responseList.add(responseString);
            }

            // Convert response list to JSON and return it
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(responseList.get(0));
            return jsonResponse;


        } catch (IOException | JSONException e) {
            log.error("Error sending request: {}", e.getMessage());
            return "Error: " + e.getMessage();
        }
        }

}

