package hello.vuetilserver.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostsErrorResponse {

    private int status;
    private String message;
    private LocalDateTime timeStamp;
}
