package me.paulschwarz.seeder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class SeedRunnerTest {

  private Seeder mockSeeder;

  @Before
  public void setup() {
    mockSeeder = mock(Seeder.class);
  }

  @Test
  public void should_return_list_of_seeders() {
    List<Seeder> seeders = SeedRunner.seeders(mockSeeder);
    assertEquals(1, seeders.size());
  }

  @Test
  public void should_not_run_when_disabled() {
    SeedRunner seedRunner = spy(new ExampleSeedRunner(false));
    verify(seedRunner, never()).run();
  }

  @Test
  public void should_run() {
    new ExampleSeedRunner(true);
    verify(mockSeeder).clean();
    verify(mockSeeder).seed();
  }

  class ExampleSeedRunner extends SeedRunner {
    ExampleSeedRunner(boolean enable) {
      super(enable);
    }

    @Override
    protected List<Seeder> provideSeeders() {
      return Collections.singletonList(mockSeeder);
    }
  }

  @Test(expected = RuntimeException.class)
  public void should_error_when_providers_is_null() {
    new NullSeedRunner(true);
  }

  class NullSeedRunner extends SeedRunner {
    NullSeedRunner(boolean enable) {
      super(enable);
    }

    @Override
    protected List<Seeder> provideSeeders() {
      return null;
    }
  }
}
