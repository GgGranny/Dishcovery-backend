package com.dishcovery.backend.components;


import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class Pagination {

    public static <T> List<T> paginate(List<T> data, int pageNumber, int pageSize) {
        int fromIndex = (pageNumber -1) * pageSize;
        int toIndex = fromIndex + pageSize;

        if(fromIndex > data.size()) {
            return Collections.emptyList();
        }
        if(toIndex > data.size()) {
            toIndex = data.size();
        }
        return data.subList(fromIndex, toIndex);
    }
}
