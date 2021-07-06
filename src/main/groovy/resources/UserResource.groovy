package resources

import db.sql.tables.pojos.Userdata
import exception.ValidationException
import model.ResponseData
import org.restlet.representation.Representation
import org.restlet.resource.Get
import org.restlet.resource.Put
import org.restlet.resource.ServerResource
import service.UserService
import validation.UserDataValidation
import javax.inject.Inject

class UserResource extends ServerResource {
  private final UserService userService
  private final UserDataValidation userDataValidation

  @Inject
  UserResource(UserService userService, UserDataValidation userDataValidation) {
    this.userService = userService
    this.userDataValidation = userDataValidation
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
    } else {
      return userService.changeUserPasswordService(userdata, userId)
    }
  }

  @Get
  ResponseData getUserData() {
    int userId = UserDataValidation.getParsedIntFromString(getAttribute("id") as String)
    return userService.getUserData(userId)
  }
}
