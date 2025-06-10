package com.connecple.connecple_backend.domain.connecple.intro.entity.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConnecpleHistoryBulkSaveRequest {

    private List<ConnecpleHistorySaveRequest> historyList = new ArrayList<>();
}
