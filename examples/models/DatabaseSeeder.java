package models;

import models.seeders.UserSeeder;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import me.paulschwarz.seeder.SeedRunner;
import me.paulschwarz.seeder.Seeder;

@Singleton
public class DatabaseSeeder extends SeedRunner {

  @Inject
  private DatabaseSeeder(Environment environment) {
    super(environment.isDev());
  }

  protected List<Seeder> provideSeeders() {
    return seeders(
        new UserSeeder(),
        // Provide more seeders here.
    );
  }
}
