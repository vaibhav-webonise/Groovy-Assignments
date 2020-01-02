import db.sql.tables.pojos.Userdata
import dao.UserDao
import exception.UserAlreadyExistsException
import model.AuthenticationResponse
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
    AuthenticationResponse responseData = userService.signInUserService(userdata);

    then:
    mockUserDao.signInUser(userdata) >> new AuthenticationResponse(1, "vaibhav", "#gfwe627263726323123")
    userdata.getUsername() == responseData.getUsername()
  }

  def "sign up user"() {
    given:
    Userdata userdata = new Userdata()
    userdata.setUsername("vaibhav")
    userdata.setPassword("vaibhav")

    when: "signUpUserService method get called it throws exception"
    userService.signUpUserService(userdata) >> { throw new UserAlreadyExistsException() }

    then: "signUpUser method should get called by service layer"
    mockUserDao.signUpUser(userdata) >> { throw new UserAlreadyExistsException() }
  }

  def "sign up for new user"() {
    given:
    Userdata userdata = new Userdata();
    userdata.setUsername("vaibhav")
    userdata.setPassword("vaibhav")

    when:
    userService.signUpUserService(userdata);

    then:
    mockUserDao.signInUser(userdata) >> new StringRepresentation("user registered")
  }
}
