package exception

import org.restlet.data.Status
import org.restlet.resource.ResourceException

class SamePasswordException extends ResourceException {
  SamePasswordException(String message) {
    super(Status.CLIENT_ERROR_CONFLICT, message)
  }
}
