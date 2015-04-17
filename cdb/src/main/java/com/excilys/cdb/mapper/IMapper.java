package com.excilys.cdb.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.RowMapper;

public interface IMapper<T,E> extends RowMapper<T>{
	
	E toDTO(T instance);
	
	T fromDTO(E instance);
	
	default List<E> toDTOList(List<T> l) {
		return l.stream().map(this::toDTO).collect(Collectors.toList());
	}
	
	default List<T> fromDTOList(List<E> l) {
		return l.stream().map(this::fromDTO).collect(Collectors.toList());
	}
}
