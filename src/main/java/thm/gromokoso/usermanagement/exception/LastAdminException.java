package thm.gromokoso.usermanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Cannot delete last Admin")
public class LastAdminException extends RuntimeException {
    public LastAdminException(String message) {
        super(message);
    }
}
