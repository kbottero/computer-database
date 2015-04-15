package com.excilys.cdb.persistence;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.NullPrecedence;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Computer;

/**
 * ComputerDao
 * @author Kevin Bottero
 *
 */
@Transactional
@Repository("computerDao")
public class ComputerDao  implements IDao<Computer, Long> {
	
	private static Logger logger = LoggerFactory.getLogger(ComputerDao.class);
	
	@Autowired
	private ComputerMapper computerMapper;

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Computer> getAll() throws DaoException {
		return sessionFactory.getCurrentSession().createCriteria(Computer.class).list();

	}

	@Override
	public List<Computer> getAll(DaoRequestParameter param) throws DaoException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Computer.class, "computer");
		addOrderByToRequest(criteria, param);
		return criteria.list();
	}

	@Override
	public Long getNb() throws DaoException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Computer.class);
		Long nbElements = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		if (nbElements == null) {
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT);
		}
		return nbElements;
	}
	
	@Override
	public Long getNb(DaoRequestParameter param) throws DaoException {
		if (param == null) {
			return getNb();
		}
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Computer.class, "computer");
		criteria.createCriteria("constructor", "company", JoinType.LEFT_OUTER_JOIN);
		addOrderByToRequest(criteria, param);
		if (param.getNameLike() != null) {
			try {
				setWhenCondition (criteria, param);
			} catch (SQLException e) {
				throw new DaoException(DaoException.INVALID_ARGUMENT);
			}
		}
		Long nbElements = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		if (nbElements == null) {
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT);
		}
		return nbElements;
	}

	@Override
	public List<Computer> getSome(DaoRequestParameter param) throws DaoException {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Computer.class, "computer");
		criteria.createCriteria("constructor", "company", JoinType.LEFT_OUTER_JOIN);
		if (param.getNameLike() != null) {
			try {
				setWhenCondition (criteria, param);
			} catch (SQLException e) {
				throw new DaoException(DaoException.INVALID_ARGUMENT);
			}	
		}
		addOrderByToRequest(criteria, param);
		if (param.getLimit() == null) {
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		} else {
			if (param.getLimit() < 0) {
				throw new DaoException(DaoException.INVALID_ARGUMENT);
			} else {
				criteria.setMaxResults(param.getLimit().intValue()); 
			}
		}
		if (param.getOffset() != null) {
			if (param.getOffset() < 0) {
				throw new DaoException(DaoException.INVALID_ARGUMENT);
			} else {
				criteria.setFirstResult(param.getOffset().intValue()); 
			}
		}
		return criteria.list();
	}

	@Override
	public Computer getById(Long id) throws DaoException {
		if (id == null) {
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		Computer comp;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Computer.class);
		criteria.add(Restrictions.idEq(id));
		comp = (Computer) criteria.uniqueResult();
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
	public void delete(Long id) throws DaoException {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		sessionFactory.getCurrentSession().delete(this.getById(id));
	}
	
	public void deleteByCompany(Long companyId) throws DaoException {
		if (companyId == null) {
			throw new IllegalArgumentException();
		}
		Criteria cr = sessionFactory.getCurrentSession().createCriteria(Computer.class);
		List<Computer> computersToDelete = cr.add(Restrictions.eq("company_id", companyId)).list();
		for (Computer c : computersToDelete) {
			sessionFactory.getCurrentSession().delete(c);
		}
	}
	
	public void addOrderByToRequest(Criteria criteria, DaoRequestParameter param) throws DaoException {
		if ((param.getColToOrderBy() == null) || (param.getColToOrderBy().size() == 0)){
			if (param.getOrder() == null) {
				criteria.addOrder(Order.asc(ComputerMapper.DEFAULT_ID));
			} else {
				switch (param.getOrder()) {
				case ASC:
					criteria.addOrder(Order.asc(ComputerMapper.DEFAULT_ID));
					break;
				case DESC:
					criteria.addOrder(Order.desc(ComputerMapper.DEFAULT_ID));
					break;
				default:
					throw new DaoException(DaoException.INVALID_ARGUMENT);
				}
			}
		} else {
			for (String strg : param.getColToOrderBy()) {
				if (ComputerMapper.mapBDModel.containsKey(strg)) {
					if (param.getOrder() == null) {
						criteria.addOrder(Order.asc(ComputerMapper.mapBDModel.get(strg)).nulls(NullPrecedence.LAST));
					} else {
						switch (param.getOrder()) {
						case ASC:
							criteria.addOrder(Order.asc(ComputerMapper.mapBDModel.get(strg)).nulls(NullPrecedence.LAST));
							break;
						case DESC:
							criteria.addOrder(Order.desc(ComputerMapper.mapBDModel.get(strg)).nulls(NullPrecedence.LAST));
							break;
						default:
							throw new DaoException(DaoException.INVALID_ARGUMENT);
						}
					}
				} else {
					throw new DaoException(DaoException.INVALID_ARGUMENT);
				}
			}
		}
	}

	public void setWhenCondition (Criteria criteria, DaoRequestParameter param) throws SQLException {
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
		
		Criterion computerName = Restrictions.like("computer.name", filter.toString());
		Criterion companyName = Restrictions.like("company.name", filter.toString());
		LogicalExpression orExp = Restrictions.or(computerName, companyName);
		criteria.add(orExp);
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
}
