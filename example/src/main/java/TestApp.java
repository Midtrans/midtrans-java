import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransCoreApi;

import java.util.HashMap;
import java.util.Map;

public class TestApp {

    public static void main(String[] args) {
        MidtransCoreApi coreApi = new ConfigFactory(new Config("SB-Mid-server-TOq1a2AVuiyhhOjvfs3U_KeO", "SB-Mid-client-nKsqvar5cn60u2Lv", false)).getCoreApi();

        coreApi.apiConfig().setEnabledLog(true);

        Map<Object, String> transaction = new HashMap<>();

        Map<String, String> details = new HashMap<>();

    }
}
