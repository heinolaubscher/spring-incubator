package entelect.training.incubator.spring.loyalty.ws.server;

import java.math.BigDecimal;

public interface RewardsService {
    
    BigDecimal updateBalance(String passportNumber, BigDecimal amount);
    
    BigDecimal getBalance(String passportNumber);
    
}
