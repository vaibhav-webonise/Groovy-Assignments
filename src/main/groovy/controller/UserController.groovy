package controller

import db.sql.tables.pojos.Userdata
import exception.ValidationException
import exception.InvalidTokenException
import model.AuthenticationResponse
import model.ResponseData
import org.restlet.representation.Representation
import org.restlet.resource.Get
import org.restlet.resource.Post
import org.restlet.resource.Put
import org.restlet.resource.ServerResource
import service.UserService
import validation.UserDataValidation
import javax.inject.Inject

class UserController extends ServerResource {
  private final UserService userService
  private final UserDataValidation userDataValidation

  @Inject
  UserController(UserService userService, UserDataValidation userDataValidation) {
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


  @Put
  Representation changeUserPassword(Userdata userdata) {
    int userId = UserDataValidation.getParsedIntFromString(getAttribute("id") as String)
    if (!userDataValidation.isRequestBodyPresent(userdata)) {
      throw new ValidationException("Request body can not be empty");
    } else if (userDataValidation.isDataNullOrEmpty(userdata.getNewPassword(), userdata.getPassword())) {
      throw new ValidationException("Password field can not be empty")
    } else if (!userDataValidation.isInputFieldNotContainsSpace(userdata.getNewPassword(), userdata.getPassword())) {
      throw new ValidationException("Invalid input, Unnecessary spaces in password")
    } else if (!userDataValidation.isPasswordLengthValid(userdata.getNewPassword())) {
      throw new ValidationException("Password must have at least 7 characters")
    } else if (!userDataValidation.isTokenValid(getRequest().getHeaders().getFirstValue("authorization"))) {
      throw new InvalidTokenException("Request header must have valid authentication token associated with it")
    } else {
      return userService.changeUserPasswordService(userdata, userId, getRequest().getHeaders().getFirstValue("authorization"))
    }
  }

  @Get
  ResponseData getUserData() {
    int userId = UserDataValidation.getParsedIntFromString(getAttribute("id") as String)
    if (userDataValidation.isTokenValid(getRequest().getHeaders().getFirstValue("authorization"))) {
      return userService.getUserData(getRequest().getHeaders().getFirstValue("authorization"), userId)
    } else {
      throw new InvalidTokenException("Request header must have valid authentication token associated with it")
    }
  }
}
