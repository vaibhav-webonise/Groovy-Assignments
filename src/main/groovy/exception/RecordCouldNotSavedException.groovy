package exception

import org.restlet.data.Status
import org.restlet.resource.ResourceException

class RecordCouldNotSavedException extends ResourceException {
  RecordCouldNotSavedException(String description) {
    super(Status.SERVER_ERROR_INTERNAL, description)
  }
}
