package exception

import org.restlet.data.Status
import org.restlet.resource.ResourceException

class InvalidPathAttributeException extends ResourceException {
  InvalidPathAttributeException(String description) {
    super(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, description)
  }
}
