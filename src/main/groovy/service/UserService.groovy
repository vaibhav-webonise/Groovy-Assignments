package service

import db.sql.tables.pojos.Userdata
import model.ResponseData
import org.restlet.representation.Representation

interface UserService {
  Representation signUpUserService(Userdata userdata);
  ResponseData signInUserService(Userdata userdata);
}
