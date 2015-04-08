package com.excilys.cdb.persistence;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Company;

/**
 * CompanyDao
 * @author Kevin Bottero
 *
 */
@Repository("companyDao")
public class CompanyDao implements IDao<Company, Long>{
	
	public CompanyDao() {};
	
	private static Logger logger = LoggerFactory.getLogger(CompanyDao.class);
	
	@Autowired
	private CompanyMapper companyMapper;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
		private static final String	COUNT_ALL = "SELECT COUNT(id) FROM company;";
		private static final String	SELECT_ALL ="SELECT id, name FROM company;";
		private static final String	SELECT_ALL_ORDERED ="SELECT id, name FROM company ORDER BY";
		private static final String	SELECT_ONE ="SELECT id, name FROM company WHERE id=?;";
		private static final String	SELECT_SOME ="SELECT id, name FROM company ORDER BY";
		private static final String	SELECT_SOME_FILTERED ="SELECT id, name FROM company WHERE name LIKE ? ORDER BY";
		private static final String	DELETE_COMPUTER ="DELETE FROM computer WHERE company_id=?;";
		private static final String	DELETE_ONE = "DELETE FROM company WHERE id=?;";

	
	@Override
	public List<Company> getAll() throws DaoException {
		return jdbcTemplate.query(SELECT_ALL, companyMapper);
	}
	
	@Override
	public List<Company> getAll(DaoRequestParameter param) throws DaoException {
		StringBuilder request = new StringBuilder();
		
		request.append(SELECT_ALL_ORDERED);
		request.append(" ");
		addOrderByToRequest(request, param);
		request.append(";");
		
		return jdbcTemplate.query(request.toString(), companyMapper);
	}

	@Override
	public Long getNb() throws DaoException {
		Long nbElements = 10l;
		nbElements = jdbcTemplate.queryForObject(COUNT_ALL,Long.class);
		if (nbElements == null) {
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT);
		}
		return nbElements;
	}
	
	@Override
	public List<Company> getSome(DaoRequestParameter param) throws DaoException {
		
		List<Object> list = new ArrayList<Object>();
		
		StringBuilder request = new StringBuilder();

		if (param.getNameLike() != null) {
			request.append(SELECT_SOME_FILTERED);	
		} else {
			request.append(SELECT_SOME);
		}
		request.append(" ");
		addOrderByToRequest(request, param);
		request.append(" ");
		request.append(" LIMIT ? OFFSET ?;");
		
		if (param.getNameLike() != null) {			
			try {
				setWhenCondition (list,param);
			} catch (SQLException e) {
				throw new DaoException(DaoException.INVALID_ARGUMENT);
			}
		}
		if (param.getLimit() == null) {
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		} else {
			if (param.getLimit() < 0) {
				throw new DaoException(DaoException.INVALID_ARGUMENT);
			} else {
				list.add(param.getLimit()); 
			}
		}
		if (param.getOffset() == null) {
			list.add(0); 
		} else {
			if (param.getOffset() < 0) {
				throw new DaoException(DaoException.INVALID_ARGUMENT);
			} else {
				list.add(param.getOffset()); 
			}
		}
		return jdbcTemplate.query(request.toString(), list.toArray(),companyMapper);
	}

	@Override
	public Company getById(Long id) throws DaoException {
		if (id == null) {
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		Company comp=null;
		comp = jdbcTemplate.queryForObject(SELECT_ONE, new Object[] {id},companyMapper);
		if (comp == null ) {
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT);
		}

		return comp;
	}
	
	@Override
	public void delete(Long id)  throws DaoException {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		int nb = jdbcTemplate.update(DELETE_ONE, new Object[] {id});
		if (nb==0) {
			throw new DaoException(DaoException.CAN_NOT_DELETE_ELEMENT);
		}
	}
	
	public void addOrderByToRequest(StringBuilder request, DaoRequestParameter param) throws DaoException {
		if ((param.getColToOrderBy() == null) || (param.getColToOrderBy().size() == 0)){
			request.append(ComputerMapper.DEFAULT_ID);
			request.append(" ");
			//ASC or DESC ?
			if (param.getOrder() == null) {
				request.append("ASC");
			} else {
				switch (param.getOrder()) {
				case ASC:
					request.append("ASC");
					break;
				case DESC:
					request.append("DESC");
					break;
				default:
					throw new DaoException(DaoException.INVALID_ARGUMENT);
				}
			}
		} else {
			StringBuilder strgBuild = new StringBuilder();
			for (String strg : param.getColToOrderBy()) {
				if (ComputerMapper.mapBDModel.containsKey(strg)) {
					if (strgBuild.length() != 0) {
						strgBuild.append(",");
						strgBuild.append(ComputerMapper.mapBDModel.get(strg));
					} else {
						strgBuild.append(ComputerMapper.mapBDModel.get(strg));
						strgBuild.append(" ");
						//ASC or DESC ?
						if (param.getOrder() == null) {
							strgBuild.append("ASC");
						} else {
							switch (param.getOrder()) {
							case ASC:
								strgBuild.append("ASC");
								break;
							case DESC:
								strgBuild.append("DESC");
								break;
							default:
								throw new DaoException(DaoException.INVALID_ARGUMENT);
							}
						}
					}
				} else {
					throw new DaoException(DaoException.INVALID_ARGUMENT);
				}
			}
			request.append(strgBuild.toString());
		}
	}
	
	public void setWhenCondition (List<Object> list,DaoRequestParameter param) throws SQLException {
		StringBuilder filter = new StringBuilder();
		switch (param.getNameFiltering()) {
		case POST:
			filter.append(param.getNameLike());
			filter.append('%');
			break;
		case PRE:
			filter.append('%');
			filter.append(param.getNameLike());
			break;
		case POST_AND_PRE:
			filter.append('%');
			filter.append(param.getNameLike());
			filter.append('%');
			break;
		case NONE:
			filter.append(param.getNameLike());
			break;
		default:
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		list.add(filter.toString());
	}
	
	
}
