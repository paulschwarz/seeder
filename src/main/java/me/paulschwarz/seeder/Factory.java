package me.paulschwarz.seeder;

import com.github.javafaker.Faker;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class Factory<M> {

  protected static final Faker faker = new Faker();
  private List<Consumer<M>> modifiers = new ArrayList<>();
  private Consumer<M> save;

  public Factory(Consumer<M> save) {
    this.save = save;
  }

  /**
   * Set values on the model using a Modifier.
   *
   * @param modifier the modifier that will be applied to the model.
   * @return Factory<M>
   */
  public Factory<M> modify(Consumer<M> modifier) {
    modifiers.add(modifier);

    return this;
  }

  /**
   * Make 1 model using this factory.
   *
   * @return M
   */
  public M make() {
    M model = makeAndModify();

    reset();

    return model;
  }

  /**
   * Make n models using this factory.
   *
   * @param count the number of models to be created.
   * @return List<M>
   */
  public List<M> make(int count) {
    List<M> models = new ArrayList<>(count);

    while (count-- > 0) {
      models.add(makeAndModify());
    }

    reset();

    return models;
  }

  /**
   * Save 1 model using this factory.
   *
   * @return M
   */
  public M save() {
    M model = createAndSave();

    reset();

    return model;
  }

  /**
   * Save n models using this factory.
   *
   * @param count the number of models to be created.
   * @return List<M>
   */
  public List<M> save(int count) {
    List<M> models = new ArrayList<>(count);

    while (count-- > 0) {
      models.add(createAndSave());
    }

    reset();

    return models;
  }

  /**
   * Provide a valid instance of the model.
   *
   * @return M
   */
  protected abstract M create();

  private M makeAndModify() {
    M model = create();

    modifiers.forEach((modifier) -> modifier.accept(model));

    return model;
  }

  private M createAndSave() {
    M model = makeAndModify();

    save.accept(model);

    return model;
  }

  private void reset() {
    modifiers = new ArrayList<>();
  }
}
