package me.paulschwarz.seeder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SeedRunnerTest {

  @Test
  public void should_return_list_of_seeders() {
    List<Seeder> seeders = SeedRunner.seeders(mock(Seeder.class));
    assertEquals(1, seeders.size());
  }

  @Test
  public void should_not_run_when_disabled() {
    SeedRunner seedRunner = spy(new SeedRunner(false) {
      @Override
      protected List<Seeder> provideSeeders() {
        return null;
      }
    });

    verify(seedRunner, never()).run();
  }

  @Test(expected = RuntimeException.class)
  public void should_error_when_providers_is_null() {
    spy(new SeedRunner(true) {
      @Override
      protected List<Seeder> provideSeeders() {
        return null;
      }
    });
  }

  @Test
  public void should_run() {
    Seeder mockSeeder = mock(Seeder.class);

    new SeedRunner(true) {
      @Override
      protected List<Seeder> provideSeeders() {
        return seeders(mockSeeder);
      }
    };

    verify(mockSeeder).clean();
    verify(mockSeeder).seed();
  }
}
