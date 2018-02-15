package me.paulschwarz.seeder;

import java.util.List;
import java.util.function.Consumer;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class FactoryTest {

  @Test
  public void make() {
    ExampleFactory factory = new ExampleFactory(null);

    Assert.assertEquals("foo", factory.make().name);
  }

  @Test
  public void makeWithInlineLambda() {
    ExampleFactory factory = new ExampleFactory(null);

    ExampleModel model = factory.modify((m) -> m.name = "bar").make();

    Assert.assertEquals("bar", model.name);
  }

  @Test
  public void makeWithDeclaredLambda() {
    ExampleFactory factory = new ExampleFactory(null);

    ExampleModel model = factory.modify(ExampleFactory::allCaps).make();

    Assert.assertEquals("FOO", model.name);
  }

  @Test
  public void makeWithLambdaAndReset() {
    ExampleFactory factory = new ExampleFactory(null);

    ExampleModel model1 = factory.modify(ExampleFactory::allCaps).make();

    ExampleModel model2 = factory.make();

    Assert.assertEquals("FOO", model1.name);
    Assert.assertEquals("foo", model2.name);
  }

  @Test
  public void makeMultipleAndReset() {
    ExampleFactory factory = new ExampleFactory(null);

    List<ExampleModel> models = factory.modify(ExampleFactory::allCaps).make(5);

    ExampleModel model = factory.make();

    Assert.assertEquals("foo", model.name);
    Assert.assertEquals(5, models.size());
    models.forEach((m) -> Assert.assertEquals("FOO", m.name));
  }

  @Test
  public void save() {
    ExampleORM saveFunction = Mockito.mock(ExampleORM.class);
    ExampleFactory factory = new ExampleFactory(saveFunction);

    ExampleModel model = factory.save();

    Assert.assertEquals("foo", model.name);
    Mockito.verify(saveFunction).accept(model);
  }

  @Test
  public void saveWithInlineLambda() {
    ExampleORM saveFunction = Mockito.mock(ExampleORM.class);
    ExampleFactory factory = new ExampleFactory(saveFunction);

    ExampleModel model = factory.modify((m) -> m.name = "bar").save();

    Assert.assertEquals("bar", model.name);
    Mockito.verify(saveFunction).accept(model);
  }

  @Test
  public void saveWithDeclaredLambda() {
    ExampleORM saveFunction = Mockito.mock(ExampleORM.class);
    ExampleFactory factory = new ExampleFactory(saveFunction);

    ExampleModel model = factory.modify(ExampleFactory::allCaps).save();

    Assert.assertEquals("FOO", model.name);
    Mockito.verify(saveFunction).accept(model);
  }

  @Test
  public void saveMultipleAndReset() {
    ExampleORM saveFunction = Mockito.mock(ExampleORM.class);
    ExampleFactory factory = new ExampleFactory(saveFunction);

    List<ExampleModel> models = factory.modify(ExampleFactory::allCaps).save(5);

    ExampleModel model = factory.save();

    Assert.assertEquals("foo", model.name);
    Assert.assertEquals(5, models.size());
    models.forEach((m) -> Assert.assertEquals("FOO", m.name));
  }

  interface ExampleORM extends Consumer<ExampleModel> {}

  static class ExampleFactory extends Factory<ExampleModel> {

    ExampleFactory(Consumer<ExampleModel> saveFunction) {
      super(saveFunction);
    }

    static void allCaps(ExampleModel model) {
      model.name = model.name.toUpperCase();
    }

    @Override
    protected ExampleModel create() {
      ExampleModel model = new ExampleModel();

      model.name = "foo";

      return model;
    }
  }

  static class ExampleModel {

    String name;
  }
}
