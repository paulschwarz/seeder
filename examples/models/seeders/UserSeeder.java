package models.seeders;

import io.ebean.Ebean;
import models.factories.UserFactory;
import me.paulschwarz.seeder.Seeder;
import models.User;

public class UserSeeder implements Seeder {

  private UserFactory userFactory = new UserFactory();

  @Override
  public void clean() {
    Ebean.getDefaultServer().createQuery(User.class).delete();
  }

  @Override
  public void seed() {
    // Create and save an administrator
    userFactory
        .modify(UserFactory::administrator)
        .save();

    // Create and save a user with an incomplete profile
    userFactory
        .modify(UserFactory::incompleteProfile)
        .save();

    // Create and save 10 normal users
    userFactory
        .save(10);
  }
}
