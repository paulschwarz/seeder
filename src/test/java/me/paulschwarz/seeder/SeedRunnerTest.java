package me.paulschwarz.seeder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
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
  public void should_run() {
    Seeder mockSeeder1 = mock(Seeder.class);
    Seeder mockSeeder2 = mock(Seeder.class);
    SeedRunner seedRunner = new SeedRunner() {
      @Override
      protected List<Seeder> provideSeeders() {
        return seeders(mockSeeder1, mockSeeder2);
      }
    };

    seedRunner.run();

    verify(mockSeeder1).clean();
    verify(mockSeeder1).seed();
    verify(mockSeeder2).clean();
    verify(mockSeeder2).seed();
  }
}
