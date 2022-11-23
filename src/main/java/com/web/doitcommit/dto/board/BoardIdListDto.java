package com.web.doitcommit.dto.board;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardIdListDto {

    private List<Long> boardIdList = new ArrayList<>();

}
