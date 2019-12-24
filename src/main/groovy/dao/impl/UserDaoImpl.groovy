package dao.impl

import db.sql.tables.pojos.Userdata
import dao.UserDao
import exception.InvalidPasswordException
import exception.RecordCouldNotSavedException
import exception.UserAlreadyExistsException
import exception.UserNotExistsException
import groovy.util.logging.Slf4j
import model.ResponseData
import org.jooq.DSLContext
import org.jooq.Record
import org.restlet.data.MediaType
import org.restlet.representation.Representation
import org.restlet.representation.StringRepresentation
import javax.inject.Inject
import static db.sql.tables.Userdata.USERDATA

@Slf4j
class UserDaoImpl implements UserDao {
  final DSLContext dslContext;

  @Inject
  UserDaoImpl(DSLContext dslContext) {
    this.dslContext = dslContext
  }

  @Override
  Representation signUpUser(Userdata userdata) {
    final int ZERO_RECORDS = 0;
    if (checkIfUserExists(userdata.getUsername())) {
      log.error("User already exists with username {}", userdata.getUsername())
      throw new UserAlreadyExistsException("user exists with given username")
    } else {
      int recordsInserted = dslContext.newRecord(USERDATA, userdata).store()
      if (recordsInserted > ZERO_RECORDS) {
        log.info(" User registered successfully with username {}", userdata.getUsername());
        return new StringRepresentation("User registered successfully", MediaType.APPLICATION_JSON)
      } else {
        throw new RecordCouldNotSavedException("User could not registered")
      }
    }
  }

  @Override
  ResponseData signInUser(Userdata userdata) {
    if (checkIfUserExists(userdata.getUsername())) {
      Record userRecord = dslContext.fetchOne(dslContext.selectFrom("UserData").where(USERDATA.USERNAME.eq(userdata.getUsername())))
      if (checkIfPasswordIsCorrect(userdata, userRecord)) {
        log.info("User logged in successfully with username {}", userdata.getUsername())
        return new ResponseData(userRecord.get("id") as int, userRecord.get("username") as String)
      } else {
        log.error("Password is invalid for username {}", userdata.getUsername())
        throw new InvalidPasswordException("Password is invalid");
      }
    } else {
      log.error("User not exists with username {}", userdata.getUsername())
      throw new UserNotExistsException("User not exists with given username");
    }
  }

  private boolean checkIfUserExists(String username) {
    return dslContext.fetchExists(dslContext.selectFrom("UserData").where(USERDATA.USERNAME.eq(username)))
  }

  private boolean checkIfPasswordIsCorrect(Userdata userdata, Record userRecord) {
    return userRecord.get("password") == userdata.getPassword()
  }
}
