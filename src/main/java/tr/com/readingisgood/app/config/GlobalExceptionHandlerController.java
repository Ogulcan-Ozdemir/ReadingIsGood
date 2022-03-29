package tr.com.readingisgood.app.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import tr.com.readingisgood.app.component.ResponseHelper;
import tr.com.readingisgood.app.model.BaseRespDTO;
import tr.com.readingisgood.app.model.exception.ApplicationException;
import tr.com.readingisgood.app.model.exception.WebRequestException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(WebRequestException.class)
    public ResponseEntity<BaseRespDTO<?>> handleCustomException(WebRequestException exception) {
        return ResponseHelper.getExceptionResponseEntity(HttpStatus.FORBIDDEN, exception);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseRespDTO<?>> handleAccessDeniedException(AccessDeniedException exception) {
        return ResponseHelper.getExceptionResponseEntity(HttpStatus.FORBIDDEN, exception);
    }

    @ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity<BaseRespDTO<?>> handleGenericNotFoundException(ApplicationException exception) {
        return ResponseHelper.getExceptionResponseEntity(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseRespDTO<?>> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseHelper.getExceptionResponseEntity(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseRespDTO<?>> handleException(Exception exception) {
        return ResponseHelper.getExceptionResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    @ExceptionHandler(value = {NoHandlerFoundException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public void badRequest(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/");
    }


}
