package com.excilys.cdb.mapper.model;

import java.util.List;
import java.util.stream.Collectors;

public interface IMapper<T,E> {
	
	E toDTO(T instance);
	
	T fromDTO(E instance);
	
	default List<E> toDTOList(List<T> l) {
		return l.stream().map(this::toDTO).collect(Collectors.toList());
	}
	
	default List<T> fromDTOList(List<E> l) {
		return l.stream().map(this::fromDTO).collect(Collectors.toList());
	}
}
