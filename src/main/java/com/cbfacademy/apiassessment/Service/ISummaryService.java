package com.cbfacademy.apiassessment.Service;

import com.cbfacademy.apiassessment.DTO.Summary;

public interface ISummaryService {
    Summary getSummary(String usernameOrEmail);

}
