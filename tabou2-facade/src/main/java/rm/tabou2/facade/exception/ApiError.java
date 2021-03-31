package rm.tabou2.facade.exception;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.List;

@ApiModel(description = "API Error")
@Validated
@Data
public class ApiError {

    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ApiError(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }

}
