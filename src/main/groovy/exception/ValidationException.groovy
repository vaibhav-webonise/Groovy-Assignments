package exception

import org.restlet.data.Status
import org.restlet.resource.ResourceException

class ValidationException extends ResourceException {
  ValidationException(String reasonPhrase) {
    super(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, reasonPhrase)
  }
}
