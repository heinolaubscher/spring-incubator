package entelect.training.incubator.rewards;

import entelect.training.incubator.client.gen.CaptureRewardsRequest;
import entelect.training.incubator.client.gen.CaptureRewardsResponse;
import entelect.training.incubator.client.gen.RewardsBalanceRequest;
import entelect.training.incubator.client.gen.RewardsBalanceResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.math.BigDecimal;

public class RewardsClient extends WebServiceGatewaySupport {
    public CaptureRewardsResponse captureRewardsResponse(String passpoertNumber, BigDecimal amount) {
        CaptureRewardsRequest request = new CaptureRewardsRequest();
        request.setPassportNumber(passpoertNumber);
        request.setAmount(amount);

        CaptureRewardsResponse response = (CaptureRewardsResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
        return response;
    }

    public RewardsBalanceResponse rewardsBalanceResponse(String passportNumber) {
        RewardsBalanceRequest request = new RewardsBalanceRequest();
        request.setPassportNumber(passportNumber);

        RewardsBalanceResponse response = (RewardsBalanceResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
        return response;
    }
}
