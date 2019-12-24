package exception

import org.restlet.data.Status
import org.restlet.resource.ResourceException

class UserAlreadyExistsException extends ResourceException {
  UserAlreadyExistsException(String message) {
    super(Status.CLIENT_ERROR_CONFLICT, message);
  }
}
