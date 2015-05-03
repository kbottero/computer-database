package com.excilys.cdb.persistence.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.NullPrecedence;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.dao.IDao;
import com.excilys.cdb.persistence.exception.DaoException;
import com.excilys.cdb.persistence.util.DaoRequestParameter;

/**
 * CompanyDao
 * @author Kevin Bottero
 *
 */
@Repository("companyDao")
public class CompanyDao implements IDao<Company, Long>{
	
	/* Primary Key.	 */
	public static final String DEFAULT_ID = "company.id";

	/*Associate an attribute to a column label in the database*/
	public static final HashMap<String,String> mapBDModel;
	static {
		mapBDModel = new HashMap<String,String>();
		mapBDModel.put("id","company.id");
		mapBDModel.put("name","company.name");
	}
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Company.class);
	
	public CompanyDao() {};

	@Override
	@SuppressWarnings("unchecked")
	public List<Company> getAll() {
		return sessionFactory.getCurrentSession().createCriteria(Company.class).list();
	}
	
	@Override
	public List<Company> getAll(DaoRequestParameter param) {
		if (param == null) {
			LOGGER.error("Parameter is null");
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Company.class, "company");
		addOrderByToRequest(criteria, param);
		@SuppressWarnings("unchecked")
		List<Company> list = criteria.list();
		return list;
	}

	@Override
	public Long getNb() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Company.class, "company");
		Long nbElements = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		if (nbElements == null) {
			LOGGER.error("Can't get nb company");
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT);
		}
		return nbElements;
	}
	
	@Override
	public Long getNb(DaoRequestParameter param) {
		if (param == null) {
			LOGGER.error("Parameter is null");
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Company.class, "company");
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
	public List<Company> getSome(DaoRequestParameter param) {
		if (param == null) {
			LOGGER.error("Parameter is null");
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Company.class, "company");
		addOrderByToRequest(criteria, param);
		if (param.getNameLike() != null) {
			try {
				setWhenCondition (criteria, param);
			} catch (SQLException e) {
				LOGGER.error("Error adding restriction to the request.");
				throw new DaoException(DaoException.INVALID_ARGUMENT);
			}
		}
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
		List<Company> list = criteria.list();
		return list;
	}

	@Override
	public Company getById(Long id) {
		if (id == null) {
			LOGGER.error("Id is null");
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		Company comp=null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Company.class, "company");
		criteria.add(Restrictions.idEq(id));
		comp = (Company) criteria.uniqueResult();
		if (comp == null) {
			LOGGER.error("Can't get company with id="+id);
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT);
		}
		return comp;
	}
	
	@Override
	public void delete(Long id)  {
		if (id == null) {
			LOGGER.error("Id is null");
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		sessionFactory.getCurrentSession().delete(this.getById(id));
	}
	
	private void addOrderByToRequest(Criteria criteria, DaoRequestParameter param) {
		if ((param.getOrders() == null) || (param.getOrders().size() == 0)) {
			criteria.addOrder(Order.asc(DEFAULT_ID).nulls(NullPrecedence.LAST));
		} else {
			for (Map.Entry<String, com.excilys.cdb.persistence.util.DaoRequestParameter.Order> e : param.getOrders().entrySet()) {
				Order order = null;
				if (mapBDModel.containsKey(e.getKey())) {
					switch(e.getValue()) {
					case ASC:
						order = Order.asc(mapBDModel.get(e.getKey())).nulls(NullPrecedence.LAST);
						break;
					case DESC:
						order = Order.desc(mapBDModel.get(e.getKey())).nulls(NullPrecedence.LAST);
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
		
		criteria.add( Restrictions.like("company.name", filter.toString()));
	}
}
