package com.security.spring.api.specification;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MongoPredicateBuilder {

    /*example with paging new MongoPredicateBuilder(new PathBuilder<>(SmeApplicationLog.class, "sme_application_log")).with("reference", ":", applications.get(0).getReference()).build(), PageRequest.of(0, 1, Sort.Direction.DESC, "id") */

    private static final Logger logger = LogManager.getLogger(MongoPredicateBuilder.class);

    private List<SearchCriteria> params;
    private PathBuilder<?> entityPath;

    public MongoPredicateBuilder() {
        params = new ArrayList<>();
    }

    public MongoPredicateBuilder with(String key, String operation, Object value){
        params.add(key == null || key.trim().isEmpty() || ((operation == null || operation.trim().isEmpty()) && !operation.trim().equals("not_null")) || value == null || value.toString().trim().isEmpty() ? null : new SearchCriteria(key, operation, value));
        params.removeIf(p -> p == null);
        return this;
    }

    public MongoPredicateBuilder with(String key, String operation, Date startDate, Date endDate){
        params.add((operation == null || operation.trim().isEmpty()) || (startDate == null && endDate == null) ? null : new SearchCriteria(key, operation, startDate, endDate));
        params.removeIf(p -> p == null);
        return this;
    }

    public MongoPredicateBuilder(PathBuilder<?> entityPath){
        params = new ArrayList<>();
        this.entityPath = entityPath;
    }

    public BooleanExpression build(){
        try {
            params.removeIf(p -> p == null);
            if (params.size() == 0) {
                return null;
            }

            List<BooleanExpression> predicates = new ArrayList<>();
            MongoPredicate predicate;

            for (SearchCriteria param : params) {

                predicate = new MongoPredicate(entityPath, param);
                BooleanExpression exp = predicate.getPredicate();

                if (exp != null) {
                    predicates.add(exp);
                }
            }
            if (!predicates.isEmpty()) {
                BooleanExpression result = predicates.get(0);
                for (int i = 0; i < predicates.size(); i++) {
                    result = result.and(predicates.get(i));
                }
                logger.info("RESULT  : " + result);
                return result;
            }
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
