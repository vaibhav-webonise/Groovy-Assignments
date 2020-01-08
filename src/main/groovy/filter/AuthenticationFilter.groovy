package filter

import dao.UserDao
import exception.InvalidTokenException
import exception.UserNotExistsException
import org.restlet.Request
import org.restlet.Response
import org.restlet.routing.Filter
import util.JwtUtil
import validation.UserDataValidation
import javax.inject.Inject

class AuthenticationFilter extends Filter {
  private final UserDataValidation userDataValidation
  private final JwtUtil jwtUtil;
  private final UserDao userDao

  @Inject
  AuthenticationFilter(UserDataValidation userDataValidation, JwtUtil jwtUtil, UserDao userDao) {
    this.userDataValidation = userDataValidation
    this.jwtUtil = jwtUtil
    this.userDao = userDao
  }

  @Override
  protected int beforeHandle(Request request, Response response) {
    String token = request.getHeaders().getFirstValue("authorization")
    if (!userDataValidation.isTokenPresent(token)) {
      throw new InvalidTokenException("Request header must have valid authentication token associated with it")
    } else if (!userDao.isUserExists(jwtUtil.extractUsername(token))) {
      throw new UserNotExistsException("User not exists")
    } else if (!jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
      throw new InvalidTokenException("Unauthorized request, log in and try again")
    } else {
      CONTINUE
    }
  }
}
