package router

import com.google.inject.Inject
import filter.AuthenticationFilter
import resources.UserResource
import resources.SignUpResource
import org.restlet.Context
import resources.SignInResource
import restling.restlet.RestlingRouter

class AppRouter extends RestlingRouter {
  @Inject
  AppRouter(Context context) {
    super(context)
  }

  @Override
  void init() throws Exception {
    attach("/user/sign-up", SignUpResource)
    attach("/user/sign-in", SignInResource)
    attach("/user/{id}", UserResource, AuthenticationFilter)
  }
}
