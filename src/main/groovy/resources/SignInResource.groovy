package resources

import db.sql.tables.pojos.Userdata
import exception.ValidationException
import model.AuthenticationResponse
import org.restlet.resource.Post
import org.restlet.resource.ServerResource
import service.UserService
import validation.UserDataValidation

import javax.inject.Inject

class SignInResource extends ServerResource {
  private final UserService userService;
  private final UserDataValidation userDataValidation;

  @Inject
  SignInResource(UserService userService, UserDataValidation userDataValidation) {
    this.userService = userService
    this.userDataValidation = userDataValidation
  }

  @Post
  AuthenticationResponse signInUser(Userdata userdata) {
    if (!userDataValidation.isRequestBodyPresent(userdata)) {
      throw new ValidationException("Request body can not be empty");
    } else if (userDataValidation.isDataNullOrEmpty(userdata.getUsername(), userdata.getPassword())) {
      throw new ValidationException("Username and password fields must be there and can not be empty")
    } else if (!userDataValidation.isInputFieldNotContainsSpace(userdata.getUsername(), userdata.getPassword())) {
      throw new ValidationException("Invalid input, Unnecessary spaces in username or password")
    } else {
      return userService.signInUserService(userdata)
    }
  }
}
