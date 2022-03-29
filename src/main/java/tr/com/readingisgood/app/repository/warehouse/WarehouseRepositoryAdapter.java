package tr.com.readingisgood.app.repository.warehouse;

import org.springframework.stereotype.Component;
import tr.com.readingisgood.app.model.warehouse.Warehouse;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Component
public class WarehouseRepositoryAdapter {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String FIND_STOCK_BY_BOOKNAME_QUERY = "select w from Warehouse w, Book b where w.book.id = b.id and b.name in :bookName and w.bookOrder is null";

    public Warehouse findStockByBookNameV2(String bookName) {
        try {
            return entityManager.createQuery(FIND_STOCK_BY_BOOKNAME_QUERY, Warehouse.class)
                    .setParameter("bookName", bookName)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

}
