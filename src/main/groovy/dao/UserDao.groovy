package dao

import db.sql.tables.pojos.Userdata
import model.AuthenticationResponse
import model.ResponseData
import org.restlet.representation.Representation

interface UserDao {
  Representation signUpUser(Userdata userdata)

  AuthenticationResponse signInUser(Userdata userdata)

  Representation changeUserPassword(Userdata userdata, int id)

  ResponseData getUserData(int id)
}
