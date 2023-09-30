package com.example.TaskkerChat.controller;

import com.example.TaskkerChat.TaskkerService.TaskkerService;
import com.theokanning.openai.completion.CompletionChoice;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.http.HttpClient;

@Data
@RequiredArgsConstructor
@Controller
public class TaskkerController {

    @Autowired
    TaskkerService taskkerService;

    @GetMapping("/tarefas")
    public ResponseEntity<String> getTarefas(@RequestParam( name = "input") String input) throws Exception {

        String endpoint = "https://api.openai.com/v1/chat/completions";
        //String endpoint = "https://api.openai.com/v1/engines/davinci/completions";
        String apiKey = "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";


        String response = TaskkerService.sendQuery(input, endpoint, apiKey);

        return ResponseEntity.ok(response);
    }

}

