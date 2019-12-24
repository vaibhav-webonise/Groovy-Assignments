package modules

import com.google.inject.AbstractModule
import dataaccess.DSLContextProvider
import org.jooq.DSLContext
import javax.naming.InitialContext
import javax.sql.DataSource

class DataAccessModule extends AbstractModule {
  static final String DATASOURCE_JNDI = "java:comp/env/jdbc/userDatabase-db"

  @Override
  protected void configure() {
    def dataSource = InitialContext.doLookup(DATASOURCE_JNDI) as DataSource
    bind(DataSource).toInstance(dataSource)
    bind(DSLContext).toProvider(new DSLContextProvider(dataSource, false))
  }
}
