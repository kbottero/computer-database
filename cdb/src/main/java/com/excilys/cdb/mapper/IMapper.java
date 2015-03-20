package com.excilys.cdb.mapper;

import java.sql.ResultSet;

public interface IMapper<T> {

	public T mapFromRow(ResultSet curs);
}
