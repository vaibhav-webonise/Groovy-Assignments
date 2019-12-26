package dao

import db.sql.tables.pojos.Userdata
import model.ResponseData
import org.restlet.representation.Representation

interface UserDao {
  Representation signUpUser(Userdata userdata)
  ResponseData signInUser(Userdata userdata)
  Representation changeUserPassword(Userdata userdata)
}
