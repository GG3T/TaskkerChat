package com.example.TaskkerChat;

import lombok.Data;

import java.util.List;

@Data
public class TaskListResponse {

    private List<TaskData> data;

    @Data
    public static class TaskData {
        private long id;
        private String created_at;
        private String updated_at;
        private long team_id;
        private String task;
        private String duedate;
        private String status;
    }
}