package com.ewd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PiChartResponse {
	
	private long sowPendingCount;
	private long sowSentCount;

}
