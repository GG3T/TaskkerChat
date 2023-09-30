package com.example.TaskkerChat.controller;

import com.example.TaskkerChat.TaskListResponse;
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

    String END_POINT = "https://api.openai.com/v1/chat/completions";
    //String endpoint = "https://api.openai.com/v1/engines/davinci/completions";
    String API_KEY = "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";
    @GetMapping("/tarefas")
    public ResponseEntity<String> getTarefas(@RequestParam( name = "input") String input) throws Exception {




        String response = TaskkerService.sendQuery(input, END_POINT, API_KEY);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/listaTarefas")
    public ResponseEntity<String> getTarefas() throws Exception {
        TaskListResponse qtdTarefas = taskkerService.taskkerChat();

        System.out.println(qtdTarefas);
        String prompt = "receba as tarefas e me diga a quantidade { \"tarefas\":" + qtdTarefas.getData().size() + " }";
        System.out.println(prompt);
        String response = TaskkerService.sendQuery(prompt, END_POINT, API_KEY);

        return ResponseEntity.ok(response);
    }
}

