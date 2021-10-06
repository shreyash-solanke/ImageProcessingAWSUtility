package io.shreyash.assignment2.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io.shreyash.assignment2.models.UserImages;

public interface UserImagesRepository extends CrudRepository<UserImages, Long>{

	public List<UserImages> findByUser_UserId(long id);
	public UserImages findByImgName(String imgName);
}
