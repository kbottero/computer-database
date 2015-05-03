package com.excilys.cdb.mapper.model;

import java.util.List;
import java.util.stream.Collectors;

public interface IMapper<T,E> {
	
	/**
	 * Create the DTO counterpart of an entity.
	 * @param instance
	 * 				Instance to transform into a DTO
	 * @return Created instance
	 */
	E toDTO(T instance);
	
	/**
	 * Create an entity from a DTO.
	 * @param instance
	 * 				DTO of an entity
	 * @return Created instance
	 */
	T fromDTO(E instance);
	
	/**
	 * Create the DTO counterpart of a list of entities.
	 * @param l
	 * 				List of instances to transform into  DTOs
	 * @return Created list
	 */
	default List<E> toDTOList(List<T> l) {
		return l.stream().map(this::toDTO).collect(Collectors.toList());
	}
	
	/**
	 * Create a list of entities from a list of DTOs.
	 * @param l
	 * 				List of DTOs
	 * @return Created list
	 */
	default List<T> fromDTOList(List<E> l) {
		return l.stream().map(this::fromDTO).collect(Collectors.toList());
	}
}
