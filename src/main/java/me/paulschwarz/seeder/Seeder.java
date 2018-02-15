package me.paulschwarz.seeder;

public interface Seeder {

  /**
   * Implement to cleanup persisted models.
   */
  void clean();

  /**
   * Implement to create and persist models.
   */
  void seed();
}
