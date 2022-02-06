package com.ocbc.oms.app.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SearchResult {
    private List<SearchResponse> searchResponseList;
}
