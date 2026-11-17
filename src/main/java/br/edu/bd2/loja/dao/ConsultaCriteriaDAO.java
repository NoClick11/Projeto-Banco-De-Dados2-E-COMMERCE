package br.edu.bd2.loja.dao;

import br.edu.bd2.loja.modelo.Pedido;
import br.edu.bd2.loja.modelo.Produto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ConsultaCriteriaDAO extends GenericDAO<Object, Long> {

    public ConsultaCriteriaDAO() {
        super(Object.class);
    }

    public List<Produto> produtosPorFaixaDePreco(BigDecimal minimo, BigDecimal maximo, int limite) {
        return executarSemTransacao(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Produto> cq = cb.createQuery(Produto.class);
            Root<Produto> p = cq.from(Produto.class);
            p.fetch("categoria");
            cq.select(p)
              .where(cb.isTrue(p.<Boolean>get("ativo")),
                     cb.between(p.<BigDecimal>get("preco"), minimo, maximo))
              .orderBy(cb.asc(p.get("preco")));
            return em.createQuery(cq).setMaxResults(limite).getResultList();
        });
    }

    public List<Pedido> pedidosFiltrados(FiltroPedido f) {
        return executarSemTransacao(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Pedido> cq = cb.createQuery(Pedido.class);
            Root<Pedido> pedido = cq.from(Pedido.class);
            Join<Object, Object> cliente = pedido.join("cliente");
            pedido.fetch("loja");

            List<Predicate> filtros = new ArrayList<>();
            if (f.nomeCliente != null && !f.nomeCliente.trim().isEmpty()) {
                filtros.add(cb.like(cb.lower(cliente.<String>get("nome")), "%" + f.nomeCliente.trim().toLowerCase() + "%"));
            }
            if (f.status != null) {
                filtros.add(cb.equal(pedido.get("status"), f.status));
            }
            if (f.lojaId != null) {
                filtros.add(cb.equal(pedido.get("loja").get("id"), f.lojaId));
            }
            if (f.dataInicio != null) {
                filtros.add(cb.greaterThanOrEqualTo(pedido.<LocalDateTime>get("dataHora"), f.dataInicio.atStartOfDay()));
            }
            if (f.dataFim != null) {
                filtros.add(cb.lessThan(pedido.<LocalDateTime>get("dataHora"), f.dataFim.plusDays(1).atStartOfDay()));
            }
            cq.select(pedido)
              .where(filtros.toArray(new Predicate[0]))
              .orderBy(cb.desc(pedido.get("dataHora")));
            return em.createQuery(cq)
                    .setFirstResult((Math.max(f.pagina, 1) - 1) * f.tamanho)
                    .setMaxResults(f.tamanho)
                    .getResultList();
        });
    }

    public List<Object[]> pedidosPorStatus() {
        return executarSemTransacao(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
            Root<Pedido> p = cq.from(Pedido.class);
            cq.multiselect(p.get("status"), cb.count(p))
              .groupBy(p.get("status"))
              .orderBy(cb.desc(cb.count(p)));
            return em.createQuery(cq).getResultList();
        });
    }
}
