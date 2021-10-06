package io.shreyash.assignment2.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io.shreyash.assignment2.models.TransformedImages;
import io.shreyash.assignment2.models.UserImages;

public interface TransformedImageRepository extends CrudRepository<TransformedImages, Long>{

	public List<TransformedImages> findByUser_UserId(long id);
	public UserImages findByTransformedImgName(String transformedImgName);
	public List<TransformedImages> findByJobId(long jobId);
}
