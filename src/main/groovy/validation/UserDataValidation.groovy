package validation

import db.sql.tables.pojos.Userdata
import exception.InvalidPathAttributeException
import groovy.util.logging.Slf4j
import org.jooq.DSLContext
import org.jooq.Record
import org.mindrot.jbcrypt.BCrypt
import javax.inject.Inject
import static db.sql.tables.Userdata.USERDATA

@Slf4j
class UserDataValidation {
  private final DSLContext dslContext
  private final static int MIN_PASSWORD_LENGTH = 7
  private final static MIN_USERNAME_LENGTH = 7

  @Inject
  UserDataValidation(DSLContext dslContext) {
    this.dslContext = dslContext
  }

  static Boolean isPasswordLengthValid(String password) {
    return password.length() >= MIN_PASSWORD_LENGTH;
  }

  static Boolean isUsernameLengthValid(String username) {
    return username.length() >= MIN_USERNAME_LENGTH;
  }

  static Boolean isRequestBodyPresent(Userdata userdata) {
    return userdata != null
  }

  static Boolean isDataNullOrEmpty(String username, String password) {
    return username == null || username.isEmpty() || password == null || password.isEmpty()
  }

  static Boolean isInputFieldNotContainsSpace(String username, String password) {
    return !username.contains(" ") && !password.contains(" ")
  }

  boolean isUserExists(String username) {
    return dslContext.fetchExists(dslContext.selectFrom("UserData").where(USERDATA.USERNAME.eq(username)))
  }

  boolean isUserExistsById(int id) {
    return dslContext.fetchExists(dslContext.selectFrom("UserData").where(USERDATA.ID.eq(id)))
  }

  static boolean isPasswordCorrect(Userdata userdata, Record userRecord) {
    return BCrypt.checkpw(userdata.getPassword(), userRecord.get("password") as String)
  }

  static String getEncryptedPassword(String plainTextPassword) {
    return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt())
  }

  static boolean isNewPassWordSame(Userdata userdata) {
    return userdata.getPassword() == userdata.getNewPassword()
  }

  boolean isOldPasswordCorrect(String password, int id) {
    Record userRecord = dslContext.fetchOne(dslContext.selectFrom("UserData").where(USERDATA.ID.eq(id)))
    return BCrypt.checkpw(password, userRecord.get("password") as String)
  }

  static int getParsedIntFromString(String input) {
    try {
      return Integer.parseInt(input)
    }
    catch (NumberFormatException exception) {
      log.error("Exception occurred:{}", exception.getMessage())
      throw new InvalidPathAttributeException("Invalid path attribute in the request path, Must be a integer value");
    }
  }

  static Boolean isTokenValid(String token) {
    return token != null && !token.isEmpty()
  }
}
