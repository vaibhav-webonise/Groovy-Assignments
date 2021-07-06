package resources

import db.sql.tables.pojos.Userdata
import exception.ValidationException
import groovy.transform.CompileStatic
import org.restlet.representation.Representation
import org.restlet.resource.Post
import org.restlet.resource.ServerResource
import service.UserService
import validation.UserDataValidation
import javax.inject.Inject

@CompileStatic
class SignUpResource extends ServerResource {
  private final UserService userService;
  private final UserDataValidation userDataValidation;

  @Inject
  SignUpResource(UserService userService, UserDataValidation userDataValidation) {
    this.userService = userService
    this.userDataValidation = userDataValidation
  }

  @Post
  Representation signUpUser(Userdata userdata) {
    if (!userDataValidation.isRequestBodyPresent(userdata)) {
      throw new ValidationException("Request body can not be empty");
    } else if (userDataValidation.isDataNullOrEmpty(userdata.getUsername(), userdata.getPassword())) {
      throw new ValidationException("Username and password fields must be there and can not be empty")
    } else if (!userDataValidation.isInputFieldNotContainsSpace(userdata.getUsername(), userdata.getPassword())) {
      throw new ValidationException("Invalid input, Unnecessary spaces in username or password")
    } else if (!userDataValidation.isPasswordLengthValid(userdata.getPassword()) || !userDataValidation.isUsernameLengthValid(userdata.getUsername())) {
      throw new ValidationException("username and password must have at least 7 characters")
    } else {
      return userService.signUpUserService(userdata)
    }
  }
}
