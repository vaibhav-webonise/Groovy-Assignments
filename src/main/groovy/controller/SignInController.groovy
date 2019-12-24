package controller

import db.sql.tables.pojos.Userdata
import model.ResponseData
import org.restlet.resource.Post
import org.restlet.resource.ServerResource
import service.UserService
import javax.inject.Inject

class SignInController extends ServerResource {
  private final UserService userService;

  @Inject
  SignInController(UserService userService) {
    this.userService = userService;
  }

  @Post
  ResponseData signInUser(Userdata userdata) {
    return userService.signInUserService(userdata)
  }
}
