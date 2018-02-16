package me.paulschwarz.seeder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.function.Consumer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FactoryTest {

  private Consumer<ExampleModel> mockSaveFunction;

  @Before
  @SuppressWarnings("unchecked")
  public void setUp() {
    mockSaveFunction = mock(Consumer.class);
  }

  @Test
  public void should_make_one_model() {
    ExampleFactory factory = new ExampleFactory(null);

    assertEquals("foo", factory.make().name);
  }

  @Test
  public void should_make_one_model_inline_modifier() {
    ExampleFactory factory = new ExampleFactory(null);

    ExampleModel model = factory.modify((m) -> m.name = "bar").make();

    assertEquals("bar", model.name);
  }

  @Test
  public void should_make_one_model_with_declared_modifier() {
    ExampleFactory factory = new ExampleFactory(null);

    ExampleModel model = factory.modify(ExampleFactory::allCaps).make();

    assertEquals("FOO", model.name);
  }

  @Test
  public void should_reset_factory_after_making_one_model() {
    ExampleFactory factory = new ExampleFactory(null);

    ExampleModel model1 = factory.modify(ExampleFactory::allCaps).make();
    ExampleModel model2 = factory.make();

    assertEquals("FOO", model1.name);
    assertEquals("foo", model2.name);
  }

  @Test
  public void should_reset_factory_after_making_many_models() {
    ExampleFactory factory = new ExampleFactory(null);

    List<ExampleModel> models = factory.modify(ExampleFactory::allCaps).make(5);
    ExampleModel model = factory.make();

    assertEquals(5, models.size());
    models.forEach((m) -> assertEquals("FOO", m.name));
    assertEquals("foo", model.name);
  }

  @Test
  public void should_save_one_model() {
    ExampleFactory factory = new ExampleFactory(mockSaveFunction);

    ExampleModel model = factory.save();

    assertEquals("foo", model.name);
    verify(mockSaveFunction).accept(model);
  }

  @Test
  public void should_save_one_model_inline_modifier() {
    ExampleFactory factory = new ExampleFactory(mockSaveFunction);

    ExampleModel model = factory.modify((m) -> m.name = "bar").save();

    assertEquals("bar", model.name);
    verify(mockSaveFunction).accept(model);
  }

  @Test
  public void should_save_one_model_declared_modifier() {
    ExampleFactory factory = new ExampleFactory(mockSaveFunction);

    ExampleModel model = factory.modify(ExampleFactory::allCaps).save();

    assertEquals("FOO", model.name);
    verify(mockSaveFunction).accept(model);
  }

  @Test
  public void should_reset_factory_after_saving_one_model() {
    ExampleFactory factory = new ExampleFactory(mockSaveFunction);

    ExampleModel model1 = factory.modify(ExampleFactory::allCaps).save();
    ExampleModel model2 = factory.save();

    assertEquals("FOO", model1.name);
    assertEquals("foo", model2.name);
  }

  @Test
  public void should_reset_factory_after_saving_many_models() {
    ExampleFactory factory = new ExampleFactory(mockSaveFunction);

    List<ExampleModel> models = factory.modify(ExampleFactory::allCaps).save(5);
    ExampleModel model = factory.save();

    assertEquals("foo", model.name);
    assertEquals(5, models.size());
    models.forEach((m) -> assertEquals("FOO", m.name));
  }

  static class ExampleFactory extends Factory<ExampleModel> {

    ExampleFactory(Consumer<ExampleModel> saveFunction) {
      super(saveFunction);
    }

    @Override
    protected ExampleModel create() {
      ExampleModel model = new ExampleModel();
      model.name = "foo";
      return model;
    }

    static void allCaps(ExampleModel model) {
      model.name = model.name.toUpperCase();
    }
  }

  static class ExampleModel {

    String name;
  }
}
