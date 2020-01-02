package exception

import org.restlet.data.Status
import org.restlet.resource.ResourceException

class InvalidPasswordException extends ResourceException {
  InvalidPasswordException(String description) {
    super(Status.CLIENT_ERROR_UNAUTHORIZED, description)
  }
}
