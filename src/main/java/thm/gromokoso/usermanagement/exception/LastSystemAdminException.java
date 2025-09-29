package thm.gromokoso.usermanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Cannot delete last Admin")
public class LastSystemAdminException extends RuntimeException {
    public LastSystemAdminException(String message) {
        super(message);
    }
}
