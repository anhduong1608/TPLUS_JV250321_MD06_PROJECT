package edu.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiDataResponse<T> {
    private Boolean success;
    private String message;
    private T data;
    private List<String> errors;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public static <T> ApiDataResponse<T> error(String message) {
        return new ApiDataResponse<>(
                false,
                message,
                null,
                List.of(message),
                LocalDateTime.now().withNano(0)
        );
    }

    public static <T> ApiDataResponse<T> error(List<String> errors, String message) {
        return new ApiDataResponse<>(
                false,
                message,
                null,
                errors,
                LocalDateTime.now().withNano(0)
        );
    }

    public static <T> ApiDataResponse<T> success(T data, String message) {
        return new ApiDataResponse<>(
                true,
                message,
                data,
                null,
                LocalDateTime.now().withNano(0)
        );
    }
}
