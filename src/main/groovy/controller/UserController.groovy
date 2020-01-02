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
    if (userDataValidation.isRequestBodyPresent(userdata)) {
      if (!userDataValidation.isDataNullOrEmpty(userdata.getUsername(), userdata.getPassword())) {
        if (userDataValidation.isInputFieldNotContainsSpace(userdata.getUsername(), userdata.getPassword())) {
          return userService.signInUserService(userdata)
        } else {
          throw new ValidationException("Invalid input, Unnecessary spaces in username or password")
        }
      } else {
        throw new ValidationException("username and password can not be empty")
      }
    } else {
      throw new ValidationException("Request body can not be empty");
    }
  }

  @Put
  Representation changeUserPassword(Userdata userdata) {
    int id = UserDataValidation.getParsedIntFromString(getAttribute("id") as String)
    if (userDataValidation.isRequestBodyPresent(userdata)) {
      if (!userDataValidation.isDataNullOrEmpty(userdata.getNewPassword(), userdata.getPassword())) {
        if (userDataValidation.isInputFieldNotContainsSpace(userdata.getNewPassword(), userdata.getPassword())) {
          if (userDataValidation.isPasswordLengthValid(userdata.getNewPassword())) {
            if (userDataValidation.isTokenValid(getRequest().getHeaders().getFirstValue("authorization"))) {
              return userService.changeUserPasswordService(userdata, id, getRequest().getHeaders().getFirstValue("authorization"))
            } else {
              throw new InvalidTokenException("Request header must have valid authentication token associated with it")
            }
          } else {
            throw new ValidationException("Password must have at least 7 characters")
          }
        } else {
          throw new ValidationException("Invalid input, Unnecessary spaces in password")
        }
      } else {
        throw new ValidationException("Password field can not be empty")
      }
    } else {
      throw new ValidationException("Request body can not be empty");
    }
  }

  @Get
  ResponseData getUserData() {
    int id = UserDataValidation.getParsedIntFromString(getAttribute("id") as String)
    if (userDataValidation.isTokenValid(getRequest().getHeaders().getFirstValue("authorization"))) {
      return userService.getUserData(getRequest().getHeaders().getFirstValue("authorization"), id)
    } else {
      throw new InvalidTokenException("Request header must have valid authentication token associated with it")
    }
  }
}
