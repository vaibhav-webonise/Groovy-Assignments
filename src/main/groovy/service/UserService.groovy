package service

import db.sql.tables.pojos.Userdata
import model.AuthenticationResponse
import model.ResponseData
import org.restlet.representation.Representation

interface UserService {

  /**
   * creates the new user and store user information in database
   * @param userdata
   * @return representation object with success message
   */
  Representation signUpUserService(Userdata userdata)

  /**
   * authenticates the user and allow to access resources
   * @param userdata
   * @return AuthenticationResponse object with jwt token
   */
  AuthenticationResponse signInUserService(Userdata userdata)

  /**
   * changes the password of requested user
   * @param userdata
   * @param userId
   * @Param token
   * @return representation object with success message
   */
  Representation changeUserPasswordService(Userdata userdata, int userId, String token)

  /**
   * retrieves the user information
   * @param token
   * @param userId
   * @return ResponseData object
   */
  ResponseData getUserData(String token, int userId)
}
