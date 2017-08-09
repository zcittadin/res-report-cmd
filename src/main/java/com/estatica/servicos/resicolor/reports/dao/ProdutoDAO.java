package com.estatica.servicos.resicolor.reports.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import com.estatica.servicos.resicolor.reports.model.Processo;
import com.estatica.servicos.resicolor.reports.model.Produto;
import com.estatica.servicos.resicolor.reports.util.HibernateUtil;

public class ProdutoDAO {

	@SuppressWarnings("unchecked")
	public Produto findByLote(int lote) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		String hql = "SELECT p FROM Produto p WHERE p.lote = " + lote;
		Query query = session.createQuery(hql);
		List<Produto> list = query.getResultList();
		if (list.isEmpty())
			return null;
		Produto p = list.get(0);
		hql = "SELECT p FROM Processo p  WHERE p.produto = " + p.getId();
		query = session.createQuery(hql);
		List<Processo> lista = query.getResultList();
		p.setProcessos(lista);
		session.close();
		return p;
	}
}
