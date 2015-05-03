package com.excilys.cdb.persistence.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.NullPrecedence;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.dao.IDao;
import com.excilys.cdb.persistence.exception.DaoException;
import com.excilys.cdb.persistence.util.DaoRequestParameter;

/**
 * ComputerDao
 * @author Kevin Bottero
 *
 */
@Repository("computerDao")
public class ComputerDao  implements IDao<Computer, Long> {

	/*Primary key*/
	public static final String DEFAULT_ID = "computer.id";
	
	/*Associate an attribute to a column label in the database*/
	public static final HashMap<String,String> mapBDModel;
	static {
		mapBDModel = new HashMap<String,String>();
		mapBDModel.put("id","computer.id");
		mapBDModel.put("name","computer.name");
		mapBDModel.put("introduced","computer.introduced");
		mapBDModel.put("discontinued","computer.discontinued");
		mapBDModel.put("constructor","company.name");
	}

	@Autowired
	private SessionFactory sessionFactory;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerDao.class);
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Computer> getAll() {
		return sessionFactory.getCurrentSession().createCriteria(Computer.class).list();

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Computer> getAll(DaoRequestParameter param) {
		if (param == null) {
			LOGGER.error("Parameter is null");
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Computer.class, "computer");
		addOrderByToRequest(criteria, param);
		return criteria.list();
	}

	@Override
	public Long getNb() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Computer.class);
		Long nbElements = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		if (nbElements == null) {
			LOGGER.error("Can't get nb computer");
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT);
		}
		return nbElements;
	}
	
	@Override
	public Long getNb(DaoRequestParameter param) {
		if (param == null) {
			LOGGER.error("Can't get nb computer");
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Computer.class, "computer");
		criteria.createCriteria("constructor", "company", JoinType.LEFT_OUTER_JOIN);
		addOrderByToRequest(criteria, param);
		if (param.getNameLike() != null) {
			try {
				setWhenCondition (criteria, param);
			} catch (SQLException e) {
				LOGGER.error("Error adding restriction to the request.");
				throw new DaoException(DaoException.INVALID_ARGUMENT);
			}
		}
		Long nbElements = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		if (nbElements == null) {
			LOGGER.error("Can't get nb computer");
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT);
		}
		return nbElements;
	}

	@Override
	public List<Computer> getSome(DaoRequestParameter param) {
		if (param == null) {
			LOGGER.error("Parameter is null");
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Computer.class, "computer");
		criteria.createCriteria("constructor", "company", JoinType.LEFT_OUTER_JOIN);
		if (param.getNameLike() != null) {
			try {
				setWhenCondition (criteria, param);
			} catch (SQLException e) {
				LOGGER.error("Error adding restriction to the request.");
				throw new DaoException(DaoException.INVALID_ARGUMENT);
			}	
		}
		addOrderByToRequest(criteria, param);
		if (param.getLimit() == null) {
			LOGGER.error("Parameter limit argument is null");
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		} else {
			if (param.getLimit() < 0) {
				LOGGER.error("Parameter limit argument null or negative");
				throw new DaoException(DaoException.INVALID_ARGUMENT);
			} else {
				criteria.setMaxResults(param.getLimit().intValue()); 
			}
		}
		if (param.getOffset() != null) {
			if (param.getOffset() < 0) {
				LOGGER.error("Parameter offset argument is null");
				throw new DaoException(DaoException.INVALID_ARGUMENT);
			} else {
				criteria.setFirstResult(param.getOffset().intValue()); 
			}
		}
		@SuppressWarnings("unchecked")
		List<Computer> list = criteria.list();
		return list;
	}

	@Override
	public Computer getById(Long id) {
		if (id == null) {
			LOGGER.error("Id is null");
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		Computer comp;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Computer.class);
		criteria.add(Restrictions.idEq(id));
		comp = (Computer) criteria.uniqueResult();
		if (comp == null) {
			LOGGER.error("Can't get computer with id="+id);
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT);
		}
		return comp;
	}

	@Override
	public void save(Computer computer) {
		if (computer == null) {
			LOGGER.error("Computer is null");
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Computer.class);
		criteria.add(Restrictions.idEq(computer.getId()));
		boolean update=false;
		if (! criteria.setProjection(Projections.rowCount()).uniqueResult().equals(new Long(0))) {
			update = true;
		}
		if (!update) {   
			computer.setId((Long)sessionFactory.getCurrentSession().save(computer));
		} else {
			sessionFactory.getCurrentSession().update(computer);
		}
	}

	@Override
	public void delete(Long id) {
		if (id == null) {
			LOGGER.error("Id is null");
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		sessionFactory.getCurrentSession().delete(this.getById(id));
	}

	/**
	 * Delete all computer build by the company with the given id.
	 * @param companyId
	 * @throws DaoException if the id in null.
	 */
	public void deleteByCompany(Long companyId) {
		if (companyId == null) {
			LOGGER.error("Company id is null");
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		Criteria cr = sessionFactory.getCurrentSession().createCriteria(Computer.class);
		cr.createCriteria("constructor", "company", JoinType.LEFT_OUTER_JOIN);
		@SuppressWarnings("unchecked")
		List<Computer> computersToDelete = cr.add(Restrictions.eq("company.id", companyId)).list();
		for (Computer c : computersToDelete) {
			sessionFactory.getCurrentSession().delete(c);
		}
	}
	
	private void addOrderByToRequest(Criteria criteria, DaoRequestParameter param) {
		if ((param.getOrders() == null) || (param.getOrders().size() == 0)) {
			criteria.addOrder(Order.asc(DEFAULT_ID).nulls(NullPrecedence.LAST));
		} else {
			for (Map.Entry<String, com.excilys.cdb.persistence.util.DaoRequestParameter.Order> e : param.getOrders().entrySet()) {
				Order order = null;
				if (mapBDModel.containsValue(e.getKey())) {
					switch(e.getValue()) {
					case ASC:
						order = Order.asc(e.getKey()).nulls(NullPrecedence.LAST);
						break;
					case DESC:
						order = Order.desc(e.getKey()).nulls(NullPrecedence.LAST);
						break;
					default:
						LOGGER.error("Unknown value for Order enumerate.");
						throw new DaoException(DaoException.INVALID_ARGUMENT);
					}
					criteria.addOrder(order);
				} else {
					throw new DaoException(DaoException.INVALID_ARGUMENT);
				}
			}
		}
	}

	private void setWhenCondition (Criteria criteria, DaoRequestParameter param) throws SQLException {
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
			LOGGER.error("Unknown value for NameFiltering enumerate.");
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		
		Criterion computerName = Restrictions.like("computer.name", filter.toString());
		Criterion companyName = Restrictions.like("company.name", filter.toString());
		LogicalExpression orExp = Restrictions.or(computerName, companyName);
		criteria.add(orExp);
	}
}
