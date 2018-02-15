import com.google.inject.AbstractModule;
import models.DatabaseSeeder;

public class DevModule extends AbstractModule {

  @Override
  public void configure() {
    bind(DatabaseSeeder.class).asEagerSingleton();
  }
}
