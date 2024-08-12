package com.dws.challenge.domain;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Transfer {
	
	@NotNull
	@NotEmpty
	private final String fromAccountId;
	
	@NotNull
	@NotEmpty
	private final String toAccountId;
	
	@NotNull
	@Min(value = 1, message = "Should transfer a positive amount")
	private BigDecimal amount;

	  @JsonCreator
	  public Transfer(@JsonProperty("accountFromId") String fromAccountId,
			  @JsonProperty("accountToId") String toAccountId,
	    @JsonProperty("amount") BigDecimal amount) {
	    this.fromAccountId = fromAccountId;
	    this.toAccountId = toAccountId;
	    this.amount = amount;
	  }
}
