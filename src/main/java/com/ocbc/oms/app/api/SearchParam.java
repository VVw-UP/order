package com.ocbc.oms.app.api;

import lombok.Builder;
import lombok.Data;

@Data
public class SearchParam {
    String searchType;
    String value;
}
