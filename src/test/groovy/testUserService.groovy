import db.sql.tables.pojos.Userdata
import dao.UserDao
import model.ResponseData
import org.restlet.representation.Representation
import org.restlet.representation.StringRepresentation
import service.UserService
import service.impl.UserServiceImpl
import spock.lang.Specification

class testUserService extends Specification {
  UserDao mockUserDao = Mock(UserDao)
  UserService userService = new UserServiceImpl(mockUserDao)

  def "sign in user"() {
    given:
    Userdata userdata = new Userdata();
    userdata.setUsername("vaibhav")
    userdata.setPassword("vaibhav")

    when:
    ResponseData responseData = userService.signInUserService(userdata);

    then:
    mockUserDao.signInUser(userdata) >> new ResponseData(1, "vaibhav")
    userdata.getUsername() == responseData.getUsername()
  }

  def "sign up user"() {
    given:
    Userdata userdata = new Userdata()
    userdata.setUsername("vaibhav")
    userdata.setPassword("vaibhav")

    when:
    Representation representation = userService.signUpUserService(userdata)

    then:
    mockUserDao.signUpUser(userdata) >> new StringRepresentation("User registered")
  }
}
