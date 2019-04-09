package com.jojoldu.batch.job.common;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.util.ClassUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class QuerydslPagingItemReader<T> extends AbstractPagingItemReader<T> {

    private EntityManagerFactory entityManagerFactory;
    private final Map<String, Object> jpaPropertyMap = new HashMap<>();

    private EntityManager entityManager;

    Function<JPAQueryFactory, JPAQuery<T>> queryFunction;

    private boolean transacted = true;//default value

    public void setTransacted(boolean transacted) {
        this.transacted = transacted;
    }

    private QuerydslPagingItemReader() {
        setName(ClassUtils.getShortName(QuerydslPagingItemReader.class));
    }

    public QuerydslPagingItemReader(EntityManagerFactory entityManagerFactory, int pageSize, Function<JPAQueryFactory, JPAQuery<T>> queryFunction) {
        this();
        setEntityManagerFactory(entityManagerFactory);
        setQuery(queryFunction);
        setPageSize(pageSize);
    }

    private void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private void setQuery(Function<JPAQueryFactory, JPAQuery<T>> queryFunction) {
        this.queryFunction = queryFunction;
    }

    private JPAQuery<T> createQuery() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        return queryFunction.apply(queryFactory);
    }


    @Override
    protected void doOpen() throws Exception {
        super.doOpen();

        entityManager = entityManagerFactory.createEntityManager(jpaPropertyMap);
        if (entityManager == null) {
            throw new DataAccessResourceFailureException("Unable to obtain an EntityManager");
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doReadPage() {

        if (transacted) {
            entityManager.clear();
        }//end if

        JPAQuery<T> query = createQuery().offset(getPage() * getPageSize()).limit(getPageSize());

        if (results == null) {
            results = new CopyOnWriteArrayList<T>();
        } else {
            results.clear();
        }

        if (!transacted) {
            List<T> queryResult = query.fetch();
            for (T entity : queryResult) {
                entityManager.detach(entity);
                results.add(entity);
            }//end if
        } else {
            results.addAll(query.fetch());
        }//end if
    }

    @Override
    protected void doJumpToPage(int itemIndex) {
    }

    @Override
    protected void doClose() throws Exception {
        entityManager.close();
        super.doClose();
    }
}
