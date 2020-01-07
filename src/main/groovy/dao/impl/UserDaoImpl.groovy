package dao.impl

import db.sql.tables.pojos.Userdata
import dao.UserDao
import exception.InvalidPasswordException
import exception.RecordCouldNotSavedException
import exception.SamePasswordException
import exception.UserAlreadyExistsException
import exception.UserNotExistsException
import exception.InvalidTokenException
import groovy.util.logging.Slf4j
import model.AuthenticationResponse
import model.ResponseData
import org.jooq.DSLContext
import org.jooq.Record
import org.restlet.data.MediaType
import org.restlet.representation.Representation
import org.restlet.representation.StringRepresentation
import util.JwtUtil
import validation.UserDataValidation
import javax.inject.Inject
import static db.sql.tables.Userdata.USERDATA

@Slf4j
class UserDaoImpl implements UserDao {
  private final DSLContext dslContext
  private final UserDataValidation userDataValidation
  private final int ZERO_RECORDS = 0
  private final JwtUtil jwtUtil;

  @Inject
  UserDaoImpl(DSLContext dslContext, JwtUtil jwtUtil1, UserDataValidation userDataValidation) {
    this.dslContext = dslContext
    this.jwtUtil = jwtUtil1
    this.userDataValidation = userDataValidation
  }

  @Override
  Representation signUpUser(Userdata userdata) {
    if (userDataValidation.isUserExists(userdata.getUsername())) {
      log.error("User already exists with username {}", userdata.getUsername())
      throw new UserAlreadyExistsException("user exists with given username")
    } else {
      Userdata newUser = new Userdata();
      newUser.setUsername(userdata.getUsername())
      newUser.setPassword(userDataValidation.getEncryptedPassword(userdata.getPassword()))
      newUser.setNewPassword(userDataValidation.getEncryptedPassword(userdata.getPassword()))
      int recordsInserted = dslContext.newRecord(USERDATA, newUser).store()
      if (recordsInserted > ZERO_RECORDS) {
        log.info(" User registered successfully with username {}", userdata.getUsername());
        return new StringRepresentation("User registered successfully", MediaType.APPLICATION_JSON)
      } else {
        throw new RecordCouldNotSavedException("User could not registered with username:" + userdata.getUsername())
      }
    }
  }


  @Override
  AuthenticationResponse signInUser(Userdata userdata) {
    Record userRecord = dslContext.fetchOne(dslContext.selectFrom("UserData").where(USERDATA.USERNAME.eq(userdata.getUsername())))
    if (!userDataValidation.isUserExists(userdata.getUsername())) {
      log.error("User not exists with username {}", userdata.getUsername())
      throw new UserNotExistsException("User not exists with given username: " + userdata.getUsername())
    } else if (!userDataValidation.isPasswordCorrect(userdata, userRecord)) {
      log.error("Password is invalid for username {}", userdata.getUsername())
      throw new InvalidPasswordException("Password is invalid for username: " + userdata.getUsername())
    } else {
      log.info("User logged in successfully with username {}", userdata.getUsername())
      return new AuthenticationResponse(userRecord.get("id") as int, userRecord.get("username") as String, jwtUtil.generateToken(userdata))
    }
  }

  @Override
  Representation changeUserPassword(Userdata userdata, int userId) {
    Record userRecord = dslContext.fetchOne(dslContext.selectFrom("UserData").where(USERDATA.ID.eq(userId)))
    if (!userDataValidation.isUserExistsById(userId)) {
      log.error("User not exists with the user userId: {}", userId)
      throw new UserNotExistsException("User not exists with given id:" + userId)
    } else if (!userDataValidation.isOldPasswordCorrect(userdata.getPassword(), userId)) {
      throw new InvalidPasswordException("Password is invalid for given user id" + userId)
    } else if (userDataValidation.isNewPassWordSame(userdata)) {
      throw new SamePasswordException("Try another password ")
    } else {
      int recordsUpdated = dslContext.update(USERDATA).
          set(USERDATA.PASSWORD, userDataValidation.getEncryptedPassword(userdata.getNewPassword())).
          set(USERDATA.NEW_PASSWORD, userDataValidation.getEncryptedPassword(userdata.getNewPassword())).where(USERDATA.ID.eq(userId)).execute()
      if (recordsUpdated > ZERO_RECORDS) {
        log.info("Password changed successfully of user userId {}", userId)
        return new StringRepresentation("password updated successfully", MediaType.APPLICATION_JSON)
      } else {
        throw new RecordCouldNotSavedException("There is an issue while changing password of the user with userId: " + userId)
      }
    }
  }

  @Override
  ResponseData getUserData(int userId) {
    if (userDataValidation.isUserExistsById(userId)) {
      Record userRecord = dslContext.fetchOne(dslContext.selectFrom("UserData").where(USERDATA.ID.eq(userId)))
      return new ResponseData(userRecord.get("id") as int, userRecord.get("username") as String)
    } else {
      log.error("User not exists with user id {}", userId)
      throw new UserNotExistsException("User not exists with given id:" + userId)
    }
  }
}
