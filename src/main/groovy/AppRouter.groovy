import com.google.inject.Inject
import controller.SignInController
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
    attach("/sign-up", SignUpController)
    attach("/sign-in", SignInController)
  }
}
