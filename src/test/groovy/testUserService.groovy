import db.sql.tables.pojos.Userdata
import dao.UserDao
import model.ResponseData
import service.UserService
import service.impl.UserServiceImpl
import spock.lang.Specification

class testUserService extends Specification {
  UserDao mockUserDao = Mock(UserDao)
  UserService userService = new UserServiceImpl(mockUserDao)

  def "sign in user"() {
    given:
    Userdata userdata = new Userdata();
    userdata.setUsername("vaibhav");
    userdata.setPassword("vaibhav")

    when:
    ResponseData responseData = userService.signInUserService(userdata);

    then:
    mockUserDao.signInUser(userdata) >> new ResponseData(1, "vaibhav")
    userdata.getUsername() == responseData.getUsername();
  }
}
