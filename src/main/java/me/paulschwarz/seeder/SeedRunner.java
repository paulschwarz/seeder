package me.paulschwarz.seeder;

import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SeedRunner {

  private static final Logger LOG = LoggerFactory.getLogger(SeedRunner.class);

  protected static List<Seeder> seeders(Seeder... seeders) {
    return Arrays.asList(seeders);
  }

  public void run() {
    LOG.info("Seeding started");

    for (Seeder seeder : provideSeeders()) {
      String seederName = seeder.getClass().getSimpleName();
      LOG.info("Seeding with " + seederName);

      seeder.clean();
      seeder.seed();

      LOG.info("Seeded with " + seederName);
    }

    LOG.info("Seeding done");
  }

  /**
   * Provide a list of seeders.
   *
   * @return list of seeders
   */
  protected abstract List<Seeder> provideSeeders();
}
