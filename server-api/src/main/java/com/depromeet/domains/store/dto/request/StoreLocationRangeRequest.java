package com.depromeet.domains.store.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoreLocationRangeRequest {

	@NotNull
	@DecimalMin(value = "0.0", inclusive = true, message = "위도의 최소값은 0.0입니다.")
	@DecimalMax(value = "90.0", inclusive = true, message = "위도의 최대값은 90.0입니다.")
	private Double latitude;

	@NotNull
	@DecimalMin(value = "-180.0", inclusive = true, message = "경도의 최소값은 -180.0입니다.")
	@DecimalMax(value = "180.0", inclusive = true, message = "경도의 최대값은 180.0입니다.")
	private Double longitude;

}
