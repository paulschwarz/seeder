[ ![Download](https://api.bintray.com/packages/paulschwarz/sbt-plugins/seeder/images/download.svg) ](https://bintray.com/paulschwarz/sbt-plugins/seeder/_latestVersion)

# Seeding

Seeding helps you set up test scenarios quickly. Two use cases immediately spring to mind.

1. I want to write tests that rely on the database being in a given state.
2. I want to demo my app with the database initialised to a known state.

## Factories

A `Factory` provides a `create` method which you must implement to return an instance of the model
you’re generating. Use the `create` method to set properties on your model before returning it. 
Factories have access to `faker` which is an instance of the 
[https://github.com/DiUS/java-faker](https://github.com/DiUS/java-faker) library. You can use 
`faker` to give your model reasonable looking random example values for those properties.

    protected User create() {
        return new User(
            faker.name().firstName(),
            faker.name().lastName());
    }

You may also declare modifiers in factories allowing you finer grained control of the models you 
generate. Examples of typical modifiers:

Modify a user to be an administrator:

    public static void administrator(User model) {
        model.setAdmin(true);
    }

Modify a user to have an incomplete profile:

    public static void incompleteProfile(User model) {
        model.setFirstName(null);
        model.setLastName(null);
    }
    
And now, how to use your factory. Use the `make` methods to get an instance of a model or a 
collection of models. The save methods behave exactly the same, but will also save the models to the 
persistence of your choice by invoking the callback provided in the constructor. 

The following code is typically prevalent in test cases where you want to test scenarios that depend 
on models being in a given state.

Make a single user:

        User user = userFactory.make();

Make multiple users:

        List<User> users = userFactory.make(5);

Save a single user:

        User user = userFactory.save();
            
Save multiple users:
            
        List<User> users = userFactory.save(5);
            
Overriding fields with a lambda modifier:
            
        userFactory
            .modify(user -> user.setName("Kermit"))
            .save();
            
Overriding fields with a declared modifier:
            
        userFactory
            .modify(UserFactory::administrator)
            .save(2);
            
Overriding fields with multiple modifiers:

        userFactory
            .modify(user -> user.setName("Kermit"))
            .modify(UserFactory::administrator)
            .save();
            
## Seeders

Until this point, you have everything you need to run automated tests against an in-memory database,
but what if you want to demo your app with your database initialised to a known state? That’s what 
seeders are for.

A `Seeder` makes use a factories to generate seed data. Typically, you will have multiple seeders
and a single implementation of the `SeedRunner`. All seeders must be registered in the seed runner 
in order for them to be run. Seeders are run in the order in which they are registered.

Since seeding is typically only done in a local environment, you might consider the following two
points.

1. Where should I put my seeders?
2. How do I ensure seeding only runs locally and not in production?

### Where put seeders

Seeders belong in neither `app` nor `test`. You might create a new top-level directory called `dev`.

For Intellij to recognise the `dev` folder as Java sources, *right click on `dev` > Mark Directory 
as > Sources Root*. 

### Run only locally

The `SeedRunner` constructor takes a boolean argument `enable`. This flag tells the seed runner
whether it should run. It’s a good idea to enable the seed runner based on the environment being
“local” (or “develop”), but not “testing” and certainly not “production”!

### Integrating with Play Framework

In development mode only, seeders are run automatically when the application starts. That is, when 
the first HTTP request is received. Seeders and factories can be declared in `dev/database/seeders` 
and `dev/database/factories`. Seeders would then be registered in `dev/database/DatabaseSeeder`.

You may also set up a Play module specifically to handle this in `dev/DevModule`.

    import com.google.inject.AbstractModule;
    import database.DatabaseSeeder;
    
    public class DevModule extends AbstractModule {
    
      @Override
      public void configure() {
        bind(DatabaseSeeder.class).asEagerSingleton();
      }
    }
        
Then, you would want to enable this module into your application’s config. To prevent this module 
from being available at all in production, it’s a good idea to use a separate configuration that you 
use locally to launch the app. You might call this `local.conf`, like this
    
    include "application.conf"
    
    play.modules.enabled += "DevModule"

In run configuration "Edit Configuration > SBT Task > VM parameters" add:
    
    -Dconfig.resource=local.conf
