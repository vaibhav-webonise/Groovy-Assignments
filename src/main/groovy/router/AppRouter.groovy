package router

import com.google.inject.Inject
import controller.UserController
import controller.SignUpController
import org.restlet.Context
import restling.restlet.RestlingRouter

class AppRouter extends RestlingRouter {
  @Inject
  AppRouter(Context context) {
    super(context)
  }

  @Override
  void init() throws Exception {
    attach("/user/sign-up", SignUpController)
    attach("/user/sign-in", UserController)
    attach("/user/{id}", UserController)
  }
}
