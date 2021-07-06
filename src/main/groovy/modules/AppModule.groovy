package modules

import dao.UserDao
import dao.impl.UserDaoImpl
import restling.guice.modules.RestlingApplicationModule
import router.AppRouter
import service.UserService
import service.impl.UserServiceImpl

class AppModule extends RestlingApplicationModule {
  Class<AppRouter> routerClass = AppRouter

  @Override
  void configureCustomBindings() {
    install(new DataAccessModule())
    bind(UserDao).to(UserDaoImpl)
    bind(UserService).to(UserServiceImpl)
  }
}
