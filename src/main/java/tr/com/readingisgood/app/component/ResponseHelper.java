package tr.com.readingisgood.app.component;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tr.com.readingisgood.app.model.BaseRespDTO;
import tr.com.readingisgood.app.model.ErrorRespDTO;

import java.util.List;
import java.util.Map;

public class ResponseHelper {

    private static final String DEFAULT_PAYLOAD = "SUCCESS";

    public static ResponseEntity<BaseRespDTO<?>> getResponseEntity(HttpStatus status, Class<Void> body){
        BaseRespDTO<?> response = new BaseRespDTO<>(DEFAULT_PAYLOAD);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<BaseRespDTO<?>> getResponseEntity(HttpStatus status, Object body){
        BaseRespDTO<?> response = new BaseRespDTO<>(body);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<BaseRespDTO<?>> getResponseEntity(HttpStatus status, Map<?, ?> body){
        BaseRespDTO<?> response = new BaseRespDTO<>(body);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<BaseRespDTO<?>> getResponseEntity(HttpStatus status, List<?> body){
        BaseRespDTO<?> response = new BaseRespDTO<>(body);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<BaseRespDTO<?>> getExceptionResponseEntity(HttpStatus status, Exception exception){
        ErrorRespDTO error = new ErrorRespDTO(exception.getMessage());
        BaseRespDTO<?> response = new BaseRespDTO<>(error);
        return new ResponseEntity<>(response, status);
    }

}
