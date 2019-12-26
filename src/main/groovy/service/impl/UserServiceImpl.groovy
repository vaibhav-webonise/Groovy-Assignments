package service.impl

import db.sql.tables.pojos.Userdata
import dao.UserDao
import model.ResponseData
import org.restlet.representation.Representation
import service.UserService
import javax.inject.Inject

class UserServiceImpl implements UserService {
  private final UserDao userDao;

  @Inject
  UserServiceImpl(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  Representation signUpUserService(Userdata userdata) {
    return userDao.signUpUser(userdata)
  }

  @Override
  ResponseData signInUserService(Userdata userdata) {
    return userDao.signInUser(userdata)
  }

  @Override
  Representation changeUserPasswordService(Userdata userdata) {
    return userDao.changeUserPassword(userdata);
  }
}
