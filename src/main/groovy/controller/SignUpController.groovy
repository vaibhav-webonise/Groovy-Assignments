package controller

import db.sql.tables.pojos.Userdata
import groovy.transform.CompileStatic
import org.restlet.representation.Representation
import org.restlet.resource.Post
import org.restlet.resource.ServerResource
import service.UserService
import javax.inject.Inject

@CompileStatic
class SignUpController extends ServerResource {
  private final UserService userService;

  @Inject
  SignUpController(UserService userService) {
    this.userService = userService;
  }

  @Post
  Representation signUpUser(Userdata userdata) {
    return userService.signUpUserService(userdata);
  }
}
