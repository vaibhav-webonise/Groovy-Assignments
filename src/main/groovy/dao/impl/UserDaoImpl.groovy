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
  final DSLContext dslContext
  final UserDataValidation userDataValidation
  final int ZERO_RECORDS = 0
  final JwtUtil jwtUtil;

  @Inject
  UserDaoImpl(DSLContext dslContext, JwtUtil jwtUtil1, UserDataValidation userDataValidation) {
    this.dslContext = dslContext
    this.jwtUtil = jwtUtil1
    this.userDataValidation = userDataValidation
  }

  @Override
  Representation signUpUser(Userdata userdata) {
    final int ZERO_RECORDS = 0
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
        throw new RecordCouldNotSavedException("User could not registered")
      }
    }
  }

  @Override
  AuthenticationResponse signInUser(Userdata userdata) {
    if (userDataValidation.isUserExists(userdata.getUsername())) {
      Record userRecord = dslContext.fetchOne(dslContext.selectFrom("UserData").where(USERDATA.USERNAME.eq(userdata.getUsername())))
      if (userDataValidation.isPasswordCorrect(userdata, userRecord)) {
        log.info("User logged in successfully with username {}", userdata.getUsername())
        return new AuthenticationResponse(userRecord.get("id") as int, userRecord.get("username") as String, jwtUtil.generateToken(userdata))
      } else {
        log.error("Password is invalid for username {}", userdata.getUsername())
        throw new InvalidPasswordException("Password is invalid")
      }
    } else {
      log.error("User not exists with username {}", userdata.getUsername())
      throw new UserNotExistsException("User not exists with given username")
    }
  }

  @Override
  Representation changeUserPassword(Userdata userdata, int id, String token) {
    if (userDataValidation.isUserExistsById(id)) {
      if (userDataValidation.isOldPasswordCorrect(userdata.getPassword(), id)) {
        if (!userDataValidation.isNewPassWordSame(userdata)) {
          Record userRecord = dslContext.fetchOne(dslContext.selectFrom("UserData").where(USERDATA.ID.eq(id)))
          if (jwtUtil.validateToken(token, userRecord.get("username") as String)) {
            int recordsUpdated = dslContext.update(USERDATA).
                set(USERDATA.PASSWORD, userDataValidation.getEncryptedPassword(userdata.getNewPassword())).
                set(USERDATA.NEW_PASSWORD, userDataValidation.getEncryptedPassword(userdata.getNewPassword())).where(USERDATA.ID.eq(id)).execute()
            if (recordsUpdated > ZERO_RECORDS) {
              log.info("Password changed successfully of user id {}", id)
              return new StringRepresentation("password updated successfully", MediaType.APPLICATION_JSON)
            } else {
              throw new RecordCouldNotSavedException("There is an issue while changing password..")
            }
          } else {
            throw new InvalidTokenException("Unauthorized request, Log-in and try again")
          }
        } else {
          throw new SamePasswordException("Try another password")
        }
      } else {
        throw new InvalidPasswordException("Password is invalid for given user id")
      }
    } else {
      log.error("User not exists with username {}", userdata.getUsername())
      throw new UserNotExistsException("User not exists with given id")
    }
  }

  @Override
  ResponseData getUserData(String token, int id) {
    if (userDataValidation.isUserExistsById(id)) {
      Record userRecord = dslContext.fetchOne(dslContext.selectFrom("UserData").where(USERDATA.ID.eq(id)))
      if (jwtUtil.validateToken(token, userRecord.get("username") as String)) {
        return new ResponseData(userRecord.get("id") as int, userRecord.get("username") as String)
      } else {
        throw new InvalidTokenException("Unauthorized request")
      }
    } else {
      log.error("User not exists with user id {}", id)
      throw new UserNotExistsException("User not exists with given id")
    }
  }
}
