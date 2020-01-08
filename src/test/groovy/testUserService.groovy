import dao.impl.UserDaoImpl
import db.sql.tables.pojos.Userdata
import dao.UserDao
import exception.UserNotExistsException
import model.AuthenticationResponse
import org.jooq.DSLContext
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
    userService.signInUserService(userdata);

    then:
    AuthenticationResponse response = mockUserDao.signInUser(userdata)
    assert response.username == userdata.getUsername();
  }

  def "sign up user"() {
    given:
    Userdata userdata = new Userdata()
    userdata.setUsername("vaibhav")
    userdata.setPassword("vaibhav")

    when: "signUpUserService method get called it throws exception"
    userService.signUpUserService(userdata)

    then: "signUpUser method should get called by service layer"
    thrown UserNotExistsException
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
