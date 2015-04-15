package com.excilys.cdb.persistence;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.model.Company;

/**
 * CompanyDao
 * @author Kevin Bottero
 *
 */
@Transactional
@Repository("companyDao")
public class CompanyDao implements IDao<Company, Long>{
	
	public CompanyDao() {};
	
	private static Logger logger = LoggerFactory.getLogger(CompanyDao.class);
	
	@Autowired
	private CompanyMapper companyMapper;
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Company> getAll() throws DaoException {
		return sessionFactory.getCurrentSession().createCriteria(Company.class).list();
	}
	
	@Override
	public List<Company> getAll(DaoRequestParameter param) throws DaoException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Company.class);
		addOrderByToRequest(criteria, param);
		return criteria.list();
	}

	@Override
	public Long getNb() throws DaoException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Company.class);
		Long nbElements = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		if (nbElements == null) {
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT);
		}
		return nbElements;
	}
	
	@Override
	public List<Company> getSome(DaoRequestParameter param) throws DaoException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Company.class);
		addOrderByToRequest(criteria, param);
		if (param.getNameLike() != null) {
			try {
				setWhenCondition (criteria, param);
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
	public Company getById(Long id) throws DaoException {
		if (id == null) {
			throw new DaoException(DaoException.INVALID_ARGUMENT);
		}
		Company comp=null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Company.class);
		criteria.add(Restrictions.idEq(id));
		comp = (Company) criteria.uniqueResult();
		if (comp == null) {
			throw new DaoException(DaoException.CAN_NOT_GET_ELEMENT);
		}
		return comp;
	}
	
	@Override
	public void delete(Long id)  throws DaoException {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		sessionFactory.getCurrentSession().delete(this.getById(id));
	}
	
	public void addOrderByToRequest(Criteria criteria, DaoRequestParameter param) throws DaoException {
		if ((param.getColToOrderBy() == null) || (param.getColToOrderBy().size() == 0)){
			if (param.getOrder() == null) {
				criteria.addOrder(Order.asc(CompanyMapper.DEFAULT_ID));
			} else {
				switch (param.getOrder()) {
				case ASC:
					criteria.addOrder(Order.asc(CompanyMapper.DEFAULT_ID));
					break;
				case DESC:
					criteria.addOrder(Order.desc(CompanyMapper.DEFAULT_ID));
					break;
				default:
					throw new DaoException(DaoException.INVALID_ARGUMENT);
				}
			}
		} else {
			for (String strg : param.getColToOrderBy()) {
				if (CompanyMapper.mapBDModel.containsKey(strg)) {
					if (param.getOrder() == null) {
						criteria.addOrder(Order.asc(strg));
					} else {
						switch (param.getOrder()) {
						case ASC:
							criteria.addOrder(Order.asc(strg));
							break;
						case DESC:
							criteria.addOrder(Order.desc(strg));
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
		
		criteria.add( Restrictions.like("name", param.getNameLike()));
	}
}
