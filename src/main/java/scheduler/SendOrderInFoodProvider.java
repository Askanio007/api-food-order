package scheduler;

import converter.XmlConverter;
import entity.ProviderOrders;
import enums.RequestState;
import models.xmlProviderResponse.ProviderResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import scheduler.pushNotification.OrderWasSendToProvider;
import service.MenuService;
import service.OrderService;
import service.ProviderOrdersService;
import service.SettingService;
import utils.DateBuilder;

import static utils.DateBuilder.today;

@Component
public class SendOrderInFoodProvider implements Runnable {

    private static final Logger log = Logger.getLogger(SendOrderInFoodProvider.class);

    @Autowired
    private MenuService menuService;

    @Autowired
    @Qualifier("myScheduler")
    private TaskScheduler scheduler;

    @Autowired
    private OrderWasSendToProvider orderWasSendToProvider;

    @Autowired
    private SettingService settingService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProviderOrdersService providerOrdersService;

    private static RestTemplate restTemplate;

    private static HttpHeaders headers;

    static {
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    @Override
    public void run() {
        log.info("Sending order in provider...");
        try {
            ProviderOrders po = providerOrdersService.getToday();
            String order = XmlConverter.xmlToString(orderService.generateXmlOrder());
            if (order == null) {
                log.error("order is null. Sending failed");
                return;
            }
            po.setRequest(order);
            po.setDateSend(today());
            po.setResponse(null);
            po.setRequestState(RequestState.PENDING);
            providerOrdersService.save(po);
            ResponseEntity<String> res = sendingOrder(Integer.parseInt(settingService.getCountAttemptSending()), settingService.getApiUrl(), createRequest(order));
            if (res != null) {
                po.setResponse(res.getBody());
                ProviderResponse providerResponse = XmlConverter.stringToXml(res.getBody().replace("&lt;", "<").replace("&gt;", ">"));
                if (providerResponse != null) {
                    if (providerResponse.getOrder() != null) {
                        po.setIdOrder(providerResponse.getOrder().getId());
                        po.setCodeOrder(providerResponse.getOrder().getCode());
                    }
                }
                menuService.deactivateTodayMenu();
                log.info("Sending completed successfully!");
                po.setRequestState(RequestState.COMPLETE);
                if (po.getCodeOrder() == null || "".equals(po.getIdOrder()) || "".equals(po.getCodeOrder()) || po.getIdOrder() == null)
                    po.setRequestState(RequestState.FAILED);
            }
            else {
                po.setRequestState(RequestState.FAILED);
                log.info("Mama mia don't send response");
            }
            if (po.getRequestState() != RequestState.FAILED)
                scheduler.schedule(orderWasSendToProvider, DateBuilder.addOneMinutes(today()));
            providerOrdersService.save(po);
        } catch (Exception e) {
            log.error("Sending is failed", e);
        }
    }

    private ResponseEntity<String> sendingOrder(int countAttempt, String apiUrl,HttpEntity req) {
        ResponseEntity<String> res = null;
        try {
            res = restTemplate.postForEntity(apiUrl, req, String.class);
        } catch (ResourceAccessException e) {
            if (countAttempt > 0) {
                log.info("Timeout exception. Try again...");
                countAttempt--;
                return sendingOrder(countAttempt, apiUrl, req);
            }
        }
        return res;
    }

    private HttpEntity<MultiValueMap<String, String>> createRequest(String order) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("order", order);
        map.add("orderText", "from solid systems");
        return new HttpEntity<>(map, headers);
    }
}
