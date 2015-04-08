package com.excilys.cdb.persistence;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Computer;

/**
 * ComputerDao
 * @author Kevin Bottero
 *
 */
@Repository("computerDao")
public class ComputerDao  implements IDao<Computer, Long> {
	
	private static Logger logger = LoggerFactory.getLogger(ComputerDao.class);
	
	@Autowired
	private ComputerMapper computerMapper;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final String COUNT_ALL ="SELECT COUNT(id) FROM computer;";
	private static final String COUNT_ALL_FILTERED = "SELECT COUNT(id) FROM computer WHERE name LIKE ? ORDER BY";
	private static final String SELECT_ALL = "SELECT id, name, introduced, discontinued, company_id FROM computer;";
	private static final String SELECT_ALL_ORDERED = "SELECT id, name, introduced, discontinued, company_id FROM computer ORDER BY";
	private static final String SELECT_SOME = "SELECT id, name, introduced, discontinued, company_id FROM computer ORDER BY";
	private static final String SELECT_SOME_FILTERED = "SELECT id, name, introduced, discontinued, company_id FROM computer WHERE name LIKE ? ORDER BY";
	private static final String SELECT_ONE = "SELECT id, name,introduced, discontinued, company_id FROM computer WHERE id=?;";
	private static final String INSERT_ONE = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?,?,?,?) ;";
	private static final String UPDATE_ONE = "UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE id=?;";
	private static final String DELETE_ONE = "DELETE FROM computer WHERE id=?;";
	private static final String DELETE_BY_COMPANY = "DELETE FROM computer WHERE company_id=?;";
	private static final String COUNT_ONE = "SELECT COUNT(id) FROM computer WHERE id=?;";


	@Override
	public List<Computer> getAll() throws DaoException {
		return jdbcTemplate.query(SELECT_ALL, computerMapper);
	}

	@Override
	public List<Computer> getAll(DaoRequestParameter param) throws DaoException {
		logger.info("getAll(param) method");

		StringBuilder request = new StringBuilder();

		request.append(SELECT_ALL_ORDERED);
		request.append(" ");
		addOrderByToRequest(request, param);

		request.append(";");
		return jdbcTemplate.query(request.toString(), computerMapper);
	}

	@Override
	public Long getNb() throws DaoException {
		Long nbElements = jdbcTemplate.queryForObject(COUNT_ALL, Long.class);
		if (nbElements == null) {
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT);
		}
		return nbElements;
	}
	
	@Override
	public Long getNb(DaoRequestParameter param) throws DaoException {
		logger.info("getNb(param) method");
		if (param == null) {
			return getNb();
		}
		Long nbElements = null;
		List<Object> list = new ArrayList<Object>();

		StringBuilder request = new StringBuilder();

		if (param.getNameLike() != null) {
			request.append(COUNT_ALL_FILTERED);
			request.append(" ");
			addOrderByToRequest(request, param);
			request.append(" LIMIT ? OFFSET ?;");
			
			if (param.getNameLike() != null) {
				try {
					setWhenCondition (list, param);
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
		} else {
			request.append(COUNT_ALL);
		}

		nbElements = jdbcTemplate.queryForObject(request.toString(),list.toArray(),Long.class);
		if (nbElements == null) {
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT);
		}
		
		return nbElements;
	}

	@Override
	public List<Computer> getSome(DaoRequestParameter param) throws DaoException {
		logger.info("getSome(param) method");

		StringBuilder request = new StringBuilder();
		List<Object> list = new ArrayList<Object>();

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
					setWhenCondition (list, param);
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
		return jdbcTemplate.query(request.toString(), list.toArray(),computerMapper);
	}

	@Override
	public Computer getById(Long id) throws DaoException {
		if (id == null) {
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		Computer comp;
		comp = jdbcTemplate.queryForObject(SELECT_ONE, new Object[] {id}, computerMapper);
		if (comp == null) {
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT);
		}
		return comp;
	}

	@Override
	public void save(Computer computer) throws DaoException {
		if (computer == null) {
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		int nb;
		boolean update=false;
		List<Object> list = new ArrayList<Object>();
		if (! jdbcTemplate.queryForObject(COUNT_ONE, new Object[] {computer.getId()}, Integer.class).equals(0)) {
			update = true;
		}
		
		if (!update)
		{   
		    SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
		    insert.withTableName("computer");
		    insert.usingGeneratedKeyColumns("id");
		    final Map<String, Object> parameters = new HashMap<>();
			setComputerInMap (parameters,computer);
		    final Number key = insert.executeAndReturnKey(parameters);
		    final long pk = key.longValue();
			computer.setId(pk);
		} else {
			setComputerInList (list,computer);
			list.add(computer.getId());
			nb = jdbcTemplate.update(UPDATE_ONE,list.toArray());
			if (nb==0) {
				throw new DaoException(DaoException.CAN_NOT_UPDATE_ELEMENT);
			}
		}
	}

	@Override
	public void delete(Long id) throws DaoException {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		int nb = jdbcTemplate.update(DELETE_ONE, new Object[] {id});
		if (nb==0) {
			throw new DaoException(DaoException.CAN_NOT_DELETE_ELEMENT);
		}
	}
	
	public void deleteByCompany(Long companyId) throws DaoException {
		if (companyId == null) {
			throw new IllegalArgumentException();
		}
		int nb = jdbcTemplate.update(DELETE_BY_COMPANY, new Object[] {companyId});
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
	
	public void setComputerInMap (final Map<String, Object> map, Computer computer) throws DaoException {
		if (computer.getName() != null) {
			map.put("name",computer.getName());
		} else {
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		if (computer.getIntroductionDate() != null) {
			map.put("introduced",Timestamp.valueOf(computer.getIntroductionDate()));
		} else {
			map.put("introduced",null);
		}
		if (computer.getDiscontinuedDate() != null) {
			map.put("discontinued",Timestamp.valueOf(computer.getDiscontinuedDate()));
		}else {
			map.put("discontinued",null);
		}
		if (computer.getConstructor() != null) {
			map.put("company_id",computer.getConstructor().getId());
		} else {
			map.put("company_id",null);
		}
	}
	
	public void setComputerInList (List<Object> list, Computer computer) throws DaoException {
		if (computer.getName() != null) {
			list.add(computer.getName());
		} else {
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		if (computer.getIntroductionDate() != null) {
			list.add(Timestamp.valueOf(computer.getIntroductionDate()));
		} else {
			list.add(null);
		}
		if (computer.getDiscontinuedDate() != null) {
			list.add(Timestamp.valueOf(computer.getDiscontinuedDate()));
		}else {
			list.add(null);
		}
		if (computer.getConstructor() != null) {
			list.add(computer.getConstructor().getId());
		} else {
			list.add(null);
		}
	}
}
