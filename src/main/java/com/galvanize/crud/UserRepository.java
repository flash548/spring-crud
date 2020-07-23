package com.galvanize.crud;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value="select count(*) from users", nativeQuery = true)
    public int getUserCountTotal();

    @Query(value="select * from users where email = :email", nativeQuery = true)
    public User getUserByEmail(String email);
}
