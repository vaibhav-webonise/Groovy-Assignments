package exception

import org.restlet.data.Status
import org.restlet.resource.ResourceException

class UserNotExistsException extends ResourceException {
  UserNotExistsException(String message) {
    super(Status.CLIENT_ERROR_NOT_FOUND, message)
  }
}
