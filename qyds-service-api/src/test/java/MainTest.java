import net.dlyt.qyds.common.dto.OrdSubListExt;
import net.dlyt.qyds.web.service.common.YtUtil.YtApi;

public class MainTest {
    public static void main(String[] args) throws Exception {
        OrdSubListExt ordSubListExt = new OrdSubListExt();
        ordSubListExt.setSubOrderId("13123123123123");
        String result = YtApi.cancelYto(ordSubListExt);
        System.out.println(result);
    }
}
