package models.factories;

import me.paulschwarz.seeder.Factory;
import models.User;

public class UserFactory extends Factory<User> {

  public UserFactory() {
    super(User::save);
  }

  @Override
  protected User create() {
    return new User(
        faker.name().firstName(),
        faker.name().lastName());
  }

  public static void administrator(User model) {
    model.setAdmin(true);
  }

  public static void incompleteProfile(User model) {
    model.setFirstName(null);
    model.setLastName(null);
  }
}
