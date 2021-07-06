package dataaccess

import com.google.inject.Provider
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import javax.sql.DataSource

class DSLContextProvider implements Provider<DSLContext> {
  private final DataSource dataSource
  private final Boolean isReadOnly

  DSLContextProvider(DataSource dataSource, final Boolean isReadOnly) {
    this.dataSource = dataSource
    this.isReadOnly = isReadOnly
  }

  DSLContext get() {
    Settings settings = new Settings().withRenderSchema(false).withExecuteLogging(false)
    DSL.using(dataSource, SQLDialect.MYSQL, settings)
  }
}
