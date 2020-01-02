package exception

import org.restlet.data.Status
import org.restlet.resource.ResourceException

class InvalidTokenException extends ResourceException {
  InvalidTokenException(String s) {
    super(Status.CLIENT_ERROR_UNAUTHORIZED, s)
  }
}
