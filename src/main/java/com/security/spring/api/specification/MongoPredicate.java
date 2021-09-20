package com.security.spring.api.specification;

import com.querydsl.core.types.dsl.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;

import static com.security.spring.api.validation.numeric.NumericDataValidator.validNumeric;


public class MongoPredicate {
    private static final Logger logger = LogManager.getLogger(MongoPredicate.class);

    private SearchCriteria param;
    private PathBuilder<?> entityPath;

    public MongoPredicate(PathBuilder<?> entityPath, SearchCriteria param) {
        this.entityPath = entityPath;
        this.param = param;
    }

    public BooleanExpression getPredicate(){
        if (param.getValue() instanceof String) {
            StringPath path = entityPath.getString(param.getKey());
            if (param.getOperation().equalsIgnoreCase(":")){
                return path.eq(param.getValue().toString());
            }
            if (param.getOperation().equalsIgnoreCase("in")){
                return path.in(param.getValue().toString());
            }
            if (param.getOperation().equalsIgnoreCase("like")){
                return path.toUpperCase().like(param.getValue().toString().toUpperCase());
            }
            if (param.getOperation().equalsIgnoreCase("not_null")){
                return path.isNotNull();
            }
            if (param.getOperation().equalsIgnoreCase("i")){
                return path.containsIgnoreCase(param.getValue().toString());
            }
            if (param.getOperation().equalsIgnoreCase("u")){
//                return path.containsIgnoreCase(param.getValue().toString());
//                return path.upper().eq(param.getValue().toString().toUpperCase());
//                return path.toUpperCase().equals(param.getValue().toString().toUpperCase());
//                StringExpression exp = Expressions.stringPath(param.getKey()).upper();
//                logger.info("EXP ::"+ ConvertToJson.setJsonString(exp));
//                StringExpression exp = Expressions.stringPath(param.getKey()).upper();
//                return (exp.eq("ABC"));
//                return path.toUpperCase().eq(param.getValue().toString());//(exp).eq("ABC");
                return path.equalsIgnoreCase(param.getValue().toString());
            }
        }
        else if (param.getValue() instanceof Boolean) {
            BooleanPath path = entityPath.getBoolean(param.getKey());
            boolean value = (boolean) param.getValue();
            return path.eq(value);
        }
        else if (validNumeric(param.getValue().toString())){
            NumberPath<Integer> path = entityPath.getNumber(param.getKey(), Integer.class);

            int value = Integer.parseInt(param.getValue().toString());

            switch (param.getOperation()){
                case ":":
                    return path.eq(value);
                case ">":
                    return path.goe(value);
                case "<":
                    return path.loe(value);
                case "in":
                    return path.in(value);
            }
        }
        else if (param.getValue() instanceof Date){
            DatePath<Date> path = entityPath.getDate(param.getKey(), Date.class);
            try {
                if (param.getOperation().equalsIgnoreCase("<=")) {
                    return path.loe(param.getStartDate());
                }
                if (param.getOperation().equalsIgnoreCase("between")) {
                    return path.between(param.getStartDate(), param.getEndDate());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (param.getValue() instanceof List) {
            NumberPath<Integer> path = entityPath.getNumber(param.getKey(), Integer.class);
            List<Integer> value = (List<Integer>) param.getValue();
            if (param.getOperation().equalsIgnoreCase("in")){
                return path.in(value);
            }
        }
        return null;
    }

}
