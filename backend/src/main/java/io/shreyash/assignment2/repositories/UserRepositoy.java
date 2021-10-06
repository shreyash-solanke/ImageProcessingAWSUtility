package io.shreyash.assignment2.repositories;

import org.springframework.data.repository.CrudRepository;

import io.shreyash.assignment2.models.User;

public interface UserRepositoy extends CrudRepository<User, Long>{

	public User findByEmail(String email);
	
}
