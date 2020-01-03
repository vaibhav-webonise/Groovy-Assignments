package controller

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
class SignUpController extends ServerResource {
  private final UserService userService;
  private final UserDataValidation userDataValidation;

  @Inject
  SignUpController(UserService userService, UserDataValidation userDataValidation) {
    this.userService = userService
    this.userDataValidation = userDataValidation
  }

  @Post
  Representation signUpUser(Userdata userdata) {
    if (userDataValidation.isRequestBodyPresent(userdata)) {
      if (!userDataValidation.isDataNullOrEmpty(userdata.getUsername(), userdata.getPassword())) {
        if (userDataValidation.isInputFieldNotContainsSpace(userdata.getUsername(), userdata.getPassword())) {
          if (userDataValidation.isPasswordLengthValid(userdata.getPassword()) && userDataValidation.isUsernameLengthValid(userdata.getUsername())) {
            return userService.signUpUserService(userdata)
          } else {
            throw new ValidationException("username and password must have at least 7 characters")
          }
        } else {
          throw new ValidationException("Invalid input, Unnecessary spaces in username or password")
        }
      } else {
        throw new ValidationException("Username and password fields must be there and can not be empty")
      }
    } else {
      throw new ValidationException("Request body can not be empty");
    }
  }
}
